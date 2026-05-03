package battleship;

/**
 * Enumeration for ship kinds to avoid primitive string literals.
 */
public enum ShipKind {
    GALEAO("galeao"),
    FRAGATA("fragata"),
    NAU("nau"),
    CARAVELA("caravela"),
    BARCA("barca");

    private final String label;

    ShipKind(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static ShipKind fromLabel(String label) {
        if (label == null) return null;
        String l = label.toLowerCase();
        for (ShipKind k : values()) {
            if (k.label.equals(l)) return k;
        }
        return null;
    }
}