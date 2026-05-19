package at.lenz.termdesk;

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

    boolean running = true;
    int selected = 0;

    while (running) {

      screen.clear();
      screen.setCursorPosition(null);

      TextGraphics g = screen.newTextGraphics();
      // makeTitle(txtgraph, title, screen
      String title = "termDesk | lenz@arch";

      Draw.drawHeadline(g, screen, title);
      /*
      int width = screen.getTerminalSize().getColumns();
      int x = (width - title.length()) / 2;

      g.putString(width - 5, 0, Headline.getBattery() + "%");
      g.putString(x, 0, title);*/

      Draw.drawRow(options, g, selected, 4);

      screen.refresh();

      KeyStroke key = screen.readInput();

      if (key.getKeyType() == KeyType.ArrowUp) {
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
      }
    }
  }
}
