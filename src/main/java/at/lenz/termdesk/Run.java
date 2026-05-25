package at.lenz.termdesk;

import java.util.List;

public class Run {

  public static void runCommand(AppMenuItem mi) throws Exception {
    List<String> command = mi.command();
    List<String> exitCommand = mi.exit();
    StartMode mode = mi.mode();

    ProcessBuilder pb = new ProcessBuilder(command);

    if (mode == StartMode.NWINDOW) {
      pb.start();
    } else if (mode == StartMode.TERM) {
      pb.inheritIO();
      pb.start().waitFor();
    } else if (mode == StartMode.WM) {
      ProcessBuilder pbExit = new ProcessBuilder(List.of("hyprshutdown"));
      pbExit.start();
      // Process shutdown = pbExit.start();
      // shutdown.waitFor();
      // ProcessBuilder pbs = new ProcessBuilder(List.of("sh", "-c", "sway"));
      // pbs.start();
      return;
    } else if (mode == StartMode.EXIT) {
      return;
    }
  }
}
