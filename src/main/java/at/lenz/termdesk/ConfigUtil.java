package at.lenz.termdesk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ConfigUtil {

  private ConfigUtil() {}

  // Lädt die Config-Datei
  public static Config load(Path path) throws IOException {
    String content = Files.readString(path);
    return parseConfig(content);
  }

  // Parst die komplette Config
  public static Config parseConfig(String content) {
    List<String> header = parseHeader(getSection(content, "header"));
    List<Column> cols = parseCols(getSection(content, "cols"));

    return new Config(header, cols);
  }

  // Holt eine Section wie [header] oder [cols]
  public static String getSection(String text, String title) {
    StringBuilder result = new StringBuilder();
    boolean insideSection = false;

    for (String line : text.split("\\R")) {
      String trimmed = line.trim();

      if (trimmed.equals("[" + title + "]")) {
        insideSection = true;
        continue;
      }

      if (insideSection && trimmed.startsWith("[") && trimmed.endsWith("]")) {
        break;
      }

      if (insideSection) {
        result.append(line).append("\n");
      }
    }

    return result.toString();
  }

  // Parst [header]
  public static List<String> parseHeader(String section) {
    List<String> header = new ArrayList<>();

    for (String line : section.split("\\R")) {
      String trimmed = line.trim();

      if (isEmptyOrComment(trimmed)) {
        continue;
      }

      header.add(trimmed);
    }

    return List.copyOf(header);
  }

  // Parst [cols]
  //
  // Beispiel:
  //
  // [cols]
  // col
  // app: firefox | F | firefox | | TERM
  // app: libre | L | libreoffice | | TERM
  //
  // col
  // app: hyprland | H | Hyprland | | TERM
  //
  // Ergebnis:
  // firefox  -> row 0, column 0
  // libre    -> row 1, column 0
  // hyprland -> row 0, column 1
  public static List<Column> parseCols(String section) {
    List<Column> cols = new ArrayList<>();

    boolean insideCol = false;

    List<AppMenuItem> currentApps = new ArrayList<>();
    String currentElement = null;

    int columnIndex = -1;
    int rowIndex = 0;

    for (String line : section.split("\\R")) {
      String trimmed = line.trim();

      if (isEmptyOrComment(trimmed)) {
        continue;
      }

      // Neue Column beginnt
      if (trimmed.equals("col")) {
        if (insideCol) {
          addColumnIfNotEmpty(cols, currentApps, currentElement, columnIndex);
        }

        insideCol = true;

        columnIndex++;
        rowIndex = 0;

        currentApps = new ArrayList<>();
        currentElement = null;

        continue;
      }

      if (!insideCol) {
        throw new IllegalArgumentException("Zeile außerhalb von col gefunden: " + trimmed);
      }

      // App-Zeile
      if (trimmed.startsWith("app:")) {
        if (currentElement != null) {
          throw new IllegalArgumentException(
              "Eine col darf nicht gleichzeitig app und element enthalten: " + trimmed);
        }

        AppMenuItem item = parseAppMenuItem(trimmed, rowIndex, columnIndex);
        currentApps.add(item);

        rowIndex++;
        continue;
      }

      // Optional: element-Zeile
      if (trimmed.startsWith("element:")) {
        if (!currentApps.isEmpty()) {
          throw new IllegalArgumentException(
              "Eine col darf nicht gleichzeitig app und element enthalten: " + trimmed);
        }

        if (currentElement != null) {
          throw new IllegalArgumentException("Eine col darf nur ein element enthalten: " + trimmed);
        }

        currentElement = parseElement(trimmed);
        rowIndex++;
        continue;
      }

      throw new IllegalArgumentException("Unbekannte Zeile in [cols]: " + trimmed);
    }

    // Letzte Column speichern
    if (insideCol) {
      addColumnIfNotEmpty(cols, currentApps, currentElement, columnIndex);
    }

    return List.copyOf(cols);
  }

  // Parst:
  //
  // app: label | icon | command | exitCommand | mode
  //
  // Beispiel:
  // app: firefox | F | firefox | | TERM
  private static AppMenuItem parseAppMenuItem(String line, int row, int column) {
    String value = line.substring("app:".length()).trim();

    List<String> parts = splitByPipe(value);

    if (parts.size() != 5) {
      throw new IllegalArgumentException(
          "App muss so aussehen: app: label | icon | command | exitCommand | mode -> " + line);
    }

    String label = parts.get(0).trim();
    String icon = parts.get(1).trim();
    List<String> command = parseCommand(parts.get(2).trim());
    List<String> exitCommand = parseCommand(parts.get(3).trim());
    StartMode mode = parseStartMode(parts.get(4).trim());

    if (label.isEmpty()) {
      throw new IllegalArgumentException("label darf nicht leer sein: " + line);
    }

    if (icon.isEmpty()) {
      throw new IllegalArgumentException("icon darf nicht leer sein: " + line);
    }

    if (command.isEmpty()) {
      throw new IllegalArgumentException("command darf nicht leer sein: " + line);
    }

    return new AppMenuItem(label, icon, command, exitCommand, mode, row, column);
  }

  private static String parseElement(String line) {
    String element = line.substring("element:".length()).trim();

    if (element.isEmpty()) {
      throw new IllegalArgumentException("element darf nicht leer sein: " + line);
    }

    return element;
  }

  private static void addColumnIfNotEmpty(
      List<Column> cols, List<AppMenuItem> apps, String element, int columnIndex) {
    if (!apps.isEmpty() || element != null) {
      cols.add(new Column(apps, element));
    }
  }

  // Trennt an |, aber nicht innerhalb von Anführungszeichen
  private static List<String> splitByPipe(String value) {
    List<String> result = new ArrayList<>();
    StringBuilder current = new StringBuilder();

    boolean insideQuotes = false;
    char quoteChar = 0;
    boolean escaping = false;

    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);

      if (escaping) {
        current.append(c);
        escaping = false;
        continue;
      }

      if (c == '\\') {
        escaping = true;
        continue;
      }

      if (insideQuotes) {
        if (c == quoteChar) {
          insideQuotes = false;
        } else {
          current.append(c);
        }

        continue;
      }

      if (c == '"' || c == '\'') {
        insideQuotes = true;
        quoteChar = c;
        continue;
      }

      if (c == '|') {
        result.add(current.toString().trim());
        current.setLength(0);
        continue;
      }

      current.append(c);
    }

    if (insideQuotes) {
      throw new IllegalArgumentException("Nicht geschlossenes Anführungszeichen: " + value);
    }

    result.add(current.toString().trim());

    return result;
  }

  // Macht aus einem Command-String eine List<String>
  //
  // "" oder "-" bedeutet: kein Command
  private static List<String> parseCommand(String value) {
    value = value.trim();

    if (value.isEmpty() || value.equals("-")) {
      return List.of();
    }

    List<String> result = new ArrayList<>();
    StringBuilder current = new StringBuilder();

    boolean insideQuotes = false;
    char quoteChar = 0;
    boolean escaping = false;

    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);

      if (escaping) {
        current.append(c);
        escaping = false;
        continue;
      }

      if (c == '\\') {
        escaping = true;
        continue;
      }

      if (insideQuotes) {
        if (c == quoteChar) {
          insideQuotes = false;
        } else {
          current.append(c);
        }

        continue;
      }

      if (c == '"' || c == '\'') {
        insideQuotes = true;
        quoteChar = c;
        continue;
      }

      if (Character.isWhitespace(c)) {
        if (current.length() > 0) {
          result.add(current.toString());
          current.setLength(0);
        }

        continue;
      }

      current.append(c);
    }

    if (insideQuotes) {
      throw new IllegalArgumentException(
          "Nicht geschlossenes Anführungszeichen im command: " + value);
    }

    if (current.length() > 0) {
      result.add(current.toString());
    }

    return List.copyOf(result);
  }

  private static StartMode parseStartMode(String value) {
    if (value.isEmpty()) {
      throw new IllegalArgumentException("StartMode darf nicht leer sein");
    }

    for (StartMode mode : StartMode.values()) {
      if (mode.name().equalsIgnoreCase(value)) {
        return mode;
      }
    }

    throw new IllegalArgumentException("Unbekannter StartMode: " + value);
  }

  private static boolean isEmptyOrComment(String line) {
    return line.isEmpty() || line.startsWith("#");
  }
}
