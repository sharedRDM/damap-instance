package at.tugraz.damap.integration.pure;

import org.damap.base.integration.pure.PureAPIPerson;
import org.damap.base.rest.dmp.domain.ContributorDO;

/** Utility for converting PureAPIPerson to ContributorDO, handling null UUIDs. */
public final class PureAPIPersonConverter {
  private PureAPIPersonConverter() {}

  public static ContributorDO toContributor(PureAPIPerson person) {
    if (person == null || person.getName() == null || person.getName().getFirstName() == null) {
      return null;
    }

    try {
      ContributorDO c = person.toContributor();
      if (c == null || c.getFirstName() == null || c.getLastName() == null) {
        return null;
      }

      if (person.getUuid() == null
          && c.getPersonId() != null
          && c.getPersonId().getIdentifier() == null) {
        c.getPersonId().setIdentifier(c.getFirstName() + "_" + c.getLastName());
      }

      return c;
    } catch (Exception e) {
      return null;
    }
  }
}
