package at.tugraz.damap.integration.pure;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.damap.base.integration.pure.PureAPI;
import org.damap.base.integration.pure.PureAPIPaginatedPersonsResponse;

/** Helper for fetching paginated persons from Pure API. */
@ApplicationScoped
public class PureAPIPaginationService {
  @Inject PureAPI pureAPI;

  public PureAPIPaginatedPersonsResponse fetchPersonsPage(long size, long offset) {
    return pureAPI.listAllPersons(size, offset);
  }
}
