package at.tugraz.damap.integration.pure;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.jbosslog.JBossLog;
import org.damap.base.integration.pure.PureAPIInternalParticipantAssociation;
import org.damap.base.integration.pure.PureAPIProject;
import org.damap.base.integration.pure.PureProjectService;
import org.damap.base.rest.base.ResultList;
import org.damap.base.rest.base.Search;
import org.damap.base.rest.dmp.domain.ProjectDO;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * TUG-specific Pure Project Service that extends core PureProjectService with improved error
 * handling and null checks for getRecommended(). Fixes core bug where project.participants can be
 * null, and adds test user ID override support.
 */
@ApplicationScoped
@Alternative
@Priority(100)
@JBossLog
public class TUGrazPureProjectService extends PureProjectService {

  /**
   * Test/debug override: If set, use this UUID instead of the authenticated user ID. Useful for
   * testing with known Pure person UUIDs. Leave empty or unset to use the real authenticated user
   * ID.
   */
  @ConfigProperty(name = "damap.tugraz.pure.test-user-id")
  Optional<String> testUserIdOverride;

  /**
   * Dev-only override: return projects without filtering by user. This helps
   * validate the Pure connection when no matching internal participant is found.
   */
  @ConfigProperty(
      name = "damap.tugraz.pure.recommended-ignore-user-filter",
      defaultValue = "false")
  boolean ignoreUserFilter;

  @Override
  public ResultList<ProjectDO> getRecommended(Search search) {
    ResultList<ProjectDO> res = new ResultList<>();
    res.setSearch(search);

    final String userId;
    if (testUserIdOverride.isPresent() && !testUserIdOverride.get().isBlank()) {
      userId = testUserIdOverride.get();
    } else {
      userId = securityService.getUserId();
    }

    if (userId == null && !ignoreUserFilter) {
      res.setItems(new ArrayList<>());
      return res;
    }

    List<ProjectDO> matchingProjects = new ArrayList<>();
    long pageSize = 100;
    long offset = 0;
    int maxPagesToCheck = ignoreUserFilter ? 1 : 50;
    int maxResults = ignoreUserFilter ? (int) pageSize : 100;
    int pagesChecked = 0;
    boolean finished = false;

    try {
      while (!finished && pagesChecked < maxPagesToCheck && matchingProjects.size() < maxResults) {
        org.damap.base.integration.pure.PureAPIPaginatedProjectsResponse pageResponse =
            pureAPI.listAllProjects(pageSize, offset);

        if (pageResponse == null
            || pageResponse.getItems() == null
            || pageResponse.getPageInformation() == null) {
          break;
        }

        List<ProjectDO> pageMatches;
        if (ignoreUserFilter) {
          pageMatches =
              pageResponse.getItems().stream()
                  .map(project -> project.toProjectDO(descriptionClassification))
                  .toList();
        } else {
          pageMatches =
              pageResponse.getItems().stream()
                  .filter(project -> project.participants != null)
                  .filter(
                      project ->
                          project.participants.stream()
                              .filter(
                                  participant ->
                                      participant instanceof PureAPIInternalParticipantAssociation)
                              .anyMatch(
                                  participant -> {
                                    PureAPIInternalParticipantAssociation internal =
                                        (PureAPIInternalParticipantAssociation) participant;
                                    return internal.person != null
                                        && internal.person.uuid != null
                                        && internal.person.uuid.equals(userId);
                                  }))
                  .map(project -> project.toProjectDO(descriptionClassification))
                  .toList();
        }

        matchingProjects.addAll(pageMatches);

        long returnedOffset = pageResponse.getPageInformation().getOffset();
        long returnedSize = pageResponse.getPageInformation().getSize();
        long nextOffset = returnedOffset + Math.max(1, returnedSize);

        if (nextOffset <= offset) {
          break;
        }

        if (pageResponse.getCount() <= nextOffset) {
          finished = true;
        }

        offset = nextOffset;
        pagesChecked++;
      }

      res.setItems(matchingProjects);

    } catch (jakarta.ws.rs.ProcessingException e) {
      log.error("Error fetching projects from Pure API", e);
      res.setItems(new ArrayList<>());
    } catch (Exception e) {
      log.error("Error fetching projects from Pure API", e);
      res.setItems(new ArrayList<>());
    }

    return res;
  }
}
