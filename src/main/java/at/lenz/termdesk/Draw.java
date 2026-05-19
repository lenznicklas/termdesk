package at.lenz.termdesk;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class Draw {

  public static void drawRow(AppMenuItem[] options, TextGraphics g, int selected, int x) {
    for (int i = 0; i < options.length; i++) {
      int y = i + 3;

      if (i == selected) {
        g.setForegroundColor(TextColor.ANSI.WHITE);
        g.enableModifiers(SGR.BOLD);
        g.putString(x, y, ">> " + options[i].icon() + "  " + options[i].label());
        g.disableModifiers(SGR.BOLD);
      } else {
        g.setForegroundColor(TextColor.ANSI.WHITE);
        g.putString(x, y, "   " + options[i].label());
      }
    }
  }

  public static void drawFooter(AppMenuItem[] options, TextGraphics g, int selected, Screen s) {
    int height = s.getTerminalSize().getRows();
    int width = s.getTerminalSize().getColumns();

    int y = height - 2;

    String text = String.join(" ", options[selected].command());

    int x = width - text.length() - 5;
    drawLine(g, s, y + 1);
    drawLine(g, s, y - 1);
    g.putString(x, y, text);

    x = 2;
    g.putString(x, y, options[selected].icon());
  }

  public static void drawHeadline(TextGraphics g, Screen s, String title) {
    int width = s.getTerminalSize().getColumns();
    int x = (width - title.length()) / 2;
    g.putString(x, 1, title);
  }

  public static void drawLine(TextGraphics g, Screen s, int y) {
    int width = s.getTerminalSize().getColumns();
    for (int i = 0; i < width; i++) {
      g.setCharacter(i, y, Symbols.SINGLE_LINE_HORIZONTAL);
    }
  }

  public static void drawBoxToBottom(TextGraphics g, Screen s, int x, int y, int width) {
    int height = s.getTerminalSize().getRows() - 3 - y;

    g.setCharacter(x, y, Symbols.SINGLE_LINE_TOP_LEFT_CORNER);
    g.setCharacter(x + width, y, Symbols.SINGLE_LINE_TOP_RIGHT_CORNER);
    for (int i = 1; i < width; i++) {
      g.setCharacter(x + i, y, Symbols.SINGLE_LINE_HORIZONTAL);
    }
    for (int i = 1; i < height; i++) {
      g.setCharacter(x, y + i, Symbols.SINGLE_LINE_VERTICAL);
      g.setCharacter(x + width, y + i, Symbols.SINGLE_LINE_VERTICAL);
    }
  }
}
