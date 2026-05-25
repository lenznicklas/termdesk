package at.lenz.termdesk;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.util.List;

public class KeyAction {

  public static void keyAction(KeyStroke key, int[] selected, List<Integer> possible) {
    if (key != null) {
      // [0] col; [1] row
      if (key.getKeyType() == KeyType.ArrowDown) {
        selected[1]++;
        int size = possible.get(selected[0]);

        if (selected[1] >= size) {
          selected[1] = 0;
        }

      } else if (key.getKeyType() == KeyType.ArrowUp) {
        selected[1]--;
        int size = possible.get(selected[0]);

        if (selected[1] == -1) {
          selected[1] = size - 1;
        }

      } else if (key.getKeyType() == KeyType.Tab) {
        selected[0]++;
        selected[1] = 0;
        int size = possible.size();

        if (selected[0] == size) {
          selected[0] = 0;
        }

      } else if (key.getKeyType() == KeyType.ReverseTab) {
        selected[0]--;
        selected[1] = 0;
        int size = possible.size();

        if (selected[0] == -1) {
          selected[0] = size - 1;
        }

      } else if (key.getKeyType() == KeyType.Escape) {
        System.exit(0);
      }
    }
  }
}
