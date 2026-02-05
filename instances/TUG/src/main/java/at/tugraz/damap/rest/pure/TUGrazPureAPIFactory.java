package at.tugraz.damap.rest.pure;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.damap.base.integration.pure.FileBasedPureAPI;
import org.damap.base.integration.pure.PureAPI;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * TUG-specific factory for Pure API that uses TUGrazPureAPI (with TUG-specific authentication).
 * This factory replaces the core PureAPIFactory when enabled via beans.xml.
 */
@ApplicationScoped
@Alternative
@Priority(100)
public class TUGrazPureAPIFactory {
  @ConfigProperty(name = "damap.elsevier-pure-backend", defaultValue = "http")
  String backend;

  @Inject Instance<FileBasedPureAPI> fileAPI;

  @Inject @RestClient Instance<TUGrazPureAPI> tugrazPureAPI;

  @Produces
  @Priority(100)
  public PureAPI create() {
    return switch (backend) {
      case "file" -> fileAPI.get();
      case "http" -> tugrazPureAPI.get();
      default -> throw new IllegalArgumentException("Pure API backend not supported: " + backend);
    };
  }
}
