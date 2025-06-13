package at.medunigraz.damap.rest.dmp.mapper;

import at.medunigraz.damap.rest.dmp.domain.MUGProject;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;
import org.damap.base.rest.dmp.domain.ProjectDO;

@Log
@UtilityClass
public class MUGProjectDOMapper {

  public ProjectDO mapEntityToDO(MUGProject project, ProjectDO projectDO) {
    projectDO.setAcronym(project.getAcronym());
    projectDO.setDescription(project.getDescription().getDe());
    projectDO.setEnd(project.getEndDate());
    projectDO.setStart(project.getStartDate());
    projectDO.setTitle(project.getName().getDe());
    projectDO.setUniversityId(project.getId());

    return projectDO;
  }
}
