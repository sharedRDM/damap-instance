package at.tugraz.damap.rest.dmp.mapper;

import at.tugraz.damap.rest.dmp.domain.TUGrazPerson;
import lombok.experimental.UtilityClass;
import org.damap.base.rest.dmp.domain.ContributorDO;

@UtilityClass
public class TUGrazPersonDOMapper {

  public ContributorDO mapEntityToDO(TUGrazPerson tuGrazPerson, ContributorDO contributorDO) {
    contributorDO.setFirstName(tuGrazPerson.getGivenName());
    contributorDO.setLastName(tuGrazPerson.getFamilyName());
    contributorDO.setUniversityId(tuGrazPerson.getIdentifier());
    contributorDO.setMbox(tuGrazPerson.getEmail());

    return contributorDO;
  }
}
