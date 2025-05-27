package dungeon.engine;


import java.util.List;
import java.util.Scanner;

public class RunGameText {

    private gameEngine engine;
    private final Scanner scanner;
    int difficultyDefault = 3;

    public RunGameText(){
        this.scanner = new Scanner(System.in);
    }

    private void displayGameMessages(){
        List<String> messages = engine.Messages();
        for (String message : messages){
            System.out.println(message);
        }
    }

    public void startGame(){
        System.out.println("Welcome to Mini Dungeon!");
        int difficulty = difficultyDefault;
        boolean loadChoice = promptForLoad();
        if (!loadChoice) difficulty = promptForDifficulty();
        engine = new gameEngine(loadChoice, difficulty);

        while(!engine.isGameOver()){
            displayGame();
            getPlayerInput();
            if (!engine.isGameOver()){
                engine.processGameTurn(); //clears msgs
            }
            displayGameMessages();
        }
        scanner.close();
    }

    private boolean promptForLoad(){
        System.out.println("Would you like to (L)oad the saved game or (S)tart a new game?");
        String choice = scanner.nextLine().toLowerCase();
        return choice.equals("l");
    }

    private int promptForDifficulty(){
        int difficulty = difficultyDefault;
        boolean correctInput = false;

        for (int i = 0; i < 2; i++) {
            System.out.println("Select difficulty to play (0-10): ");
            String input = scanner.nextLine();
            try {
                int chosenDifficulty = Integer.parseInt(input);
                if (chosenDifficulty >= 0 && chosenDifficulty <= 10) {
                    difficulty = chosenDifficulty;
                    correctInput = true;
                    break;
                } else {
                    if (i == 0) {
                        System.out.println("Invalid input. Please enter a valid number (e.g. 5).");
                    }
                }
            } catch (NumberFormatException e) {
                if (i == 0) {
                    System.out.println("Invalid input. Please enter a valid number (e.g. 5).");
                }
            }
        }
        if (!correctInput)
            System.out.printf("Invalid input. Difficulty set to default (%d).\n", difficulty);

        return difficulty;
    }

    private void displayGame(){
        System.out.println("MovesRemaining: " + engine.getMoves() +
                            " HP: " + engine.getHealth() +
                            " Score: " + engine.getScore());
        engine.getMap().displayMap();
    }

    private void getPlayerInput() {
        System.out.println("Enter your move (1=left, 2=up, 3=down, 4=right):");
        String input = scanner.nextLine().toLowerCase();
        switch (input) {
            case "1" -> movePlayer(-1,0,"Left");
            case "2" -> movePlayer(0, -1, "up");
            case "3" -> movePlayer(0,1, "down");
            case "4" -> movePlayer(1, 0, "right");
            case "q" -> engine.setGameOver(true);
            case "s" -> engine.saveGame();
            case "l" -> engine.loadGame();
            // more inputs
            default -> {
                System.out.println("Invalid move.");
                getPlayerInput();
            }
        }
    }

    private void movePlayer(int dx, int dy, String direction) {
        engine.processPlayerMove(dx, dy, direction);
    }

    public static void main(String[] args){
        RunGameText text = new RunGameText();
        text.startGame();
    }
}
