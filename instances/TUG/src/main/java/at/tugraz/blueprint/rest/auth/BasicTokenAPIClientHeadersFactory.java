package at.tugraz.blueprint.rest.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

@JBossLog
@ApplicationScoped
public class BasicTokenAPIClientHeadersFactory implements ClientHeadersFactory {

  @Inject CredentialsService credentialsService;

  @Override
  public MultivaluedMap<String, String> update(
      MultivaluedMap<String, String> incomingHeaders,
      MultivaluedMap<String, String> clientOutgoingHeaders) {
    MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
    String token = credentialsService.createBasicAuthToken();
    result.add("Authorization", "Basic " + token);
    return result;
  }
}
