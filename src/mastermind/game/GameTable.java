package mastermind.game;

import java.util.ArrayList;
import java.io.IOException;

public class GameTable {

    // Arguments

    private final GameSettings settings;
    private Secret secretCombination;
    private ArrayList<Combination> tryCombinations;
    private int currentTry;


    // Getter/Setter

    public GameSettings getSettings() {
        return this.settings;
    }

    public Secret getSecretCombination() {
        return this.secretCombination;
    }

    public ArrayList<Combination> getTryCombinations() {
        return this.tryCombinations;
    }

    public int getCurrentTry(){
        return this.currentTry;
    }

    // Constructor

    public GameTable(GameSettings settings) {
        this.settings = settings;
        this.secretCombination = new Secret(this.settings,Secret.createRandomList(this.settings));
        this.tryCombinations = new ArrayList<>();
        this.currentTry = 0;
    }

    public GameTable (GameSettings settings, String secret, int currentTry, String tryCombination){
        this.settings=settings;
        ArrayList<Integer> s = new ArrayList<Integer>(); 
        String[] data = secret.split(":");
        for(int i=0;i<data.length;i++){
            s.add(Integer.parseInt(data[i]));
        }
        this.secretCombination = new Secret(settings, s);
        this.currentTry = currentTry;
        this.tryCombinations = new ArrayList<>();
        data = tryCombination.split(",");
        for(int i=0;i<data.length;i++){
            String[] line = data[i].split(":");
            ArrayList<Integer> c = new ArrayList<Integer>();
            for(int j=0;j<line.length;j++){
                c.add(Integer.parseInt(line[j]));
            } 
            this.tryCombinations.add(new Combination(settings, c));
        }
    }

    // Methods

        // creer une nouvelle combinaison de pions
    public boolean addCombinaison(){
        String[] line = System.console().readLine("Saisissez vos " + settings.getNbPawn() + " valeurs (X,X,X,X...), '-1' si vous voulez sauvegarder ").split(",");
        boolean test = false;
        while(!test){
            if (line.length==settings.getNbPawn()){
                test = true;
            }else if (line.length==1 && line[0].equals("-1")){
                return true;
            }else{
                line = System.console().readLine("Nombre incorrect de pions, veuillez resaisir " + settings.getNbPawn() + " valeurs (X,X,X,X...) ou '-1' si vous voulez sauvegarder ").split(",");
            }
        }
        boolean good=false;
        do{
            good=true;
            for(int i=0;i<line.length;i++){
                if(Integer.parseInt(line[i]) < 0 || Integer.parseInt(line[i]) >= settings.getNbColor()){
                    good=false;
                    break;
                }
            }
            if(good==false){
                line = System.console().readLine("Erreur dans la saisie de vos pions, veuillez entrer " + settings.getNbPawn() + " valeurs entre 1 et " + String.valueOf(settings.getNbPawn()) + " (X,X,X,X...) ").split(",");
            }
        }while(good == false);
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for(int i=0;i<line.length;i++){
            numbers.add(Integer.parseInt(line[i]));
        }
        this.getTryCombinations().add(new Combination(settings,numbers));
        return false;
    }

        // creer une combinaison et la compare avec la combinaison secrète
    public int nextTry(){
        if (currentTry == 0){
            System.out.println(settings);
            System.out.println("\n\nAppuyez sur une touche pour débuter la partie");
            System.console().readLine();
            try {
                Process process = Runtime.getRuntime().exec("clear");
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("\n\nDébut de la partie :");

        }
        System.out.println("il vous reste " + (settings.getNbTry() - currentTry) + " essais\n");
        boolean save = addCombinaison();
        if (save){
            return -1;
        }
        int[] position = secretCombination.compare(tryCombinations.get(currentTry));
        boolean win = true;
        for(int i=0;i<settings.getNbPawn();i++){
            if(position[i]!=2){
                win=false;
            }
        }
        if(win){
            System.out.println("\nBravo vous avez gagné ! La combinaison secrète était bien:\n" + secretCombination.toString());
            return 1;
        }else if(!win && currentTry==settings.getNbTry()-1){
            System.out.println("Dommage\nMerci d'avoir participé.\nLa bonne combinaison était:\n" + secretCombination);
        }else{
            String res="";
            if(settings.getGivePawnPosition()){
                for(int i=0;i<settings.getNbPawn();i++){
                    if(position[i]==0){
                        res+="X  ";
                    }else if(position[i]==1){
                        res+="~  ";
                    }else{
                        res+="O  ";
                    }
                }
            }else{
                int wellPlaced2=0, poorlyPlaced2=0;
                for(int i=0;i<settings.getNbPawn();i++){
                    if(position[i]==1){
                        poorlyPlaced2++;
                    }else if(position[i]==2){
                        wellPlaced2++;
                    }
                }
                res+= String.valueOf(wellPlaced2) + " pions biens placés, " + String.valueOf(poorlyPlaced2) + " pions mal placés" + "\n";
            }
            System.out.println("Combinaison donnée:  " + tryCombinations.get(currentTry) + " | " + res + "\n\n");
        }
        currentTry++;
        return 0;
    }

        // affichage des indices (pions bien placés ou non) 
    public String combinationsRes(){
        String res="";
        if(settings.getGivePawnPosition()){     // si on veut affiché les positions des pions bien placés
            for(int j=0;j<tryCombinations.size();j++){
                int[] position = secretCombination.compare(tryCombinations.get(j));
                String chain="";
                for(int i=0;i<settings.getNbPawn();i++){
                    if(position[i]==0){
                        chain+="X  ";
                    }else if(position[i]==1){
                        chain+="~  ";
                    }else{
                        chain+="O  ";
                    }
                }
                res+= tryCombinations.get(j) + " | " + chain + "\n";
            }
        }else{      // si on veut juste le nombre de pions bien placés ou mal placés
            for(int j=0;j<tryCombinations.size();j++){
                int wellPlaced=0, poorlyPlaced=0;
                int[] position = secretCombination.compare(tryCombinations.get(j));
                for(int i=0;i<settings.getNbPawn();i++){
                    if(position[i]==1){
                        poorlyPlaced++;
                    }else if (position[i]==2){
                        wellPlaced++;
                    }
                }
                res+= tryCombinations.get(j) + " | " + String.valueOf(wellPlaced) + " pions biens placés, " + String.valueOf(poorlyPlaced) + " pions mal placés" + "\n";
            }
        }
        return res;
    }

    @Override
    public String toString() {
        String chain = "La table des couleurs utilisées:\n" + settings.colorTable() + "\n\n";
        chain+="Rappel des combinaisons déjà données:\n" + combinationsRes();
        return chain;
    }
}