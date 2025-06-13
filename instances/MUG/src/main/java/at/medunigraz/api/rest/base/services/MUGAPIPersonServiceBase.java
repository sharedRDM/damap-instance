package at.medunigraz.api.rest.base.services;

import at.medunigraz.api.rest.base.models.MUGSearchResult;
import at.medunigraz.damap.rest.dmp.domain.MUGPerson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;

@Produces(MediaType.APPLICATION_JSON)
@RegisterClientHeaders(value = AuthorizationClientHeadersFactory.class)
public interface MUGAPIPersonServiceBase {

  // The API currently applies an AND operation for each filter (i.e.
  // first_name__contains AND last_name__contains). Currenly, we only search
  // for one field, as we do not know how people will search.
  @GET
  @Path("")
  MUGSearchResult<MUGPerson> search(
      @QueryParam("last_name__icontains") String firstName,
      @QueryParam("offset") int offset,
      @QueryParam("limit") int limit,
      @QueryParam("expand") List<String> expand);

  @GET
  @Path("/{id}")
  @Consumes(value = "application/json")
  MUGPerson read(@PathParam("id") String id, @QueryParam("expand") List<String> expand);
}
