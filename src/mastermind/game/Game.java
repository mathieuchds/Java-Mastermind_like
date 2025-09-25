package mastermind.game;

import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;



public class Game {

    // arguments

    private int nbPlayers;
    private int nbParty;
    private int currentPlayer;
    private int currentParty;
    private int[][] points;
    private GameSettings settings;


    // méthodes

        // méthode principale du jeu
    public void play(){
        Main.clearConsole();
        int choice = Integer.parseInt(System.console().readLine("Bonjour, quel mode voulez-vous utilisez?\n   - 1. Solo\n   - 2. Multijoueur\n   - 3. Recharger une partie non achevée\n\n"));
        while(choice != 1 && choice != 2 && choice != 3){
            choice = Integer.parseInt(System.console().readLine("Mauvais choix!\n   - 1. Solo\n   - 2. Multijoueur\n   - 3. Recharger une partie non achevée\n\n"));
        }
        if(choice != 3){
            int difficulty = Integer.parseInt(System.console().readLine("\nChoisissez le niveau de difficulté de la partie :\n    - 1 : Facile \n    - 2 : Moyenne \n    - 3 : Difficile\n    - 0 : Personnalisé\n\n"));
            while(difficulty<0 && difficulty>3){
                difficulty = Integer.parseInt(System.console().readLine("\nMauvais choix!\n    - 1 : Facile \n    - 2 : Moyenne \n    - 3 : Difficile\n    - 0 : Personnalisé\n\n"));
            }
            settings = new GameSettings(difficulty);
            currentParty = 0;
            currentPlayer = 0;
        }else{
            load(System.console().readLine("Donnez le nom du fichier à charger. "));
        }
        if (choice == 1 || (choice == 3 && settings.getMultiplayer()==false)){
            nbPlayers = 1;
            nbParty = 1;
            currentParty = 1;
            currentPlayer = 1;
            points = new int[nbParty][nbPlayers];
            GameTable t = new GameTable(settings);
            settings.setMultiplayer(false);
            for(int i=0;i<t.getSettings().getNbTry();i++){
                if(i!=0){
                    System.out.println("\n\n\n\n" + t + "\n");
                }
                int result = t.nextTry();
                if(result==1){
                    break;
                }else if(result==-1){
                    save(System.console().readLine("Donnez le nom du fichier. "), settings, t);
                    System.out.println("Au revoir!");
                    return;
                }
            }
            points[0][0] = t.getCurrentTry();
        }else if(choice == 2 || (choice == 3 && settings.getMultiplayer()==true)){
            if(choice == 2){
                settings.setMultiplayer(true);
                nbPlayers = Integer.parseInt(System.console().readLine("Entrez le nombre de joueur qui participe: "));
                nbParty = Integer.parseInt(System.console().readLine("Entrez le nombre de partie que vous voulez jouer: "));
                points = new int[nbParty][nbPlayers];
            }
            for(int k=currentParty;k<nbParty;k++){
                System.out.println("\n\n\n\n\n\nPartie numéro " + String.valueOf(k+1) +" !");
                currentParty = k;
                for(int j=currentPlayer;j<nbPlayers;j++){
                    GameTable t = new GameTable(settings);
                    currentPlayer = j;
                    System.out.println("\n\nJoueur " + (j+1) + ", à vous de jouer!\n");
                    for(int i=0;i<t.getSettings().getNbTry();i++){
                        if(i!=0){
                            System.out.println("\n\n\n\n" + t + "\n");
                        }
                        int result = t.nextTry();
                        if(result==1){
                            break;
                        }else if(result==-1){
                            save(System.console().readLine("Donnez le nom du fichier. "), settings, t);
                            System.out.println("Au revoir!");
                            return;
                        }
                    }
                    points[k][j]=t.getCurrentTry();
                }
                currentPlayer=0;
                System.out.println("\n\n\n\n\n\n\n\n");
            }
            int[] score = score(points,settings.getNbTry());
            ArrayList<Integer> winners = max(score);
            String res = "";
            if(winners.size() == 1){
                res="Bravo joueur " + String.valueOf(winners.get(0)+1) + " vous avez gagné avec " + String.valueOf(score[winners.get(0)]) + " tentatives restantes au total !";
            }else if(winners.size()==nbPlayers && score[0]==settings.getNbTry()){
                res="Dommage !\n Merci d'avoir participé";
            }else{
                res = "Il y a égalité entre les joueurs:\n";
                for(int i=0;i<winners.size();i++){
                    res+= String.valueOf(winners.get(i) + 1);
                    if(i<winners.size()-2){
                        res+=" , ";
                    }else if (i<winners.size()-1){
                        res+=" et ";
                    }
                }
                res+="\nBravo à vous! Vous aviez " + String.valueOf(score[winners.get(0)]) + " tentatives restantes au total !";
            }
            res+="\n\nClassement final:\n";
            for(int i=0;i<score.length;i++){
                res+="Joueur " + String.valueOf(i+1) + " : " + String.valueOf(score[i]) + " tentatives restantes\n";
            }
            System.out.println(res);
        }
    }

