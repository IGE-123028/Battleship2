/**
 * 
 */
package battleship;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * The type Ship.
 */
public abstract class Ship implements IShip
{
	/**
	 * The constant GALEAO.
	 */
	private static final String GALEAO = "galeao";
	/**
	 * The constant FRAGATA.
	 */
	private static final String FRAGATA = "fragata";
	/**
	 * The constant NAU.
	 */
	private static final String NAU = "nau";
	/**
	 * The constant CARAVELA.
	 */
	private static final String CARAVELA = "caravela";
	/**
	 * The constant BARCA.
	 */
	private static final String BARCA = "barca";

	/**
	 * Create a new ship
	 *
	 * @param shipKind the ship kind
	 * @param bearing  the bearing
	 * @param pos      the pos
	 * @return s ship
	 */
	static Ship buildShip(String shipKind, Compass bearing, Position pos)
    {
		assert shipKind != null;
		assert bearing != null;
		assert pos != null;

        Ship s;
        switch (shipKind)
        {
        case BARCA:
            s = new Barge(bearing, pos);
			break;
        case CARAVELA:
            s = new Caravel(bearing, pos);
			break;
        case NAU:
            s = new Carrack(bearing, pos);
			break;
        case FRAGATA:
            s = new Frigate(bearing, pos);
			break;
        case GALEAO:
            s = new Galleon(bearing, pos);
			break;
        default:
            s = null;
        }
        return s;
    }

    //---------------------------------------------------------

	/**
	 * The Category.
	 */
	private String category;

	/**
	 * The Bearing.
	 */
	private Compass bearing;

	/**
	 * The Pos.
	 */
	private IPosition pos;

	/**
	 * The size
	 */
	private Integer size;

	/**
	 * The Positions occupied by the ship.
	 */
	protected List<IPosition> positions;

	/**
	 * Create ships
	 *
	 * @param category The category of ships of interest
	 * @param bearing  The bearing of ships of interest
	 * @param pos      The position of ships of interest
	 * @param size		The number of positions occupied by the ship
	 */
	public Ship(String category, Compass bearing, IPosition pos, int size)
    {
		this.category = Objects.requireNonNull(category, "Ship's category must not be null");
		this.bearing = Objects.requireNonNull(bearing, "Ship's bearing must not be null");
		this.pos = Objects.requireNonNull(pos, "Ship's position must not be null");
	
		this.category = category;
		this.bearing = bearing;
		this.pos = pos;
		this.size = size;

		positions = new ArrayList<>();
    }

	/**
	 * Gets category.
	 *
	 * @return the category
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IShip#getCategory()
     */
    @Override
    public String getCategory()
    {
	return category;
    }

	/**
	 * Gets positions.
	 *
	 * @return the positions
	 */
	public List<IPosition> getPositions()
    {
		return positions;
    }

	/**
	 * Retrieves a list of positions adjacent to the ship's current positions.
	 * Adjacent positions are defined as neighboring positions that are not
	 * already occupied by the ship and are not duplicates in the final list.
	 *
	 * @return a list of IPosition objects representing adjacent positions
	 */
	public List<IPosition> getAdjacentPositions()
	{
		List<IPosition> adjacentPositions = new ArrayList<IPosition>();
		for (IPosition position : getPositions())
		{
			List<IPosition> adjacents = position.adjacentPositions();
			for (IPosition adj : adjacents)
				if (!getPositions().contains(adj) && !adjacentPositions.contains(adj))
					adjacentPositions.add(adj);
		}
		return adjacentPositions;
	}

	/**
	 * Gets position.
	 *
	 * @return the position
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IShip#getPosition()
     */
    @Override
    public IPosition getPosition()
    {
	return pos;
    }

	/**
	 * Gets bearing.
	 *
	 * @return the bearing
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IShip#getBearing()
     */
    @Override
    public Compass getBearing()
    {
	return bearing;
    }


	/**
	 * Gets size.
	 *
	 * @return the size
	 */
	public Integer getSize()
	{
		return size;
	}

