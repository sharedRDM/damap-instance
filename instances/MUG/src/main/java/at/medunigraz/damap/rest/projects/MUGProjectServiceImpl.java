package at.medunigraz.damap.rest.projects;

import at.medunigraz.damap.rest.dmp.domain.MUGProject;
import at.medunigraz.damap.rest.dmp.mapper.MUGPersonDOMapper;
import at.medunigraz.damap.rest.dmp.mapper.MUGProjectDOMapper;
import at.medunigraz.damap.rest.persons.MUGPersonRestService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedMap;
import java.time.Duration;
import java.util.Date;
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
public class MUGProjectServiceImpl implements ProjectService {

  @Inject @RestClient MUGPersonRestService personRestService;

  @Inject @RestClient MUGProjectRestService projectRestService;

  @Override
  public ResultList<ProjectDO> search(MultivaluedMap<String, String> queryParams) {
    Search s = Search.fromMap(queryParams);
    List<ProjectDO> projects = List.of();
    int limit = s.getPagination().getPerPage();
    int offset = ((s.getPagination().getPage() < 1 ? 1 : s.getPagination().getPage()) - 1) * limit;

    try {
      var mugProjects =
          projectRestService.search(s.getQuery(), new Date().toInstant(), offset, limit);

      projects =
          mugProjects.getResults().stream()
              .map(p -> MUGProjectDOMapper.mapEntityToDO(p, new ProjectDO()))
              .collect(Collectors.toList());
    } catch (Exception e) {
      log.warn("Error during Project search: " + e);
    }

    return ResultList.fromItemsAndSearch(projects, s);
  }

  @Override
  public List<ContributorDO> getProjectStaff(String projectId) {
    MUGProject project = projectRestService.read(projectId, null);
    List<String> personIDs =
        project.getPersons().stream()
            .map(input -> input.split("-")[1])
            .collect(Collectors.toList());

    var unis =
        personIDs.stream()
            .map(
                personID ->
                    Uni.createFrom()
                        .item(() -> personRestService.read(personID, null))
                        .emitOn(Infrastructure.getDefaultExecutor()));

    var persons =
        Uni.join()
            .all(unis.collect(Collectors.toList()))
            .andCollectFailures()
            .await()
            .atMost(Duration.ofSeconds(10));

    return persons.stream()
        .map(mugPerson -> MUGPersonDOMapper.mapEntityToDO(mugPerson, new ContributorDO()))
        .collect(Collectors.toList());
  }

  @Override
  public ProjectDO read(String id, MultivaluedMap<String, String> queryParams) {
    return this.read(id, queryParams, null);
  }

  public ProjectDO read(
      String id, MultivaluedMap<String, String> queryParams, List<String> expand) {
    var project = projectRestService.read(id, expand);
    return MUGProjectDOMapper.mapEntityToDO(project, new ProjectDO());
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
    MUGProject project = projectRestService.read(projectId, null);
    if (project.getManager() == null) {
      return null;
    }

    return MUGPersonDOMapper.mapEntityToDO(
        personRestService.read(project.getManager(), null), new ContributorDO());
  }
}
