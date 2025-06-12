package at.medunigraz.damap.rest.persons;

import at.medunigraz.api.rest.base.models.MUGSearchResult;
import at.medunigraz.damap.rest.dmp.domain.MUGPerson;
import at.medunigraz.damap.rest.dmp.mapper.MUGPersonDOMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.jbosslog.JBossLog;
import org.damap.base.rest.base.ResultList;
import org.damap.base.rest.base.Search;
import org.damap.base.rest.dmp.domain.ContributorDO;
import org.damap.base.rest.persons.PersonService;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@JBossLog
@ApplicationScoped
public class MUGPersonServiceImpl implements PersonService {

  @Inject @RestClient MUGPersonRestService restService;

  @Override
  public ContributorDO read(String id, MultivaluedMap<String, String> queryParams) {
    MUGPerson contributor = restService.read(id, null);

    return MUGPersonDOMapper.mapEntityToDO(contributor, new ContributorDO());
  }

  @Override
  public ResultList<ContributorDO> search(MultivaluedMap<String, String> queryParams) {
    Search s = Search.fromMap(queryParams);
    int limit = s.getPagination().getPerPage();
    int offset = ((s.getPagination().getPage() < 1 ? 1 : s.getPagination().getPage()) - 1) * limit;

    MUGSearchResult<MUGPerson> people = restService.search(s.getQuery(), offset, limit, null);

    List<ContributorDO> contributors =
        people.getResults().stream()
            .map(c -> MUGPersonDOMapper.mapEntityToDO(c, new ContributorDO()))
            .collect(Collectors.toList());

    s.getPagination().setNumTotalItems(people.getCount());
    return ResultList.fromItemsAndSearch(contributors, s);
  }
}
