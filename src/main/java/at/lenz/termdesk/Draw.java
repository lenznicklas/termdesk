package at.lenz.termdesk;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Draw {

  public static int drawRow(AppMenuItem[] options, TextGraphics g, int[] selected, int x, int row) {

    ArrayList<AppMenuItem> rowList = new ArrayList<>();
    for (int i = 0; i < options.length; i++) {
      if (options[i].row() == row) {
        rowList.add(options[i]);
      }
    }

    for (int i = 0; i < rowList.size(); i++) {
      if (rowList.get(i).row() == row) {
        int y = i + 4;
        // TODO: dynamisch machen
        clearArea(g, x, y, 18, 20);
        if (i == selected[1] && selected[0] == row) {
          g.setForegroundColor(TextColor.ANSI.WHITE);
          g.enableModifiers(SGR.BOLD);
          g.putString(x, y, ">> " + rowList.get(i).icon() + "  " + rowList.get(i).label());
          g.disableModifiers(SGR.BOLD);
        } else {
          g.setForegroundColor(TextColor.ANSI.WHITE);
          g.putString(x, y, "   " + rowList.get(i).label());
        }
      }
    }
    int cnt = rowList.size();

    return cnt;
  }

  public static void drawFooter(AppMenuItem[] options, TextGraphics g, int[] selected, Screen s) {
    int height = s.getTerminalSize().getRows();
    int width = s.getTerminalSize().getColumns();
    ArrayList<AppMenuItem> rowList = new ArrayList<>();
    for (int i = 0; i < options.length; i++) {
      if (options[i].row() == selected[0]) {
        rowList.add(options[i]);
      }
    }
    int y = height - 2;

    clearArea(g, 0, y, width, 1);

    String text = String.join(" ", rowList.get(selected[1]).command());

    int x = width - text.length() - 5;
    drawLine(g, s, y + 1);
    drawLine(g, s, y - 1);
    g.putString(x, y, text);

    x = 2;
    g.putString(x, y, rowList.get(selected[1]).icon());
  }

  public static void drawHeadline(TextGraphics g, Screen s, LocalTime t, String title) {
    g.setForegroundColor(TextColor.ANSI.WHITE);

    int width = s.getTerminalSize().getColumns();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    String time = "\uF017 " + t.format(formatter);
    String bat = "\uF240 " + Battery.getBattery();
    int[] x = {5, (width - title.length()) / 2, width - bat.length() - 4};
    int y = 1;

    g.putString(x[0], y, time);
    g.putString(x[1], y, title);
    g.putString(x[2], y, bat);

    drawBox(g, x[0] - 2, 0, time.length() + 3, 2, true);
    drawBox(g, x[1] - 2, 0, title.length() + 3, 2, true);
    drawBox(g, x[2] - 2, 0, bat.length() + 3, 2, true);
  }

  public static void drawLine(TextGraphics g, Screen s, int y) {
    int width = s.getTerminalSize().getColumns();
    for (int i = 0; i < width; i++) {
      g.setCharacter(i, y, Symbols.SINGLE_LINE_HORIZONTAL);
    }
  }

  public static void drawBox(
      TextGraphics g, int x, int y, int width, int height, boolean drawBottom) {
    g.setForegroundColor(TextColor.ANSI.WHITE);

    g.setCharacter(x, y, Symbols.SINGLE_LINE_TOP_LEFT_CORNER);
    g.setCharacter(x + width, y, Symbols.SINGLE_LINE_TOP_RIGHT_CORNER);
    if (drawBottom == true) {
      g.setCharacter(x, y + height, Symbols.SINGLE_LINE_BOTTOM_LEFT_CORNER);
      g.setCharacter(x + width, y + height, Symbols.SINGLE_LINE_BOTTOM_RIGHT_CORNER);
      for (int i = 1; i < width; i++) {
        g.setCharacter(x + i, y + height, Symbols.SINGLE_LINE_HORIZONTAL);
      }
    } else {
      g.setCharacter(x, y + height, Symbols.SINGLE_LINE_VERTICAL);
      g.setCharacter(x + width, y + height, Symbols.SINGLE_LINE_VERTICAL);
    }
    for (int i = 1; i < width; i++) {
      g.setCharacter(x + i, y, Symbols.SINGLE_LINE_HORIZONTAL);
    }
    for (int i = 1; i < height; i++) {
      g.setCharacter(x, y + i, Symbols.SINGLE_LINE_VERTICAL);
      g.setCharacter(x + width, y + i, Symbols.SINGLE_LINE_VERTICAL);
    }
  }

  public static void clearArea(TextGraphics g, int x, int y, int width, int height) {
    g.fillRectangle(new TerminalPosition(x, y), new TerminalSize(width, height), ' ');
  }
}
