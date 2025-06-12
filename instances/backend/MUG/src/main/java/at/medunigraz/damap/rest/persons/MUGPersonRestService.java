package at.medunigraz.damap.rest.persons;

import at.medunigraz.api.rest.base.services.AuthorizationClientHeadersFactory;
import at.medunigraz.api.rest.base.services.MUGAPIPersonServiceBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@RegisterRestClient(configKey = "rest.persons")
@RegisterClientHeaders(value = AuthorizationClientHeadersFactory.class)
public interface MUGPersonRestService extends MUGAPIPersonServiceBase {}
