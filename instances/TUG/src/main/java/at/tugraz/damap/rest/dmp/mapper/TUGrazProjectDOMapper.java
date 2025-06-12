package at.tugraz.damap.rest.dmp.mapper;

import at.tugraz.damap.rest.dmp.domain.TUGrazProject;
import lombok.experimental.UtilityClass;
import org.damap.base.rest.dmp.domain.ProjectDO;

@UtilityClass
public class TUGrazProjectDOMapper {

  public ProjectDO mapEntityToDO(TUGrazProject project, ProjectDO projectDO) {
    projectDO.setUniversityId(project.getIdentifier());
    projectDO.setTitle(project.getName());
    projectDO.setDescription(project.getDescription());
    projectDO.setStart(project.getStartDate());
    projectDO.setEnd(project.getEndDate());

    return projectDO;
  }
}
