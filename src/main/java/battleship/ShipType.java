package battleship;

/**
 * Enumeration of the different types of ships in the game.
 */
public enum ShipType {
	/**
	 * Barge ship type.
	 */
	BARCA("barca"),
	/**
	 * Caravel ship type.
	 */
	CARAVELA("caravela"),
	/**
	 * Carrack ship type.
	 */
	NAU("nau"),
	/**
	 * Frigate ship type.
	 */
	FRAGATA("fragata"),
	/**
	 * Galleon ship type.
	 */
	GALEAO("galeao");

	private final String code;

	ShipType(String code) {
		this.code = code;
	}

	/**
	 * Returns the ShipType corresponding to the given string code.
	 *
	 * @param code the string code of the ship type
	 * @return the matching ShipType, or null if not found
	 */
	public static ShipType fromCode(String code) {
		for (ShipType type : values()) {
			if (type.code.equalsIgnoreCase(code)) {
				return type;
			}
		}
		return null;
	}
}
