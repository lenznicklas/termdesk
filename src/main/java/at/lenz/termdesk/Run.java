package at.lenz.termdesk;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Run {

    public static void runCommand(AppMenuItem mi) throws Exception {
        String command = mi.command();
        StartMode mode = mi.mode();

        ProcessBuilder pb = new ProcessBuilder(command);

        if (mode == StartMode.NWINDOW) {
            pb.start();
        } else if (mode == StartMode.TERM) {
            pb.inheritIO();
            pb.start().waitFor();
        } else if (mode == StartMode.EXIT) {
            return;
        }
    }
}
