package at.lenz.termdesk;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Matrix {

  public static void drawMatrix(TextGraphics g, int x, int y, int index) {

    String[] listStr = {
      "Y + 7   ( k b 4   L ß 8 e z I m ~ -",
      "i w J = 7 M C u   T 3 I z u | # % X",
      "- K   / 4 P ) 6 R Z < R   n I   s Y",
      "p   g 2   o j W   ) |   i Z / C k u",
      "z c b P P 5 Z - % < ß o m e a m f  ",
      "4 G µ E 7 ! H   = ) ß ;   i P ! r J",
      "λ N e : M Y W { Y E & - d   B % [ Q",
      "  5 λ ) j R O *     V x w µ ; E x 8",
      "0 V ß j Y E * w   ^ - 6 ß H m   v F",
      "        N X ^ 4 b k R & m 1 *   λ €",
      "6 ! u f 4 x   M H 6 s V > λ 1 W G ]",
      "  λ Q k ] f §   > 8   # X U 9 V J W",
      "A E d ; Ω 0 ß ] € c l & U S B   X 5",
      "    y B !   z   t - > n W x λ : € €",
      "u # ; n p   t E s J [ I   7 c b / R",
      "k ]   t D ) o v S % c H   Z q J n  ",
      "T c E k z   M w q ß h 8 J T   O   *",
      "w 7   k - U   : λ F Ω s ~     ^ F a",
      "9   2 * @ M 0 ~ = U g < P = Z H Ω λ",
      "  q K   ) f > € I l ( X }       J ?",
      "! ß ? H Q O   O q λ 4 Ω p X 2 g < d",
      "9 + 6 t 1 r - C X N } { W q *   € b",
      "λ   W j / | k I k [ Ω   K C h #   7",
      "0 j 0 ; ! § W ß f u % M   o v ( λ p",
      "= P ) > ~ < 8 n P 8   ß c 6 i v 7 &",
      "w T u % # % ) * µ       p H   H j g",
      "g 4   R F ; 0 § R F ) € n Z l d 0 #",
      "? 6 o m V j ? h Z / 1   5   ? z j  ",
      ") Z # [ c I @ Ω Y U G F p ß   ] c X",
      "- O C t & | 5 6 5 M   € u X N j λ N",
      "F   Ω 0 y I µ s 1 M   λ 1 e E % % 3",
      "+ § k N d ) B f L 5   >   c ^ L s }",
      "% ? 9   K S A h c ß O * A ? 2 d C §",
      "? µ o y ) 7   L J   f C * O y 4 M g",
      "P f m g € I R > G U r @ j 5   q B c",
      "k * 3 t # _ f ^ z ß W 3 v J V   € -",
      "-   i Y J Ω Ω >     & ^ 3 ß ß X y  ",
      "Y A { j f 1 5 L 0 < % Ω < k 8 +   €",
      "G ! n h _ q   Ω ^ V O   G E ; > N z",
      "  8   d ]   h Z L / E q M d u ) W x",
      "€ i ß e   m c s 8 s ß z O r 6 d 0 U",
      "  O k e f S § i 1 l C & N W [ ] p ^",
      "z J d X : ß | e   X P w § s   } : r",
      "~ λ ! H 7 @ µ 3 r w z S M Y l p   y",
      "W { n ß a e i 5 z 0 4 C g 3 b   W I",
      "  2 < ^ = h v e z r l S z G & # / q",
      "E v - s e K c ] t Y q d a f h   S y",
      "S w   c f     T 7 2   C 9 k 3 € I C",
      "U ~ 4   7 Q ~ ß 4 T 0 Y 1 9 t 2 B W",
      "S K P H ß | O O ? <   A 7 R Q [ - :",
      "E 2 3 ; : 2 + @ D L [ Y u µ h     p",
      "w e L λ § S I { h ~   f f ] R w < a",
      "4 K E b < X P   r q g B ß L = # µ x",
      "f e     N p ß o H D 2 F K 1 1 G C q",
      "  a S ( G Ω ß e 7 n C O X & 5 i >  ",
      "6 2 d Ω ( < C q n R j i 8 n q 4 +  ",
      "> ß Y u d J µ e ; x / < 2 I d 8   i",
      "b   8 ; x & W d E t s + n > { k o G",
      "  8   | 6 q 1 c F €   @ 3   *   > 0",
      "  v ; € 1 w   ( # µ m g * S   D f §",
      "H   E f   ß   J E   K + m ^ i 6 k >",
      "]   z - m 0 { / Ω   | l J     S /  ",
      "x e F ( ; D ? 0 9 u   3 ( %   w > -",
      "#   f P t G K d j   w ?   w   T   E",
      "W ] l k R ! & r j l   f u o Z C ß ^",
      "1 L ^ (   ~ G N N O C ? 0 A I n x l",
      "* ~ k   X & 9   § g A D U a § } d 2",
      "( W k     ? w 2 v Q 0 :   d M 8 M #",
      "O i k z ß Z H W [ v R P 5 k # D   b",
      "< [ s 6 x Z   Q g   5 z /   ) ß 3  "
    };
    int start = index % listStr.length;

    for (int row = 0; row < listStr.length; row++) {
      int arrayIndex = (start - row + listStr.length) % listStr.length;
      g.setForegroundColor(TextColor.ANSI.GREEN);
      g.putString(x, y + row, listStr[arrayIndex]);
    }
  }
}
