package mastermind.game;

import java.util.ArrayList;

public class Combination {

    // arguments

    private ArrayList<Pawn> pawnTable = new ArrayList<Pawn>();
    private final GameSettings settings;

    // constructeur

    public Combination(GameSettings settings, ArrayList<Integer> table){
        this.settings = settings;
        for(int i = 0; i < table.size(); i++){
            Pawn elem = new Pawn(Color.getColorByIndex(table.get(i)), i);
            pawnTable.add(elem);
        }
    }

    // Getter / Setter

    public ArrayList<Pawn> getPawnTable(){
        return this.pawnTable;
    }

    public Pawn getPawn(int index){
        return this.pawnTable.get(index);
    }

    public GameSettings getSettings(){
        return this.settings;
    }

    // méthodes

    @Override
    public String toString(){
        String result = "";
        for (int i = 0; i < getPawnTable().size(); i++){
            result += getPawn(i).getColor().getColorCode() + "\u2B24";
            if(i < getPawnTable().size() - 1){
                result += "    ";
            }
        }
        return result + "\u001B[0m";
    }
}
