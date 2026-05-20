package at.lenz.termdesk;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.time.LocalTime;
import java.util.List;

public class Main {
  public static void main(String[] args) throws Exception {

    AppMenuItem[] options = {
      new AppMenuItem("nvim", "\uE6AE", List.of("nvim"), StartMode.TERM, 0),
      new AppMenuItem("firefox", "\uE745", List.of("firefox"), StartMode.NWINDOW, 0),
      new AppMenuItem(
          "tetris",
          "\uEC17",
          List.of("python3", "/home/lenz/Projects/tetris.py"),
          StartMode.TERM,
          0),
      new AppMenuItem("spotify", "\uF1BC", List.of("spotify_player"), StartMode.TERM, 0),
      new AppMenuItem("network", "\uF1EB", List.of("nmtui"), StartMode.TERM, 0),
      new AppMenuItem("Exit", "\uEA6E", List.of(""), StartMode.EXIT, 0),
      new AppMenuItem("hyprland", "\uF359", List.of("start-hyprland"), StartMode.WM, 1),
      new AppMenuItem("libreoffice", "L", List.of("libreoffice"), StartMode.NWINDOW, 1)
    };

    Terminal terminal = new DefaultTerminalFactory().createTerminal();
    Screen screen = new TerminalScreen(terminal);
    screen.startScreen();
    LocalTime time = LocalTime.now();

    boolean running = true;
    int[] selected = {0, 0};
    long lastUpdate = 0;

    int yMatrix = 0;

    int width = screen.getTerminalSize().getColumns();
    int cntRows = 1;

    String title = "termdesk | lenz@arch";
    // TODO: if terminalsize != terminalsize reload

    int[] rows = {0, 0};

    while (running) {

      TextGraphics g = screen.newTextGraphics();
      long now = System.currentTimeMillis();

      if (now - lastUpdate >= 110) {
        yMatrix++;
        Matrix.drawMatrix(g, width - 39, 3, yMatrix);
        time = time.now();
        Draw.drawHeadline(g, screen, time, title);
        lastUpdate = now;
      }

      screen.setCursorPosition(null);

      Draw.drawBox(g, width - 39, 3, 36, screen.getTerminalSize().getRows() - 6, false);
      int row1 = Draw.drawRow(options, g, selected, 4, 0);
      rows[0] = row1;
      Draw.drawBox(g, 3, 3, 20, screen.getTerminalSize().getRows() - 6, false);
      Draw.drawFooter(options, g, selected, screen);

      int row2 = Draw.drawRow(options, g, selected, 40, 1);
      rows[1] = row2;
      screen.refresh();

      // react to input
      KeyStroke key = screen.pollInput();
      if (key != null) {
        if (key.getKeyType() == KeyType.ArrowUp) {
          Draw.clearArea(g, 4, 3, 18, 20);
          selected[1]--;
          int len = rows[selected[0]];

          if (selected[1] < 0) {
            selected[1] = len - 1;
          }
        } else if (key.getKeyType() == KeyType.ArrowDown) {

          selected[1]++;
          int len = rows[selected[0]];

          if (selected[1] >= len) {
            selected[1] = 0;
          }
        } else if (key.getKeyType() == KeyType.Tab) {
          selected[0]++;
          selected[1] = 0;

          if (selected[0] > cntRows) {
            selected[0] = 0;
          }

        } else if (key.getKeyType() == KeyType.Enter) {
          AppMenuItem item = options[selected[1]];
          if (item.mode() == StartMode.EXIT) {
            return;
          }
          Run.runCommand(item);
          screen.clear();
        }
      }
      Thread.sleep(30);
    }
  }
}
