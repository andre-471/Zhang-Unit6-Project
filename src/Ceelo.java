import java.util.Scanner;

public class Ceelo {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final int RESULT_WIN = -1;
    private static final int RESULT_LOST = -2;
    private static final int RESULT_INDECISIVE = -3;
    private static Dice dice = new Dice(3);
    private Player[] players;
    private Banker banker;
    private int topScore;

    public Ceelo(int playerCount) {
        players = new Player[playerCount];
        banker = null;
        topScore = 0;
        play();
    }

    public Ceelo() {
        this(3);
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
        while (banker.isInGame() && atLeastOnePlayerInGame()) {
            startRound();
            printStats();
        }
        topScore = highestChipCountForPlayers();
    }

    private void startRound() {
        setWagers();
        do {
            System.out.printf("Banker %s%n",dice.rollDice());
            banker.setRollResult(dice.parseRoll());
        } while (!processBankerRoll());

        if (!ifContinueRound()) {
            return;
        }

        for (Player player : players) {
            if (!player.isInGame()) {
                continue;
            }
            do {
                System.out.printf("Player %s %s%n",player.getName(), dice.rollDice());
                player.setRollResult(dice.parseRoll());
            } while (!processPlayerRoll(player));
        }
    }

    private void createPeople() {
        banker = new Banker();
        for (int i = 0; i < players.length; i++) {
            System.out.print("What is your name? ");
            String name = SCANNER.nextLine().trim();
            players[i] = new Player(name);
        }
    }

    private void setWagers() {
        for (Player player : players) {
            if (!player.isInGame()) {
                continue;
            }
            System.out.print("Wager for " + player.getName() + ": ");
            while (!player.setWager(repeatUntilInt())) {
                System.out.printf("You only have %d chips, try again: ", player.getChips());
            }
        }
    }

    private boolean processBankerRoll() {
        return switch (banker.getRollResult()) {
            case RESULT_WIN -> {
                bankerModifyPlayerWagers("collect");
                yield true;
            }
            case RESULT_LOST -> {
                bankerModifyPlayerWagers("pay");
                yield true;
            }
            case RESULT_INDECISIVE -> false;
            default -> true;
        };
    }

    private boolean processPlayerRoll(Player player) {
        return switch (player.getRollResult()) {
            case RESULT_WIN -> {
                bankerModifyPlayerWagers(player, "pay");
                yield true;
            }
            case RESULT_LOST -> {
                bankerModifyPlayerWagers(player, "collect");
                yield true;
            }
            case RESULT_INDECISIVE -> false;
            default -> {
                if (player.getRollResult() >= banker.getRollResult()) {
                    bankerModifyPlayerWagers(player, "pay");
                } else {
                    bankerModifyPlayerWagers(player, "collect");
                }
                yield true;
            }
        };
    }

    private void printStats() {
        System.out.println(banker);
        for (Player player : players) {
            System.out.println(player);
        }
    }

    private boolean ifContinueRound() {
        return 1 <= banker.getRollResult() && banker.getRollResult() <= 6;
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
            if (!player.isInGame()) {
                continue;
            }
            bankerModifyPlayerWagers(player, action);
        }
    }

    private int highestChipCountForPlayers() {
        int chips = Integer.MIN_VALUE;

        for (Player player : players) {
            chips = Math.max(chips, player.getChips());
        }

        return chips;
    }

    private boolean atLeastOnePlayerInGame() {
        for (Player player : players) {
            if (player.isInGame()) {
                return true;
            }
        }

        return false;
    }

    private int repeatUntilInt() {
        while (!SCANNER.hasNextInt()) {
            System.out.print("Type a number: ");
            SCANNER.next();
        }

        int integer = SCANNER.nextInt();
        SCANNER.nextLine();
        return integer;
    }
}
