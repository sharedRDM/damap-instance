package at.medunigraz.api.rest.base.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.Optional;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

@JBossLog
@ApplicationScoped
public class AuthorizationClientHeadersFactory implements ClientHeadersFactory {

  @ConfigProperty(name = "damap.mug.api.auth.token")
  Optional<String> authToken;

  @Override
  public MultivaluedMap<String, String> update(
      MultivaluedMap<String, String> incomingHeaders,
      MultivaluedMap<String, String> clientOutgoingHeaders) {
    MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
    if (authToken.isPresent() && !authToken.get().trim().isEmpty()) {
      // Add authorization header if token is set in config
      result.add("Authorization", authToken.get());
    }
    return result;
  }
}
