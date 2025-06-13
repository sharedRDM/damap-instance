package at.tugraz.damap.conversion;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.jbosslog.JBossLog;
import org.damap.base.conversion.TemplateFileBrokerServiceImpl;

@ApplicationScoped
@Priority(1)
@JBossLog
public class TUGrazTemplateFileBrokerServiceImpl extends TemplateFileBrokerServiceImpl {
  // We do not override templates right now. Core templates have been adapted to
  // be general and not include university specifics.

  // In case templates have to be adapted, place template and resource files in
  // the `resources/at/tugraz/damap/template` folder. Then override the
  // appropriate methods here.
}
