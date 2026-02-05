package at.tugraz.damap.rest.pure;

import io.quarkus.arc.lookup.LookupIfProperty;
import jakarta.enterprise.inject.Typed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.damap.base.integration.pure.PureAPI;
import org.damap.base.integration.pure.PureAPIPaginatedPersonsResponse;
import org.damap.base.integration.pure.PureAPIPaginatedProjectsResponse;
import org.damap.base.integration.pure.PureAPIPerson;
import org.damap.base.integration.pure.PureAPIProject;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * TUG-specific Pure API REST client that uses TUG-specific authentication header factory. This
 * ensures the API key is properly read from the TUG instance configuration.
 *
 * <p><strong>Data Source Verification:</strong>
 *
 * <ul>
 *   <li>Base URL: {@code https://tugraz-staging.elsevierpure.com/ws/api} (from {@code
 *       damap.elsevier-pure-endpoint-url})
 *   <li>Authentication: API key header via {@link PureAPIClientHeadersFactory} (from {@code
 *       damap.elsevier-pure-api-key})
 *   <li>Persons endpoint: {@code GET /persons?size=X&offset=Y}
 *   <li>Projects endpoint: {@code GET /projects?size=X&offset=Y}
 * </ul>
 *
 * <p>All data comes directly from the TUGraz Pure API instance. No data is mixed from other sources
 * (University API, ORCID API, etc.). The ORCID values in results are stored in Pure and returned by
 * the Pure API.
 */
@Path("")
@RegisterRestClient(configKey = "elsevier-pure")
@RegisterClientHeaders(PureAPIClientHeadersFactory.class)
@Produces(MediaType.APPLICATION_JSON)
@Typed(TUGrazPureAPI.class)
@LookupIfProperty(name = "damap.elsevier-pure-backend", stringValue = "http")
public interface TUGrazPureAPI extends PureAPI {
  /**
   * List all projects using pagination.
   *
   * @param size the size of the page.
   * @param offset the offset to start at.
   * @return the response containing the projects on that page.
   */
  @GET
  @Path("/projects")
  @Override
  PureAPIPaginatedProjectsResponse listAllProjects(
      @QueryParam("size") Long size, @QueryParam("offset") Long offset);

  /** Search projects using Pure's {@code q} query parameter. */
  @GET
  @Path("/projects")
  PureAPIPaginatedProjectsResponse searchProjects(
      @QueryParam("q") String q, @QueryParam("size") Long size, @QueryParam("offset") Long offset);

  /**
   * Retrieve a project with a specific ID.
   *
   * @param uuid the ID of the project.
   * @return the project if found, or null if the project was not found.
   */
  @GET
  @Path("/projects/{uuid}")
  @Override
  PureAPIProject getProject(@PathParam("uuid") String uuid);

  /**
   * Retrieve all persons in the database, paginated.
   *
   * @param size the size of the page.
   * @param offset the offset to start at.
   * @return the response containing the list of persons.
   */
  @GET
  @Path("/persons")
  @Override
  PureAPIPaginatedPersonsResponse listAllPersons(
      @QueryParam("size") Long size, @QueryParam("offset") Long offset);

  /**
   * Fetch a single person based on their ID.
   *
   * @param uuid the ID of the person to fetch.
   * @return the person if found, or null if the person was not found.
   */
  @GET
  @Path("/persons/{uuid}")
  @Override
  PureAPIPerson getPerson(@PathParam("uuid") String uuid);
}
