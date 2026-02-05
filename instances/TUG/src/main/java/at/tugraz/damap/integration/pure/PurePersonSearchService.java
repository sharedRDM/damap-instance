package at.tugraz.damap.integration.pure;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedHashMap;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.jbosslog.JBossLog;
import org.damap.base.integration.pure.PureAPIPaginatedPersonsResponse;
import org.damap.base.integration.pure.PureAPIPerson;
import org.damap.base.rest.base.Pagination;
import org.damap.base.rest.base.ResultList;
import org.damap.base.rest.base.Search;
import org.damap.base.rest.dmp.domain.ContributorDO;

/**
 * Efficient Pure person search for TUG (4604+ persons).
 * Empty query: fetches requested page only. With query: scans first 2000 persons max.
 */
@ApplicationScoped
@JBossLog
public class PurePersonSearchService {
  private static final long SCAN_PAGE_SIZE = 50;
  private static final int MAX_SCAN_PAGES = 40; // 40 * 50 = 2000 persons scanned at most

  @Inject PureAPIPaginationService pagination;

  public ResultList<ContributorDO> search(Search search) {
    log.info("PurePersonSearchService.search called with query: " + (search != null ? search.getQuery() : "null"));
    ResultList<ContributorDO> result = new ResultList<>();
    result.setSearch(search);

    Pagination p = (search != null && search.getPagination() != null) ? search.getPagination() : new Pagination();
    int page = p.getPage();
    int perPage = p.getPerPage();

    if (page < 1) page = 1;
    if (perPage < 1) perPage = 10;

    String query = (search != null) ? search.getQuery() : null;

    if (query == null || query.isBlank()) {
      long offset = (long) (page - 1) * perPage;
      log.info("Fetching Pure API page: offset=" + offset + ", size=" + perPage);
      PureAPIPaginatedPersonsResponse response;
      try {
        response = pagination.fetchPersonsPage((long) perPage, offset);
        log.info("Pure API returned " + (response.getItems() != null ? response.getItems().size() : 0) + " persons");
      } catch (Exception e) {
        log.error("Pure API call failed", e);
        result.setItems(List.of());
        return result;
      }
      List<ContributorDO> items =
          (response.getItems() == null)
              ? List.of()
              : response.getItems().stream().map(PureAPIPersonConverter::toContributor).filter(x -> x != null).toList();

      result.setItems(items);

      Pagination newP = new Pagination();
      MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
      map.add("page", String.valueOf(page));
      map.add("perPage", String.valueOf(perPage));
      newP.setPage(map);
      newP.setPerPage(map);
      try {
        newP.setNumTotalItems((int) response.getCount());
      } catch (Exception ignored) {
        newP.setNumTotalItems(null);
      }
      newP.calculateFields();
      result.getSearch().setPagination(newP);

      return result;
    }

    // Case 2: query present -> scan pages, filter client-side, then apply UI pagination
    log.info("Searching with query: '" + query + "', scanning up to " + MAX_SCAN_PAGES + " pages");
    String q = query.toLowerCase();
    int desiredStart = (page - 1) * perPage;
    int desiredEndExclusive = desiredStart + perPage;

    List<ContributorDO> matches = new ArrayList<>(desiredEndExclusive);
    long offset = 0;

    boolean reachedEnd = false;
    for (int i = 0; i < MAX_SCAN_PAGES; i++) {
      PureAPIPaginatedPersonsResponse response;
      try {
        response = pagination.fetchPersonsPage(SCAN_PAGE_SIZE, offset);
      } catch (Exception e) {
        log.error("Pure API call failed at page " + i + ", offset " + offset, e);
        break;
      }
      List<PureAPIPerson> persons = response.getItems();
      if (persons == null || persons.isEmpty()) {
        reachedEnd = true;
        break;
      }

      for (PureAPIPerson person : persons) {
        ContributorDO c = PureAPIPersonConverter.toContributor(person);
        if (c == null) continue;
        if (matchesQuery(c, q)) {
          matches.add(c);
          if (matches.size() >= desiredEndExclusive) {
            break;
          }
        }
      }

      if (matches.size() >= desiredEndExclusive) {
        break;
      }

      if (response.getPageInformation() != null) {
        offset = response.getPageInformation().getOffset() + response.getPageInformation().getSize();
        if (response.getCount() <= response.getPageInformation().getOffset() + response.getPageInformation().getSize()) {
          reachedEnd = true;
          break;
        }
      } else {
        offset += SCAN_PAGE_SIZE;
      }
    }

    int from = Math.min(desiredStart, matches.size());
    int to = Math.min(desiredEndExclusive, matches.size());
    result.setItems(matches.subList(from, to));
    log.info("Search completed: found " + matches.size() + " matches, returning " + result.getItems().size() + " items");

    Pagination newP = new Pagination();
    MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
    map.add("page", String.valueOf(page));
    map.add("perPage", String.valueOf(perPage));
    newP.setPage(map);
    newP.setPerPage(map);
    newP.setNumTotalItems(null);
    newP.setNumPages(null);
    newP.setHasPrevious(page > 1);
    newP.setHasNext(!reachedEnd && matches.size() >= desiredEndExclusive);
    result.getSearch().setPagination(newP);

    return result;
  }

  private boolean matchesQuery(ContributorDO item, String lowerQuery) {
    return (item.getFirstName() != null && item.getFirstName().toLowerCase().contains(lowerQuery))
        || (item.getLastName() != null && item.getLastName().toLowerCase().contains(lowerQuery))
        || (item.getPersonId() != null
            && item.getPersonId().getIdentifier() != null
            && item.getPersonId().getIdentifier().toLowerCase().contains(lowerQuery));
  }
}
