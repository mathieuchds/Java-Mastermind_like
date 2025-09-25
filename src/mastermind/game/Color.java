package mastermind.game;

public enum Color {

    //enumeration

    NOIR(0, "\u001B[30m"),
    ROUGE(1, "\u001b[31m"),
    VERT(2, "\u001B[32m"),
    JAUNE(3, "\u001B[33m"),
    BLEU(4, "\u001B[34m"),
    VIOLET(5, "\u001B[35m"),
    CYAN(6, "\u001B[36m"),
    BLANC(7, "\u001B[37m");

    //arguments

    private final int value;
    private final String colorCode;

    // constructeurs

    Color(int value, String colorCode) {
        this.value = value;
        this.colorCode = colorCode;
    }

    // Getter / Setter

    public int getValue() {
        return this.value;
    }

    public String getColorCode() {
        return this.colorCode;
    }

    public static Color getColorByIndex(int value) {
        for (Color color : Color.values()) {
            if (color.value == value) {
                return color;
            }
        }
        return null;
    }

    // méthodes

    @Override
    public String toString(){
        return getColorCode() + "\u2B24";
    }
}
