package at.medunigraz.damap.rest.dmp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MUGProject {
  @Size(max = 255)
  private String id;

  @JsonProperty(value = "abstract")
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  private MultiLanguageEntry description;

  @JsonProperty(value = "title")
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  private MultiLanguageEntry name;

  @JsonProperty(value = "short")
  private String acronym;

  @JsonProperty(value = "begin_effective")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
  private Date startDate;

  @JsonProperty(value = "end_effective")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
  private Date endDate;

  // List of person IDs with prefix and suffix. Format:
  // "<organizationID>-<personID>-<projectRoleID>"
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  private List<String> persons;

  private String manager;
}
