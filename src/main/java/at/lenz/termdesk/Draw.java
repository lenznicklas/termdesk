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

public class Draw {

  public static void drawRow(AppMenuItem[] options, TextGraphics g, int selected, int x) {
    for (int i = 0; i < options.length; i++) {
      int y = i + 4;
      // TODO: dynamisch machen
      clearArea(g, x, y, 18, 20);
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

    clearArea(g, 0, y, width, 1);

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
    drawBox(g, x - 1, 0, title.length() + 1, 2);
  }

  public static void drawTime(TextGraphics g, LocalTime t) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    g.setForegroundColor(TextColor.ANSI.WHITE);
    String ts = "\uF017 " + t.format(formatter);
    int x = 5;
    g.putString(x, 1, ts);
    drawBox(g, x - 2, 0, ts.length() + 3, 2);
  }

  public static void drawBattery(TextGraphics g, Screen s) {
    String bat = "\uF240 " + Headline.getBattery();
    g.setForegroundColor(TextColor.ANSI.WHITE);
    int x = s.getTerminalSize().getColumns() - bat.length() - 4;
    g.putString(x, 1, bat);
    drawBox(g, x - 2, 0, bat.length() + 3, 2);
  }

  public static void drawLine(TextGraphics g, Screen s, int y) {
    int width = s.getTerminalSize().getColumns();
    for (int i = 0; i < width; i++) {
      g.setCharacter(i, y, Symbols.SINGLE_LINE_HORIZONTAL);
    }
  }

  public static void drawBoxToBottom(TextGraphics g, Screen s, int x, int y, int width) {
    g.setForegroundColor(TextColor.ANSI.WHITE);
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

  public static void drawBox(TextGraphics g, int x, int y, int width, int heigth) {
    g.setForegroundColor(TextColor.ANSI.WHITE);

    g.setCharacter(x, y, Symbols.SINGLE_LINE_TOP_LEFT_CORNER);
    g.setCharacter(x + width, y, Symbols.SINGLE_LINE_TOP_RIGHT_CORNER);
    g.setCharacter(x, y + heigth, Symbols.SINGLE_LINE_BOTTOM_LEFT_CORNER);
    g.setCharacter(x + width, y + heigth, Symbols.SINGLE_LINE_BOTTOM_RIGHT_CORNER);
    for (int i = 1; i < width; i++) {
      g.setCharacter(x + i, y, Symbols.SINGLE_LINE_HORIZONTAL);
      g.setCharacter(x + i, y + heigth, Symbols.SINGLE_LINE_HORIZONTAL);
    }
    for (int i = 1; i < heigth; i++) {
      g.setCharacter(x, y + i, Symbols.SINGLE_LINE_VERTICAL);
      g.setCharacter(x + width, y + i, Symbols.SINGLE_LINE_VERTICAL);
    }
  }

  public static void clearArea(TextGraphics g, int x, int y, int width, int height) {
    g.fillRectangle(new TerminalPosition(x, y), new TerminalSize(width, height), ' ');
  }
}
