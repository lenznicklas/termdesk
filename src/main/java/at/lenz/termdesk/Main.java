package at.lenz.termdesk;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.nio.file.Path;

public class Main {
  public static void main(String[] args) throws Exception {

    Config config = ConfigUtil.load(Path.of("/home/lenz/.config/termdesk/config.tdc"));
    System.out.println(config);

    Terminal terminal = new DefaultTerminalFactory().createTerminal();
    Screen screen = new TerminalScreen(terminal);
    screen.startScreen();

    boolean running = true;

    int[] selected = {0, 0};

    while (running) {

      TextGraphics g = screen.newTextGraphics();
      screen.clear();
      screen.setCursorPosition(null);

      for (Column col : config.cols()) {

        if (col.isAppColumn()) {

          Draw.drawCol(col, g, selected);
        }
      }

      KeyStroke key = screen.pollInput();

      KeyAction.keyAction(key, selected);

      screen.refresh();
      Thread.sleep(30);
    }
  }
}
