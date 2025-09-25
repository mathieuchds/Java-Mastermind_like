package mastermind.game;

public class Pawn {

    // arguments

    private final Color color;
    private final int position;

    // constructeurs

    public Pawn(Color color, int position){
        this.color = color;
        this.position = position;
    }

    // Getter / Setter

    public Color getColor(){
        return this.color;
    }

    public int getPosition(){
        return this.position;
    }

    // méthodes

        // compare deux pions
    public boolean equals(Pawn other) {
        return this.getColor() == other.getColor();
    }

    @Override
    public String toString(){
        return this.getColor().getColorCode();
    }
}
