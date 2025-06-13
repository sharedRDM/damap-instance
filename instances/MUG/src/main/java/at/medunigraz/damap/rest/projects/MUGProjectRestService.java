package at.medunigraz.damap.rest.projects;

import at.medunigraz.api.rest.base.services.MUGAPIProjectServiceBase;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "rest.projects")
public interface MUGProjectRestService extends MUGAPIProjectServiceBase {}
