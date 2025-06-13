package at.tugraz.blueprint.rest.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class SearchResult<T> {
  @JsonProperty(value = "hydra:member")
  List<T> items = new ArrayList<>();
}
