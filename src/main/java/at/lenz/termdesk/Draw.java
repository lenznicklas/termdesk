package at.lenz.termdesk;

import com.googlecode.lanterna.SGR;
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
        g.putString(x, y, ">> " + options[i].icon() + " " + options[i].label());
        g.disableModifiers(SGR.BOLD);
      } else {
        g.setForegroundColor(TextColor.ANSI.WHITE);
        g.putString(x, y, "   " + options[i].label());
      }
    }
  }

  public static void drawHeadline(TextGraphics g, Screen s, String title) {
    int width = s.getTerminalSize().getColumns();
    int x = (width - title.length()) / 2;
    g.putString(x, 1, title);
  }
}
