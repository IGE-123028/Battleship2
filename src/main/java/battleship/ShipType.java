package battleship;

public enum ShipType {
	BARCA("barca"),
	CARAVELA("caravela"),
	NAU("nau"),
	FRAGATA("fragata"),
	GALEAO("galeao");

	private final String code;

	ShipType(String code) {
		this.code = code;
	}

	public static ShipType fromCode(String code) {
		if (code == null)
			return null;

		for (ShipType type : values())
			if (type.code.equalsIgnoreCase(code))
				return type;
		return null;
	}
}
