package battleship;

import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;

public class BoardColor {

        private static final AnsiFormat WATER = new AnsiFormat(Attribute.BLUE_TEXT());
        private static final AnsiFormat SHIP = new AnsiFormat(Attribute.GREEN_TEXT());
        private static final AnsiFormat HIT = new AnsiFormat(Attribute.RED_TEXT());
        private static final AnsiFormat MISS = new AnsiFormat(Attribute.CYAN_TEXT());
        private static final AnsiFormat ADJ = new AnsiFormat(Attribute.YELLOW_TEXT());

        public static String water() {
                return WATER.format(".");
        }

        public static String ship() {
                return SHIP.format("#");
        }

        public static String hit() {
                return HIT.format("*");
        }

        public static String miss() {
                return MISS.format("o");
        }

        public static String adjacent() {
                return ADJ.format("-");
        }

        public static String colored(char marker) {
                return switch (marker) {
                        case '#' -> ship();
                        case '*' -> hit();
                        case 'o' -> miss();
                        case '-' -> adjacent();
                        case '.' -> water();
                        default -> String.valueOf(marker);
                };
        }
}
