package at.medunigraz.api.rest.base.models;

import java.util.List;
import lombok.Data;

@Data
public class MUGSearchResult<T> {
  private int count;
  private String next;
  private String previous;
  private List<T> results;
}
