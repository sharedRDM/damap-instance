package at.medunigraz.damap.rest.dmp.mapper;

import at.medunigraz.damap.rest.dmp.domain.MUGPerson;
import lombok.experimental.UtilityClass;
import org.damap.base.rest.dmp.domain.ContributorDO;

@UtilityClass
public class MUGPersonDOMapper {

  public ContributorDO mapEntityToDO(MUGPerson mugPerson, ContributorDO contributorDO) {
    contributorDO.setFirstName(mugPerson.getFirstName());
    contributorDO.setLastName(mugPerson.getLastName());
    contributorDO.setUniversityId(mugPerson.getId());
    contributorDO.setMbox(mugPerson.getEmail());

    return contributorDO;
  }
}
