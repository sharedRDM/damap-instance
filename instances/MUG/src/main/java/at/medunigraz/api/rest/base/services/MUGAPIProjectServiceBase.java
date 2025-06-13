package at.medunigraz.api.rest.base.services;

import at.medunigraz.api.rest.base.models.MUGSearchResult;
import at.medunigraz.damap.rest.dmp.domain.MUGProject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.time.Instant;
import java.util.List;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;

@Produces(MediaType.APPLICATION_JSON)
@RegisterClientHeaders(value = AuthorizationClientHeadersFactory.class)
public interface MUGAPIProjectServiceBase {
  @GET
  @Path("")
  @ClientHeaderParam(name = "accept", value = "application/json")
  MUGSearchResult<MUGProject> search(
      @QueryParam("title") String title,
      @QueryParam("end_effective__gte") Instant endEffective,
      @QueryParam("offset") int offset,
      @QueryParam("limit") int limit);

  @GET
  @Path("/{id}")
  @Consumes(value = "application/json")
  MUGProject read(@PathParam("id") String id, @QueryParam("expand") List<String> expand);
}
