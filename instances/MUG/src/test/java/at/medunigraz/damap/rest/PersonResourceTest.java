package at.medunigraz.damap.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import at.medunigraz.api.rest.base.models.MUGSearchResult;
import at.medunigraz.damap.rest.dmp.domain.MUGPerson;
import at.medunigraz.damap.rest.persons.MUGPersonRestService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import java.util.List;
import org.damap.base.rest.PersonResource;
import org.damap.base.rest.persons.orcid.ORCIDPersonServiceImpl;
import org.damap.base.security.SecurityService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
@TestHTTPEndpoint(PersonResource.class)
class PersonResourceTest {

  @InjectMock SecurityService securityService;

  @InjectMock @RestClient MUGPersonRestService mockPersonRestService;

  @InjectMock ORCIDPersonServiceImpl orcidPersonServiceImpl;

  @BeforeEach
  public void setup() {
    MUGPerson mockPerson = new MUGPerson();
    mockPerson.setEmail("first.last@medunigraz.at");
    mockPerson.setFirstName("first");
    mockPerson.setLastName("last");
    mockPerson.setId("medunigraz-first-last");

    MUGSearchResult<MUGPerson> mockSearchResult = new MUGSearchResult<>();
    mockSearchResult.setCount(1);
    mockSearchResult.setResults(List.of(mockPerson));

    Mockito.when(securityService.getUserId()).thenReturn("012345");
    Mockito.when(securityService.getUserName()).thenReturn("testUser");
    Mockito.when(
            mockPersonRestService.search(anyString(), anyInt(), anyInt(), nullable(List.class)))
        .thenReturn(mockSearchResult);
  }

  @Test
  @TestSecurity(user = "userJwt", roles = "user")
  void testSearchPeople() {
    given()
        .queryParams("searchService", "UNIVERSITY", "q", "")
        .get("/")
        .then()
        .statusCode(200)
        .body("items.size", not(0));

    verify(mockPersonRestService, times(1))
        .search(anyString(), anyInt(), anyInt(), nullable(List.class));
    verify(orcidPersonServiceImpl, times(0)).search(any());
  }
}
