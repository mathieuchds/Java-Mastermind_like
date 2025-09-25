package mastermind.game;

import java.util.ArrayList;
import java.util.Collections;

public class GameSettings {

    // Attributs
    
    int difficulty;
    int nbTry;
    int nbPawn;
    int nbColor;
    boolean givePawnPosition;
    boolean multiplayer;
    boolean oneColorPawn;
    ArrayList<Color> colorList;

    // Constructeur

    public GameSettings(int difficulty){
        this.difficulty = difficulty;
        while((this.difficulty < 0)||(this.difficulty > 3)){
            this.difficulty = Integer.parseInt(System.console().readLine("Veuillez entrer un mode de difficulté valide\n\n"));
        }

        if(this.difficulty == 0){
            this.nbTry = Integer.parseInt(System.console().readLine("\n\nNombre d'essai voulu: 10/11/12\n\n"));
            while((this.nbTry < 10)||(this.nbTry > 12)){
                this.nbTry = Integer.parseInt(System.console().readLine("\n\nVeuillez entrer un nombre d'essai valide (10/11/12)\n\n"));
            }

            this.nbPawn = Integer.parseInt(System.console().readLine("\n\nTaille de combinaison voulu: 4/5\n\n"));
            while((this.nbPawn != 4)&&(this.nbPawn != 5)){
                this.nbPawn = Integer.parseInt(System.console().readLine("\n\nVeuillez entrer une taille de combinaison valide (4/5)\n\n"));
            }

            this.nbColor = Integer.parseInt(System.console().readLine("\n\nAvec combien de couleur voulez vous jouer: 6/7/8\n\n"));
            while((this.nbColor < 6)||(this.nbColor > 8)){
                this.nbColor = Integer.parseInt(System.console().readLine("\n\nVeuillez entrer un nombre de couleur valide (6/7/8)\n\n"));
            }

            String oneColor = System.console().readLine("\n\nAutorisez-vous les combinaisons avec plusieurs pions de même couleur : oui/non\n\n");
            while((!oneColor.equals("oui"))&&(!oneColor.equals("non"))){
                oneColor = System.console().readLine("\n\nVeuillez entrer une réponse valide (oui/non)\n\n");
            }
            if(oneColor == "oui"){
                this.oneColorPawn = false;
            }else{
                this.oneColorPawn = true;
            }

            int giveDifficulty = Integer.parseInt(System.console().readLine("\n\nNiveau de difficulté des indications :\n\n(1) Facile : les bonnes couleurs et bons pions sont clairement indiqués\n(2) Expert : seul le nombre de bons pions et bonnes couleurs sont indiqués\n\n"));
            while((giveDifficulty != 1)&&(giveDifficulty != 2)){
                giveDifficulty = Integer.parseInt(System.console().readLine("\n\nVeuillez entrer une difficulté valide (1/2)\n\n"));
            }
            if(giveDifficulty == 1){
                this.givePawnPosition = true;
            }else{
                this.givePawnPosition = false;
            }
        }else if(this.difficulty == 1){
            this.nbTry = 12;
            this.nbPawn = 4;
            this.nbColor = 6;
            this.givePawnPosition = true;
            this.oneColorPawn = true;
        }else if(this.difficulty == 2){
            this.nbTry = 10;
            this.nbPawn = 5;
            this.nbColor = 6;
            this.givePawnPosition = true;
            this.oneColorPawn = false;
        }else{
            this.nbTry = 10;
            this.nbPawn = 5;
            this.nbColor = 8;
            this.givePawnPosition = false;
            this.oneColorPawn = false;
        }
        this.colorList = new ArrayList<>();
        colors(this.nbColor);
    }

    public GameSettings(int difficulty, int nbTry, int nbPawn, int nbColor, boolean oneColorPawn, boolean givePawnPosition){
        this.difficulty=difficulty;
        this.nbTry=nbTry;
        this.nbPawn=nbPawn;
        this.nbColor=nbColor;
        this.givePawnPosition=givePawnPosition;
        this.oneColorPawn=oneColorPawn;
        this.colorList = new ArrayList<>();
        colors(this.nbColor);
    }

    // Getter / Setter

    public int getDifficulty(){
        return this.difficulty;
    }

    public int getNbTry(){
        return this.nbTry;
    }

    public int getNbPawn(){
        return this.nbPawn;
    }

    public int getNbColor(){
        return this.nbColor;
    }

    public boolean getGivePawnPosition(){
        return this.givePawnPosition;
    }

    public ArrayList<Color> getColorList() {
        return colorList;
    }

    public boolean getOneColorPawn() {
        return this.oneColorPawn;
    }

    public boolean getMultiplayer(){
        return this.multiplayer;
    }

    public void setMultiplayer(boolean m){
        this.multiplayer = m;
    }
    
    // Méthodes 

        //renvoie l'affichage de la table des couleurs
    public String colorTable(){
        String chain = "";
        Collections.sort(getColorList());
        for (int i = 0 ; i < getColorList().size() ; i++){
            chain += "\u001B[0m" + String.valueOf(i) + " : " + getColorList().get(i).toString()+ "\n";
        }
        chain += "\u001B[0m";
        return chain;
    }

        // créé la table des couleurs
    private void colors(int nbColor) {

        ArrayList<Integer> randomList = new ArrayList<>();

        for (int i = 0 ; i < nbColor ; i++) {
            randomList.add(i);
        }

        Collections.shuffle(randomList);

        for (int i = 0 ; i < nbColor ; i++) {
            getColorList().add(Color.getColorByIndex(randomList.get(i)));
        } 
    }

    @Override
    public String toString() {

        String chain = "Vous avez choisi la difficulté ";

        if(getDifficulty() == 0){
            chain += "personnalisé";
        }else if(getDifficulty() == 1){
            chain += "facile";
        }else if(getDifficulty() == 2){
            chain += "moyenne";
        } else {
            chain += "difficile";
        }

        chain += ", vous jouez donc avec les paramètres de jeu suivant :\n    - " + getNbTry() + " essais\n    - Combinaison de " + getNbPawn() + " pions\n    - " + getNbColor() + " couleurs possibles par pion\n    - ";
        if (this.oneColorPawn){
            chain += "Tout les pions sont de couleurs différentes";
        } else {
            chain += "Il peut y avoir plusieurs pions de la même couleur";
        }
        chain += "\n    - ";
        if (this.givePawnPosition){
            chain += "La position des pions bien placés est indiqué\n les pions bien placés seront affichés avec: O\n les pions mal placés seront affichés avec: ~\n et les pions invalide seront affichés par: X";
        }else {
            chain += "Vous n'obtenez que le nombre de pions bien placés ou mal placés";
        }

        chain += "\n\nPour entrer la combinaison souhaitée, vous devrez entrer le chiffre correspondant à la couleur souhaité sous la forme : ";
        
        for (int i = 0 ; i < getNbPawn(); i++){
            chain += "X";
            if(i!=getNbPawn()-1){
                chain+=",";
            }
        }

        chain += " où X est un chiffre.\n\n";
        chain += "La table des couleurs utilisée est la suivante :\n\n";

        chain += colorTable();

        return chain;
    }
}
