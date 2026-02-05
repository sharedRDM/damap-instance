package at.tugraz.damap.integration.pure;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.extern.jbosslog.JBossLog;
import org.damap.base.integration.PersonService;
import org.damap.base.integration.pure.PureAPI;
import org.damap.base.integration.pure.PureAPIPerson;
import org.damap.base.rest.base.ResultList;
import org.damap.base.rest.base.Search;
import org.damap.base.rest.dmp.domain.ContributorDO;

/**
 * TUG-specific Pure Person Service. Core service fetches all 4604+ persons (too slow). This uses
 * efficient pagination: empty query = 1 API call, with query = scans first 2000 max.
 */
@ApplicationScoped
@Alternative
@Priority(100)
@JBossLog
public class TUGrazPurePersonService implements PersonService {

  @Inject PureAPI pureAPI;
  @Inject PurePersonSearchService purePersonSearchService;

  @Override
  public ContributorDO read(String id) {
    PureAPIPerson person = pureAPI.getPerson(id);
    return PureAPIPersonConverter.toContributor(person);
  }

  @Override
  public ResultList<ContributorDO> search(Search search) {
    log.info("TUGrazPurePersonService.search called - delegating to PurePersonSearchService");
    return purePersonSearchService.search(search);
  }
}
