package at.tugraz.blueprint.rest.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CredentialsResponseBody {
  @JsonProperty(value = "access_token")
  String accessToken;

  // value in seconds
  @JsonProperty(value = "expires_in")
  long expiresIn;
}
