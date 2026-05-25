package at.lenz.termdesk;

import java.util.List;

public record Column(List<AppMenuItem> apps, String element) {

  public Column {
    apps = apps == null ? List.of() : List.copyOf(apps);
  }

  public boolean isElementColumn() {
    return element != null;
  }

  public boolean isAppColumn() {
    return !apps.isEmpty();
  }
}
