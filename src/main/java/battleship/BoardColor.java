package battleship;

import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;

/**
 * Utility class for providing ANSI colored representations of board elements.
 */
public class BoardColor {

        private static final AnsiFormat WATER = new AnsiFormat(Attribute.BLUE_TEXT());
        private static final AnsiFormat SHIP = new AnsiFormat(Attribute.GREEN_TEXT());
        private static final AnsiFormat HIT = new AnsiFormat(Attribute.RED_TEXT());
        private static final AnsiFormat MISS = new AnsiFormat(Attribute.CYAN_TEXT());
        private static final AnsiFormat ADJ = new AnsiFormat(Attribute.YELLOW_TEXT());

        private BoardColor() {
                // Utility class
        }

        /**
         * Returns a colored string representation of water.
         *
         * @return colored water marker
         */
        public static String water() {
                return WATER.format(".");
        }

        /**
         * Returns a colored string representation of a ship.
         *
         * @return colored ship marker
         */
        public static String ship() {
                return SHIP.format("#");
        }

        /**
         * Returns a colored string representation of a hit.
         *
         * @return colored hit marker
         */
        public static String hit() {
                return HIT.format("*");
        }

        /**
         * Returns a colored string representation of a miss.
         *
         * @return colored miss marker
         */
        public static String miss() {
                return MISS.format("o");
        }

        /**
         * Returns a colored string representation of a position adjacent to a ship.
         *
         * @return colored adjacent marker
         */
        public static String adjacent() {
                return ADJ.format("-");
        }

}
