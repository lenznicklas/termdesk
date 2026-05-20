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
      new AppMenuItem("nvim", "\uE6AE", List.of("nvim"), StartMode.TERM),
      new AppMenuItem("firefox", "\uE745", List.of("firefox"), StartMode.NWINDOW),
      new AppMenuItem(
          "tetris", "\uEC17", List.of("python3", "/home/lenz/Projects/tetris.py"), StartMode.TERM),
      new AppMenuItem("spotify", "\uF1BC", List.of("spotify_player"), StartMode.TERM),
      new AppMenuItem("network", "\uF1EB", List.of("nmtui"), StartMode.TERM),
      new AppMenuItem("Exit", "\uEA6E", List.of(""), StartMode.EXIT)
    };

    Terminal terminal = new DefaultTerminalFactory().createTerminal();
    Screen screen = new TerminalScreen(terminal);
    screen.startScreen();
    LocalTime time = LocalTime.now();

    boolean running = true;
    int selected = 0;
    long lastUpdate = 0;

    int yMatrix = 0;

    int width = screen.getTerminalSize().getColumns();

    while (running) {
      TextGraphics g = screen.newTextGraphics();
      long now = System.currentTimeMillis();

      if (now - lastUpdate >= 110) {
        yMatrix++;
        Matrix.drawMatrix(g, width - 39, 3, yMatrix);
        time = time.now();
        Draw.drawTime(g, time);
        Draw.drawBattery(g, screen);
        lastUpdate = now;
      }

      Draw.drawBoxToBottom(g, screen, width - 39, 3, 36);

      // draw TUI
      // screen.clear();
      screen.setCursorPosition(null);

      String title = "termDesk | lenz@arch";

      Draw.drawHeadline(g, screen, title);
      Draw.drawRow(options, g, selected, 4);
      Draw.drawBox(g, 3, 3, 20, screen.getTerminalSize().getRows() - 6, false);
      Draw.drawFooter(options, g, selected, screen);

      screen.refresh();

      // react to input
      KeyStroke key = screen.pollInput();
      if (key != null) {
        if (key.getKeyType() == KeyType.ArrowUp) {
          Draw.clearArea(g, 4, 3, 18, 20);
          selected--;

          if (selected < 0) {
            selected = options.length - 1;
          }
        } else if (key.getKeyType() == KeyType.ArrowDown) {

          selected++;

          if (selected >= options.length) {
            selected = 0;
          }
        } else if (key.getKeyType() == KeyType.Enter) {
          AppMenuItem item = options[selected];
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
