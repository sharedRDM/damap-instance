package at.tugraz.blueprint.rest.auth;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@JBossLog
@Singleton
public class CredentialsService {

  @ConfigProperty(name = "damap.tugraz.api.auth.client")
  String authClient;

  @ConfigProperty(name = "damap.tugraz.api.auth.secret")
  String authSecret;

  String token;
  Instant expiresAt;

  public String createBasicAuthToken() {
    return Base64.getEncoder().encodeToString((authClient + ":" + authSecret).getBytes());
  }

  private boolean tokenExpired() {
    return expiresAt.isBefore(Instant.now());
  }

  @Inject @RestClient CredentialsRestService credentialsRestService;

  public String getToken() {
    if (token == null || tokenExpired()) {
      try {
        log.info("Fetching new auth token");
        var response = credentialsRestService.getToken("client_credentials");
        token = response.accessToken;

        // Subtract grace period to prevent "time of check" vs "time of use" problem
        long gracePeriodSeconds = 30;
        long expiresInWithGrace = Math.max(0, response.expiresIn - gracePeriodSeconds);

        expiresAt = Instant.now().plus(expiresInWithGrace, ChronoUnit.SECONDS);

      } catch (Exception e) {
        log.warn("Failed to retrieve token with rest service: " + e);
      }
    }
    return token;
  }
}
