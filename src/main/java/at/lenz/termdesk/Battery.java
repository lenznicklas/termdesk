package at.lenz.termdesk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Battery {

  public static String getBattery() {

    try {
      String capacity = Files.readString(Path.of("/sys/class/power_supply/BAT0/capacity")).trim();
      return capacity;
    } catch (IOException e) {
      System.err.println(e);
    }
    return null;
  }
}
