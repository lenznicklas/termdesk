package at.lenz.termdesk;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.util.List;

public class Main {
  public static void main(String[] args) throws Exception {
    AppMenuItem[] options = {
      new AppMenuItem("nvim", List.of("nvim"), StartMode.TERM),
      new AppMenuItem("firefox", List.of("firefox"), StartMode.NWINDOW),
      new AppMenuItem(
          "tetris", List.of("python3", "/home/lenz/Projects/tetris.py"), StartMode.TERM),
      new AppMenuItem("Exit", List.of(""), StartMode.EXIT)
    };

    Terminal terminal = new DefaultTerminalFactory().createTerminal();
    Screen screen = new TerminalScreen(terminal);
    screen.startScreen();

    boolean running = true;
    int selected = 0;

    while (running) {

      screen.clear();
      screen.setCursorPosition(null);

      TextGraphics g = screen.newTextGraphics();

      String title = "termDesk";

      int width = screen.getTerminalSize().getColumns();
      int x = (width - title.length()) / 2;

      g.putString(width - 5, 0, Headline.getBattery() + "%");
      g.putString(x, 0, title);

      for (int i = 0; i < options.length; i++) {
        int y = i + 3;

        if (i == selected) {
          g.setForegroundColor(TextColor.ANSI.WHITE);
          g.enableModifiers(SGR.BOLD);
          g.putString(2, y, ">> " + options[i].label());
          g.disableModifiers(SGR.BOLD);
        } else {
          g.setForegroundColor(TextColor.ANSI.WHITE);
          g.putString(2, y, "   " + options[i].label());
        }
      }

      screen.refresh();

      KeyStroke key = screen.readInput();

      if (key.getKeyType() == KeyType.ArrowUp) {
        selected--;

        if (selected < 0) {
          selected = options.length - 1;
        }
      } else if (key.getKeyType() == KeyType.ArrowDown) {
        selected++;

        if (selected > options.length) {
          selected = 0;
        }
      } else if (key.getKeyType() == KeyType.Enter) {
        AppMenuItem item = options[selected];

        Run.runCommand(item);
      }
    }
  }
}
