package at.tugraz.damap.rest.projects;

import at.tugraz.blueprint.rest.auth.BearerTokenClientHeadersFactory;
import at.tugraz.blueprint.rest.services.ProjectServiceBase;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "rest.projects")
@RegisterClientHeaders(BearerTokenClientHeadersFactory.class)
public interface TUGrazProjectRestService extends ProjectServiceBase {}
