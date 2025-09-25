package mastermind.game;

public class Main {
    public static void main(String[] args){

        Game game = new Game();
        game.play();
    }

    public static void clearConsole() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    } 
}
