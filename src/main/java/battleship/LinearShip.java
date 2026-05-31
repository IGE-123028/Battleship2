package battleship;

/**
 * Represents a ship that occupies positions in a straight line (horizontal or vertical).
 */
public abstract class LinearShip extends Ship {

    /**
     * Instantiates a new Linear ship.
     *
     * @param category the category
     * @param bearing  the bearing
     * @param pos      the pos
     * @param size     the size
     */
    public LinearShip(String category, Compass bearing, IPosition pos, int size) {
        super(category, bearing, pos, size);
        addPositionsByBearing(bearing, pos);
    }

    /**
     * Adds positions to the ship based on its bearing and size, in a straight line.
     *
     * @param bearing the bearing
     * @param pos     the initial position
     */
    protected void addPositionsByBearing(Compass bearing, IPosition pos) {
        switch (bearing) {
            case NORTH:
            case SOUTH:
                for (int r = 0; r < this.getSize(); r++) {
                    getPositions().add(new Position(pos.getRow() + r, pos.getColumn()));
                }
                break;
            case EAST:
            case WEST:
                for (int c = 0; c < this.getSize(); c++) {
                    getPositions().add(new Position(pos.getRow(), pos.getColumn() + c));
                }
                break;
        }
    }
}
