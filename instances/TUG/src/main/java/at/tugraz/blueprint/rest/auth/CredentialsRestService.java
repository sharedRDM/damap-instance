package at.tugraz.blueprint.rest.auth;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Singleton
@RegisterRestClient(configKey = "rest.tugraz.api.auth")
@RegisterClientHeaders(BasicTokenAPIClientHeadersFactory.class)
public interface CredentialsRestService {

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  CredentialsResponseBody getToken(@FormParam("grant_type") String grantType);
}