	/**
	 * Still floating boolean.
	 *
	 * @return the boolean
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IShip#stillFloating()
     */
    @Override
    public boolean stillFloating()
    {
	for (int i = 0; i < getSize(); i++)
	    if (!getPositions().get(i).isHit())
		return true;
	return false;
    }

	/**
	 * Gets top most pos.
	 *
	 * @return the top most pos
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IShip#getTopMostPos()
     */
    @Override
    public int getTopMostPos()
    {
	int top = getPositions().get(0).getRow();
	for (int i = 1; i < getSize(); i++)
	    if (getPositions().get(i).getRow() < top)
		top = getPositions().get(i).getRow();
	return top;
    }

	/**
	 * Gets bottom most pos.
	 *
	 * @return the bottom most pos
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IShip#getBottomMostPos()
     */
    @Override
    public int getBottomMostPos()
    {
	int bottom = getPositions().get(0).getRow();
	for (int i = 1; i < getSize(); i++)
	    if (getPositions().get(i).getRow() > bottom)
		bottom = getPositions().get(i).getRow();
	return bottom;
    }

	/**
	 * Gets left most pos.
	 *
	 * @return the left most pos
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IShip#getLeftMostPos()
     */
    @Override
    public int getLeftMostPos()
    {
	int left = getPositions().get(0).getColumn();
	for (int i = 1; i < getSize(); i++)
	    if (getPositions().get(i).getColumn() < left)
		left = getPositions().get(i).getColumn();
	return left;
    }

	/**
	 * Gets right most pos.
	 *
	 * @return the right most pos
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IShip#getRightMostPos()
     */
    @Override
    public int getRightMostPos()
    {
	int right = getPositions().get(0).getColumn();
	for (int i = 1; i < getSize(); i++)
	    if (getPositions().get(i).getColumn() > right)
		right = getPositions().get(i).getColumn();
	return right;
    }

	/**
	 * Occupies boolean.
	 *
	 * @param pos the pos
	 * @return the boolean
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IShip#occupies(battleship.IPosition)
     */
    @Override
    public boolean occupies(IPosition pos)
    {
		assert pos != null;

		for (int i = 0; i < getSize(); i++)
			if (getPositions().get(i).equals(pos))
				return true;
		return false;
    }

	/**
	 * Too close to boolean.
	 *
	 * @param other the other
	 * @return the boolean
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IShip#tooCloseTo(battleship.IShip)
     */
    @Override
    public boolean tooCloseTo(IShip other)
    {
		assert other != null;

		Iterator<IPosition> otherPos = other.getPositions().iterator();
		while (otherPos.hasNext())
			if (tooCloseTo(otherPos.next()))
			return true;

		return false;
    }

	/**
	 * Too close to boolean.
	 *
	 * @param pos the pos
	 * @return the boolean
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IShip#tooCloseTo(battleship.IPosition)
     */
    @Override
    public boolean tooCloseTo(IPosition pos)
    {
		assert pos != null;

		for (int i = 0; i < this.getSize(); i++)
			if (getPositions().get(i).isAdjacentTo(pos))
			return true;
		return false;
    }


	/**
	 * Shoot.
	 *
	 * @param pos the pos
	 */
	/*
     * (non-Javadoc)
     * 
     * @see battleship.IShip#shoot(battleship.IPosition)
     */
    @Override
    public void shoot(IPosition pos)
    {
		assert pos != null;
		assert pos.isInside();

		for (IPosition position : getPositions())
		{
			if (position.equals(pos))
				position.shoot();
		}
    }

	/**
	 * Sink.
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see battleship.IShip#sink()
	 */
	@Override
	public void sink() {
		for (IPosition position : getPositions())
			position.shoot();
	}

	/**
	 * To string string.
	 *
	 * @return the string
	 */
	@Override
    public String toString()
    {
	return "[" + category + " " + bearing + " " + pos + "]";
    }

}
