package at.medunigraz.damap.rest.dmp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MUGPerson {
  @Size(max = 255)
  private String id;

  @Size(max = 255)
  @JsonProperty(value = "first_name")
  private String firstName;

  @Size(max = 255)
  @JsonProperty(value = "last_name")
  private String lastName;

  @Size(max = 255)
  private String email;
}
