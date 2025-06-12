package at.medunigraz.damap.rest.dmp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class MultiLanguageEntry {
  String de;
  String en;
}
