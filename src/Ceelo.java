import java.util.Scanner;

public class Ceelo {
    public static final Scanner SCANNER = new Scanner(System.in);
    public static final int[] ROLL_WIN = {4, 5, 6};
    public static final int[] ROLL_LOST = {1, 2, 3};
    private static Dice[] dices = {new Dice(), new Dice(), new Dice()};
    private int topScore;
    private Player[] players;
    private Banker banker;

    public Ceelo() {
        players = null;
        banker = null;
    }

    public static int[] rollDice() {
        int[] result = new int[dices.length];
        for (int i = 0; i < dices.length; i++) {
            dices[i].rollDice();
            result[i] = dices[i].getValue();
        }

        return result;
    }
    private void run() {
        String choice;
        do {
            startGame();
            choice = SCANNER.nextLine();
        } while ("y".equals(choice));
    }

    private void startGame() {
        createPeople();
        while (true) {
            startRound();
        }
    }

    private void startRound() {
        banker.rollDice();
    }

    private void createPeople() {
        banker = new Banker();
        for (int i = 0; i < players.length; i++) {
            String name = SCANNER.nextLine().trim();
            players[i] = new Player(name);
        }
    }
}
