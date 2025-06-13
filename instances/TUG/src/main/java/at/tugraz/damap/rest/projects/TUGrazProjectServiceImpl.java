package at.tugraz.damap.rest.projects;

import at.tugraz.damap.rest.dmp.mapper.TUGrazProjectDOMapper;
import at.tugraz.damap.rest.persons.TUGrazPersonRestService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.jbosslog.JBossLog;
import org.damap.base.rest.base.ResultList;
import org.damap.base.rest.base.Search;
import org.damap.base.rest.dmp.domain.ContributorDO;
import org.damap.base.rest.dmp.domain.ProjectDO;
import org.damap.base.rest.projects.ProjectService;
import org.damap.base.rest.projects.ProjectSupplementDO;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@JBossLog
@ApplicationScoped
public class TUGrazProjectServiceImpl implements ProjectService {

  @Inject @RestClient TUGrazPersonRestService tuGrazPersonRestService;

  @Inject @RestClient TUGrazProjectRestService tuGrazProjectRestService;

  @Override
  public ResultList<ProjectDO> search(MultivaluedMap<String, String> queryParams) {
    Search s = Search.fromMap(queryParams);
    List<ProjectDO> projects = List.of();
    try {
      var tuGrazProjectSearch =
          tuGrazProjectRestService.search(
              s.getQuery(), s.getPagination().getPage(), s.getPagination().getPerPage(), null);

      projects =
          tuGrazProjectSearch.getItems().stream()
              .map(p -> TUGrazProjectDOMapper.mapEntityToDO(p, new ProjectDO()))
              .collect(Collectors.toList());

    } catch (Exception e) {
      log.warn("Error during Project search: " + e);
    }

    return ResultList.fromItemsAndSearch(projects, s);
  }

  @Override
  public List<ContributorDO> getProjectStaff(String projectId) {
    // Info not available within current project API.
    return List.of();
  }

  @Override
  public ProjectDO read(String id, MultivaluedMap<String, String> queryParams) {
    var project = tuGrazProjectRestService.read(id, null);
    return TUGrazProjectDOMapper.mapEntityToDO(project, new ProjectDO());
  }

  @Override
  public ResultList<ProjectDO> getRecommended(MultivaluedMap<String, String> queryParams) {
    return this.search(queryParams);
  }

  @Override
  public ProjectSupplementDO getProjectSupplement(String projectId) {
    return null;
  }

  @Override
  public ContributorDO getProjectLeader(String projectId) {
    return null;
  }
}
