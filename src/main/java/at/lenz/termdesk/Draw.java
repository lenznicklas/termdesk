package at.lenz.termdesk;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class Draw {
  private static final int COLUMN_WIDTH = 20;
  private static final int ROW_HEIGHT = 3;

  private Draw() {}

  public static void drawCol(Column col, TextGraphics g, Screen screen, int[] selected) {

    int height = screen.getTerminalSize().getRows();
    int width = screen.getTerminalSize().getColumns();

    int x = 0;
    int y = 0;

    g.setForegroundColor(TextColor.ANSI.WHITE);

    for (AppMenuItem ami : col.apps()) {

      x = ami.column() * COLUMN_WIDTH + 3;
      y = ami.row() + ROW_HEIGHT;

      if (selected[0] == ami.column() && selected[1] == ami.row()) {

        String s = ami.icon();
        int codePoint = Integer.parseInt(s.substring(1), 16);
        String symbol = new String(Character.toChars(codePoint));

        g.enableModifiers(SGR.BOLD);
        g.putString(x, y, symbol + "  " + ami.label());
        g.putString(4, height - 2, symbol);
        g.disableModifiers(SGR.BOLD);

        g.putString(width - 4 - ami.label().length(), height - 2, ami.label());

      } else {
        g.putString(x, y, ami.label());
      }
    }
  }

  public static void drawBox(TextGraphics g, int x, int y, int width, int height) {

    g.setCharacter(x, y, Symbols.SINGLE_LINE_TOP_LEFT_CORNER);
    g.setCharacter(x + width, y, Symbols.SINGLE_LINE_TOP_RIGHT_CORNER);
  }

  public static void drawFooterOutline(TextGraphics g, Screen screen) {

    int screenWidth = screen.getTerminalSize().getColumns();
    int screenHeight = screen.getTerminalSize().getRows();

    for (int i = 0; i < screenWidth; i++) {
      g.setCharacter(i, screenHeight - 1, Symbols.SINGLE_LINE_HORIZONTAL);
      g.setCharacter(i, screenHeight - 3, Symbols.SINGLE_LINE_HORIZONTAL);
    }
  }
}