        // méthode qui trouve le ou les gagnant(s)
    public ArrayList<Integer> max(int[] score){
        ArrayList<Integer> index = new ArrayList<Integer>();
        int max=0;
        for(int i=0;i<score.length;i++){
            if(score[i]>score[max]){
                max = i;
            }
        }
        for(int i=0;i<score.length;i++){
            if(score[i] == score[max]){
                index.add(i);
            }
        }
        return index;
    }

        // calcul des scores de chaque joueurs
    public int[] score(int[][] points, int nbTry){
        int[] score = new int[nbPlayers];
        for(int i=0;i<points.length;i++){
            for(int j=0;j<points[i].length;j++){
                score[j]+= (nbTry - points[i][j]);
            }
        }
        return score;
    }

        // sauvegarder la partie dans un fichier
    public void save(String file, GameSettings settings, GameTable t){
        String res = String.valueOf(settings.multiplayer==true?1:0) + "/";
        int difficulty = settings.getDifficulty();
        res+=String.valueOf(difficulty);
        if(difficulty == 0){
            res+="," + String.valueOf(settings.getNbTry()) + "," + String.valueOf(settings.getNbPawn()) + "," + String.valueOf(settings.getNbColor()) + "," + String.valueOf(settings.getOneColorPawn()==true ? 1 : 0) + "," + String.valueOf(settings.getGivePawnPosition()==true ? 1 : 0);
        }
        res+="/" + String.valueOf(nbPlayers) + "/" + String.valueOf(nbParty) + "/" + String.valueOf(currentPlayer) + "/" + String.valueOf(currentParty) + "/";
        for(int i=0;i<points.length;i++){
            for(int j=0;j<points[i].length;j++){
                res+=String.valueOf(points[i][j]);
                if(j!=points[i].length-1){
                    res+=":";
                }
            }
            if(i!=points.length-1){
                res+=",";
            }
        }
        res+="/";
        Secret secret = t.getSecretCombination();
        for(int i=0;i<secret.getPawnTable().size();i++){
            res+=secret.getPawn(i).getColor().getValue();
            if(i!=secret.getPawnTable().size()-1){
                res+=":";
            }
        }
        res+="/" + String.valueOf(t.getCurrentTry()) + "/";
        for(int i=0;i<t.getTryCombinations().size();i++){
            Combination c = t.getTryCombinations().get(i);
            for(int j=0;j<c.getPawnTable().size();j++){
                res+=c.getPawn(j).getColor().getValue();
                if(j!=c.getPawnTable().size()-1){
                    res+=":";
                }
            }
            if(i!=t.getTryCombinations().size()-1){
                res+=",";
            }
        }

        // écriture fichier
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(res);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        // charge une partie sauvegardé auparavant
    public void load(String file){
        String[] data = new String[8];

        // lecture fichier
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            
            while((line = br.readLine()) != null){
                data=line.split("/");
            }
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        if(data[1]=="0"){
            String[] loadSettings = data[1].split(",");
            settings = new GameSettings(Integer.parseInt(loadSettings[0]),Integer.parseInt(loadSettings[1]),Integer.parseInt(loadSettings[2]),Integer.parseInt(loadSettings[3]),loadSettings[4]=="1"?true:false,loadSettings[5]=="1"?true:false);
        }else{
            settings = new GameSettings(Integer.parseInt(data[1]));
        }
        settings.setMultiplayer(data[0]=="0"?false:true);
        nbPlayers = Integer.parseInt(data[2]);
        nbParty = Integer.parseInt(data[3]);
        currentPlayer = Integer.parseInt(data[4]);
        currentParty = Integer.parseInt(data[5]);
        points = new int[nbParty][nbPlayers];
        String[] loadPoints = data[6].split(",");
        for(int i=0;i<loadPoints.length;i++){
            String[] line = loadPoints[i].split(":");
            for(int j=0; j<line.length; j++){
                points[i][j]=Integer.parseInt(line[j]);
            }
        }
        GameTable t = new GameTable(settings,data[7],Integer.parseInt(data[8]),data[9]);
        System.out.println("\n\nJoueur " + (currentPlayer+1) + ", a vous de jouer!\n");
        System.out.println(t.getCurrentTry());
        for(int i=t.getCurrentTry();i<t.getSettings().getNbTry();i++){
            if(i!=0){
                System.out.println("\n\n\n\n" + t + "\n");
            }
            int result = t.nextTry();
            if(result==1){
                break;
            }else if(result==-1){
                save(System.console().readLine("Donnez le nom du fichier. "), settings, t);
                System.out.println("Au revoir!");
                return;
            }
        }
        points[currentParty][currentPlayer]=t.getCurrentTry();
        if((currentPlayer+1)==nbPlayers){
            currentParty++;
            currentPlayer=0;
        }else{
            currentPlayer++;
        }
    }
}
