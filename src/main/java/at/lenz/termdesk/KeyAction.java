package at.lenz.termdesk;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class KeyAction {

  public static void keyAction(KeyStroke key, int[] selected) {
    if (key != null) {
      // [0] col; [1] row
      if (key.getKeyType() == KeyType.ArrowDown) {
        selected[1]++;
      } else if (key.getKeyType() == KeyType.ArrowUp) {
        selected[1]--;
      } else if (key.getKeyType() == KeyType.Escape) {
        System.exit(0);
      }
    }
  }
}
