package at.lenz.termdesk;

import java.util.List;

public record Config(List<String> header, List<Column> cols) {
  public Config {
    header = List.copyOf(header);
    cols = List.copyOf(cols);
  }
}
