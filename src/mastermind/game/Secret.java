package mastermind.game;

import java.util.Random;
import java.util.ArrayList;

public class Secret extends Combination{

    // constructeur

    public Secret(GameSettings settings, ArrayList<Integer> table){
        super(settings,table);
    }

    // méthode

        //creer une arraylist random
    public static ArrayList<Integer> createRandomList(GameSettings settings){
        ArrayList<Integer> table = new ArrayList<Integer>();
        Random rand = new Random();
        if (settings.getOneColorPawn()==false){
            for(int i = 0; i < settings.getNbPawn(); i++){
                table.add(rand.nextInt((settings.getNbColor())));
            }
        }else{
            for(int i = 0; i < settings.getNbPawn(); i++){
                int nb = rand.nextInt((settings.getNbColor()));
                while(table.contains(nb)){
                    nb = rand.nextInt((settings.getNbColor()));
                }
                table.add(nb);
            }
        }
        return table;
    }

        // compare la combinaisons secrète avec une combinaison du jeu
    public int[] compare(Combination combination) {
        int[] resultat = new int[this.getPawnTable().size()];
        
        // Tableau pour stocker les occurrences de chaque couleur dans la solution
        int[] occurrencesSolution = new int[Color.values().length];
        for (Pawn pawn : this.getPawnTable()) {
            occurrencesSolution[pawn.getColor().getValue()]++;
        }
        
        // Étape 1: Vérifier les éléments corrects et bien placés
        for (int i = 0; i < combination.getPawnTable().size(); i++) {
            Pawn pComb = combination.getPawn(i);
            Pawn pSol = this.getPawn(i);
            if (pComb.equals(pSol)) {
                resultat[i] = 2;  // correct et bien placé
                occurrencesSolution[pComb.getColor().getValue()]--; // décrémenter l'occurrence
            }
        }
        
        // Étape 2: Vérifier les éléments corrects mais mal placés
        for (int i = 0; i < combination.getPawnTable().size(); i++) {
            Pawn pComb = combination.getPawn(i);
            if (resultat[i] != 2 && occurrencesSolution[pComb.getColor().getValue()] > 0) {
                resultat[i] = 1;  // correct mais mal placé
                occurrencesSolution[pComb.getColor().getValue()]--; // décrémenter l'occurrence
            }
        }
        
        return resultat;
    }
}
