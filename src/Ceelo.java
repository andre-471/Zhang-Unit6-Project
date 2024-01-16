import java.util.Scanner;

public class Ceelo {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final int[] ROLL_WIN = {4, 5, 6};
    private static final int[] ROLL_LOST = {1, 2, 3};
    private static final int RESULT_WIN = 0;
    private static final int RESULT_LOST = -1;
    private static Dice dice = new Dice(3);
    private int topScore;
    private Player[] players;
    private Banker banker;

    public Ceelo() {
        players = null;
        banker = null;
        play();
    }

    private static int rollDice() {
        int[] rolledValues = dice.rollDice();
        if (ArrayUtility.ifEqual(rolledValues, ROLL_WIN) || ArrayUtility.allEqual(rolledValues)) {
            return 0;
        }
        if (ArrayUtility.ifEqual(rolledValues, ROLL_LOST)) {
            return -1;
        }
        if (ArrayUtility.ifDuplicateValues(rolledValues)) {
            return ArrayUtility.getRarestValue(rolledValues);
        }
        return rollDice();
    }

    private void play() {
        String choice;
        do {
            startGame();
            choice = SCANNER.nextLine();
        } while ("y".equals(choice));
    }

    private void startGame() {
        createPeople();
        while (banker.isInGame()) {
            startRound();
        }
    }

    private void startRound() {
        setWagers();

        int bankerResult = rollDice();
        System.out.println("banker: " + bankerResult);
        processBankerRoll(bankerResult);
        if (!ifContinueRound(bankerResult)) {
            return;
        }

        for (Player player : players) {
            int playerResult = rollDice();
            System.out.println("player: " + playerResult);
            playerResult
        }
    }

    private void createPeople() {
        banker = new Banker();
        for (int i = 0; i < players.length; i++) {
            String name = SCANNER.nextLine().trim();
            players[i] = new Player(name);
        }
    }

    private void setWagers() {
        for (Player player : players) {
            System.out.println("Wager!");
            while (!player.setWager(repeatUntilInt())) {
                System.out.println("wager too big nuh uh");
            }
        }
    }

    private void processBankerRoll(int result) {
        switch (result) {
            case RESULT_WIN -> {
                System.out.println("womp womp");
                bankerModifyPlayerWagers("collect");
            }
            case RESULT_LOST -> {
                System.out.println("womp womp");
                bankerModifyPlayerWagers("pay");
            } default -> {
                System.out.println("yay");
            }
        }
    }

    private void processPlayerRoll(Player player, int bankerResult) {
        switch (result) {
            case RESULT_WIN -> {
                bankerModifyPlayerWagers(player, "pay");
            }
            case RESULT_LOST -> {
                bankerModifyPlayerWagers(player, "collect");
            }
            default -> {
                if ()
            }

        }
    }

    private boolean ifContinueRound(int result) {
        return 1 <= result && result <= 6;
    }

    private void bankerModifyPlayerWagers(Player player, String action) {
        switch (action) {
            case "collect" -> {
                banker.modifyChips(player.getWager());
                player.modifyChips(-player.getWager());
            }
            case "pay" -> {
                banker.modifyChips(-player.getWager());
                player.modifyChips(player.getWager());

            }
            default -> throw new IllegalStateException("Unexpected value: " + action);
        }
    }

    private void bankerModifyPlayerWagers(String action) {
        for (Player player : players) {
            bankerModifyPlayerWagers(player, action);
        }
    }

    private int repeatUntilInt() {
        while (!SCANNER.hasNextInt()) {
            System.out.println("nuh uh");
            SCANNER.nextLine();
        }

        return SCANNER.nextInt();
    }
}
