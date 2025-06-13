package at.tugraz.damap.rest.persons;

import at.tugraz.blueprint.rest.auth.CredentialsService;
import at.tugraz.blueprint.rest.models.SearchResult;
import at.tugraz.damap.rest.dmp.domain.TUGrazPerson;
import at.tugraz.damap.rest.dmp.mapper.TUGrazPersonDOMapper;
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
public class TUGrazPersonServiceImpl implements PersonService {

  @Inject @RestClient TUGrazPersonRestService tuGrazPersonRestService;

  @Inject CredentialsService credentialsService;

  @Override
  public ContributorDO read(String id, MultivaluedMap<String, String> queryParams) {
    TUGrazPerson contributor;
    // We first try to get local attributes. If a field does not exist or we do
    // not have permission to access it, this will throw. So we try again without
    // fetching locals.
    try {
      contributor = tuGrazPersonRestService.read(id, String.join(",", List.of("email")));
    } catch (Exception e) {
      contributor = tuGrazPersonRestService.read(id, null);
    }

    return TUGrazPersonDOMapper.mapEntityToDO(contributor, new ContributorDO());
  }

  @Override
  public ResultList<ContributorDO> search(MultivaluedMap<String, String> queryParams) {
    Search s = Search.fromMap(queryParams);
    SearchResult<TUGrazPerson> tuGrazPeopleSearch = new SearchResult<>();

    // We first try to get local attributes. If a field does not exist or we do
    // not have permission to access it, this will throw. So we try again without
    // fetching locals.
    try {
      tuGrazPeopleSearch =
          tuGrazPersonRestService.search(
              s.getQuery(),
              s.getPagination().getPage(),
              s.getPagination().getPerPage(),
              String.join(",", List.of("email")));
    } catch (Exception e) {
      tuGrazPeopleSearch =
          tuGrazPersonRestService.search(
              s.getQuery(), s.getPagination().getPage(), s.getPagination().getPerPage(), null);
    }

    List<ContributorDO> contributors =
        tuGrazPeopleSearch.getItems().stream()
            .map(c -> TUGrazPersonDOMapper.mapEntityToDO(c, new ContributorDO()))
            .collect(Collectors.toList());

    return ResultList.fromItemsAndSearch(contributors, s);
  }
}
