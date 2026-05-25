package at.lenz.termdesk;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Draw {
  private static final int COLUMN_WIDTH = 20;
  private static final int ROW_HEIGHT = 3;

  private Draw() {}

  public static void drawCol(Column col, TextGraphics g, int[] selected) {

    int x = 0;
    int y = 0;

    g.setForegroundColor(TextColor.ANSI.WHITE);

    for (AppMenuItem ami : col.apps()) {

      x = ami.column() * COLUMN_WIDTH + 3;
      y = ami.row() + ROW_HEIGHT;

      String s = ami.icon();
      int codePoint = Integer.parseInt(s.substring(1), 16);
      String symbol = new String(Character.toChars(codePoint));

      if (selected[0] == ami.column() && selected[1] == ami.row()) {
        g.enableModifiers(SGR.BOLD);
        g.putString(x, y, symbol + "  " + ami.label());
        g.disableModifiers(SGR.BOLD);
      } else {
        g.putString(x, y, ami.label());
      }
    }
  }
}
