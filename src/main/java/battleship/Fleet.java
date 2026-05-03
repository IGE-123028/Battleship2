/**
 * 
 */
package battleship;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Fleet.
 */
public class Fleet implements IFleet
{
	/**
	 * Creates a randomly generated fleet containing ships of various predefined types.
	 * Each ship is assigned a random bearing and position. If a ship cannot be added
	 * due to constraints (e.g., collision or boundary issues), it will be retried.
	 *
	 * @return a fully constructed and valid fleet as an instance of IFleet
	 */
	public static IFleet createRandom() {

		Fleet randomFleet = new Fleet();

		// Define the types of ships to be added
		ShipType[] shipTypes =
					{ShipType.GALEAO,                           // 1 galleon
				 	ShipType.FRAGATA,                           // 1 frigate
 				 	ShipType.NAU, ShipType.NAU,                 // 2 carracks
					ShipType.CARAVELA, ShipType.CARAVELA, ShipType.CARAVELA,  // 3 caravels
					ShipType.BARCA, ShipType.BARCA, ShipType.BARCA, ShipType.BARCA}; // 4 barges

		int fleetSize = 0;

		while (fleetSize < shipTypes.length) {

			// Build the ship
			Ship ship = Ship.buildShip(shipTypes[fleetSize], Compass.randomBearing(), Position.randomPosition());

			// Attempt to add the ship to the fleet
			if (ship != null && randomFleet.addShip(ship)) {
				fleetSize++; // Increment count if ship is successfully added
			}
		}
		return randomFleet;
	}


    // -----------------------------------------------------

	/**
	 * The Ships.
	 */
	private final List<IShip> ships;

	// -----------------------------------------------------ge
	/**
	 * Instantiates a new Fleet.
	 */
	public Fleet()
    {
	ships = new ArrayList<>();
    }

	/**
	 * Gets ships.
	 *
	 * @return the ships
	 */
	@Override
    public List<IShip> getShips()
    {
	return ships;
    }
	
	/**
	 * Add ship boolean.
	 *
	 * @param s the s
	 * @return the boolean
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IFleet#addShip(battleship.IShip)
     */
    @Override
    public boolean addShip(IShip s)
    {
		assert s != null;

		boolean result = false;
        if ((ships.size() < FLEET_SIZE) && (isInsideBoard(s)) && (!colisionRisk(s)))
		{
			ships.add(s);
			result = true;
		}
		return result;
    }

	/**
	 * Gets ships like.
	 *
	 * @param category the category
	 * @return the ships like
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IFleet#getShipsLike(java.lang.String)
     */
    @Override
    public List<IShip> getShipsLike(String category)
    {
		assert category != null;

		List<IShip> shipsLike = new ArrayList<>();
		for (IShip s : ships)
			if (s.getCategory().equals(category))
				shipsLike.add(s);

		return shipsLike;
    }

	/**
	 * Gets floating ships.
	 *
	 * @return the floating ships
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IFleet#getFloatingShips()
     */
    @Override
    public List<IShip> getFloatingShips() {
        return filterShips(true);
    }


    private List<IShip> filterShips(boolean floating) {
        List<IShip> result = new ArrayList<>();
        for (IShip s : ships) {
            if (s.stillFloating() == floating) {
                result.add(s);
            }
        }
        return result;
    }

	/**
	 * Gets sunk ships.
	 *
	 * @return the sunk ships
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see battleship.IFleet#getSunkShips()
	 */
	@Override
    public List<IShip> getSunkShips() {
        return filterShips(false);
    }

	/**
	 * Ship at ship.
	 *
	 * @param pos the pos
	 * @return the ship
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IFleet#shipAt(battleship.IPosition)
     */
    @Override
    public IShip shipAt(IPosition pos)
    {
		assert pos != null;

		for (IShip ship : ships)
			if (ship.occupies(pos))
				return ship;
		return null;
    }

	/**
	 * Is inside board boolean.
	 *
	 * @param s the s
	 * @return the boolean
	 */
	private boolean isInsideBoard(IShip s)
    {
		assert s != null;

		return (s.getLeftMostPos() >= 0 && s.getRightMostPos() <= Game.BOARD_SIZE - 1 && s.getTopMostPos() >= 0
			&& s.getBottomMostPos() <= Game.BOARD_SIZE - 1);
    }

	/**
	 * Colision risk boolean.
	 *
	 * @param s the s
	 * @return the boolean
	 */
	private boolean colisionRisk(IShip s)
    {
		assert s != null;

		for (int i = 0; i < ships.size(); i++)
		{
			if (ships.get(i).tooCloseTo(s))
				return true;
		}
		return false;
    }

	/**
	 * This operation prints all the given ships
	 *
	 * @param ships The list of ships
	 */
	public void printShips(List<IShip> ships)
	{
		assert ships != null;

		for (IShip ship : ships)
			System.out.println(ship);
	}

	/**
	 * This operation shows the state of a fleet
	 */
	public void printStatus()
    {
		System.out.println("Estado da Frota: " + this.getFloatingShips().size() + " a flutuar, " + this.getSunkShips().size() + " afundados!");
//		printAllShips();
//		printFloatingShips();
//		printShipsByCategory("Galeao");
//		printShipsByCategory("Fragata");
//		printShipsByCategory("Nau");
//		printShipsByCategory("Caravela");
//		printShipsByCategory("Barca");
    }

	/**
	 * This operation prints all the ships of a fleet belonging to a particular
	 * category
	 *
	 * @param category The category of ships of interest
	 */
	public void printShipsByCategory(String category)
    {
		assert category != null;

		printShips(getShipsLike(category));
    }

	/**
	 * This operation prints all the ships of a fleet but not yet shot
	 */
	public void printFloatingShips()
    {
	printShips(getFloatingShips());
    }

	/**
	 * This operation prints all the ships of a fleet
	 */
	void printAllShips()
    {
		printShips(ships);
    }
}
