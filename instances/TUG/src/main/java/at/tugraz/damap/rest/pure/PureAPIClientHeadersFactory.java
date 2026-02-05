package at.tugraz.damap.rest.pure;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

/** TUG-specific implementation for injecting the API key into Pure API requests. */
@ApplicationScoped
public class PureAPIClientHeadersFactory implements ClientHeadersFactory {
  @ConfigProperty(name = "damap.elsevier-pure-api-key")
  String apiKey;

  @Override
  public MultivaluedMap<String, String> update(
      MultivaluedMap<String, String> incomingHeaders,
      MultivaluedMap<String, String> clientOutgoingHeaders) {
    MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
    // Intentionally no logging here (to keep Quarkus dev output readable).
    if (apiKey != null && !apiKey.isEmpty()) {
      result.add("api-key", apiKey);
    }
    return result;
  }
}
