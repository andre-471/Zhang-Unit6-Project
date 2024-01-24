import java.util.Scanner;

public class Ceelo {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String[] MENU_CHOICES = new String[]{"s", "v", "d", "q"};
    private static final int RESULT_WIN = -1;
    private static final int RESULT_LOST = -2;
    private static final int RESULT_INDECISIVE = -3;
    private static boolean debugMode = false;
    private static Dice dice = new Dice(3);
    private Player[] players;
    private Banker banker;
    private int topScore;
    private String topPlayer;

    public Ceelo(int playerCount) {
        players = new Player[playerCount];
        banker = null;
        topScore = 0;
        topPlayer = null;
        play();
    }

    public Ceelo() {
        this(3);
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    private void play() {
        String choice;
        do {
            System.out.println("""
                    S: Start new game
                    V: View top score
                    Q: Quit game""");
            choice = repeatUntil(MENU_CHOICES);
            switch (choice) {
                case "s" -> startGame();
                case "v" -> {
                    if (topPlayer != null) {
                        System.out.printf("Player %s%s%s has the top score with %s%d%s chips.\n",
                                ConsoleUtil.CYAN, topPlayer, ConsoleUtil.RESET,
                                ConsoleUtil.YELLOW, topScore, ConsoleUtil.RESET);
                    } else {
                        System.out.println("The top score is empty.");
                    }
                }
                case "d" -> {
                    debugMode = true;
                    startGame();
                    debugMode = false;
                }
                case "q" -> System.out.println("bye");
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            }
        } while (!"q".equals(choice));
    }

    private void startGame() {
        createPeople();
        ConsoleUtil.clearScreen();
        while (banker.isInGame() && atLeastOnePlayerInGame()) {
            startRound();
            ConsoleUtil.sleep(6000);
            ConsoleUtil.clearScreen();
            printStats();
        }
        if (banker.isInGame()) {
            System.out.printf("The %sbanker%s %swins%s with %s%d%s chips!\n",
                    ConsoleUtil.CYAN, ConsoleUtil.RESET,
                    ConsoleUtil.YELLOW, ConsoleUtil.RESET,
                    ConsoleUtil.YELLOW, banker.getChips(), ConsoleUtil.RESET);
        } else {
            Player roundWinner = playerWithMostChips();
            System.out.printf("Player %s%s%s %swins%s with %s%d%s chips!\n",
                    ConsoleUtil.CYAN, roundWinner.getName(), ConsoleUtil.RESET,
                    ConsoleUtil.YELLOW, ConsoleUtil.RESET,
                    ConsoleUtil.YELLOW, roundWinner.getChips(), ConsoleUtil.RESET);
            if (roundWinner.getChips() > topScore) {
                topScore = roundWinner.getChips();
                topPlayer = roundWinner.getName();
            }
        }

        ConsoleUtil.sleep(6000);
        ConsoleUtil.clearScreen();
    }

    private void startRound() {
        setWagers();
        ConsoleUtil.clearScreen();
        do {
            ConsoleUtil.sleep(2000);
            System.out.printf("%sBanker%s rolls: %s%s%s\n",
                    ConsoleUtil.CYAN, ConsoleUtil.RESET,
                    ConsoleUtil.RED, dice.rollDice(), ConsoleUtil.RESET);
            banker.setRollResult(dice.parseRoll());
        } while (!processBankerRoll());
        System.out.println();

        if (!ifContinueRound()) {
            return;
        }

        for (Player player : players) {
            if (!player.isInGame()) {
                continue;
            }
            do {
                ConsoleUtil.sleep(2000);
                System.out.printf("Player %s%s%s rolls: %s%s%s\n",
                        ConsoleUtil.CYAN, player.getName(), ConsoleUtil.RESET,
                        ConsoleUtil.RED, dice.rollDice(), ConsoleUtil.RESET);
                player.setRollResult(dice.parseRoll());
            } while (!processPlayerRoll(player));
            System.out.println();
        }
    }

    private void createPeople() {
        banker = new Banker();
        for (int i = 0; i < players.length; i++) {
            System.out.printf("What is player %d's name? ", i + 1);
            String name = SCANNER.nextLine().trim();
            players[i] = new Player(name);
        }
    }

    private void setWagers() {
        for (Player player : players) {
            if (!player.isInGame()) {
                continue;
            }
            System.out.printf("Enter %s%s%s's wager: ", ConsoleUtil.CYAN, player.getName(), ConsoleUtil.RESET);
            while (!player.setWager(repeatUntil())) {
                System.out.printf("You only have %d chips, try again: ", player.getChips());
            }
        }
    }

    private boolean processBankerRoll() {
        return switch (banker.getRollResult()) {
            case RESULT_WIN -> {
                System.out.printf("The %sbanker%s automatically wins and collects wagers from each player!\n",
                        ConsoleUtil.CYAN, ConsoleUtil.RESET);
                bankerModifyPlayerWagers("collect");
                yield true;
            }
            case RESULT_LOST -> {
                System.out.printf("The %sbanker%s automatically loses and pays out wagers to each player!\n",
                        ConsoleUtil.CYAN, ConsoleUtil.RESET);
                bankerModifyPlayerWagers("pay");
                yield true;
            }
            case RESULT_INDECISIVE -> {
                System.out.println("Not a valid roll.");
                yield false;
            }
            default -> {
                System.out.printf("The %sbanker%s now has a score of %s%d%s for this round.\n",
                        ConsoleUtil.CYAN, ConsoleUtil.RESET,
                        ConsoleUtil.PURPLE, banker.getRollResult(), ConsoleUtil.RESET);
                yield true;
            }
        };
    }

    private boolean processPlayerRoll(Player player) {
        return switch (player.getRollResult()) {
            case RESULT_WIN -> {
                System.out.printf("%s%s%s automatically wins and collects their wager from the %sbanker%s!\n",
                        ConsoleUtil.CYAN, player.getName(), ConsoleUtil.RESET,
                        ConsoleUtil.CYAN, ConsoleUtil.RESET);
                bankerModifyPlayerWagers(player, "pay");
                yield true;
            }
            case RESULT_LOST -> {
                System.out.printf("%s%s%s automatically loses and gives their wager to the %sbanker%s!\n",
                        ConsoleUtil.CYAN, player.getName(), ConsoleUtil.RESET,
                        ConsoleUtil.CYAN, ConsoleUtil.RESET);
                bankerModifyPlayerWagers(player, "collect");
                yield true;
            }
            case RESULT_INDECISIVE -> {
                System.out.println("Not a valid roll.");
                yield false;
            }
            default -> {
                System.out.printf("%s%s%s now has a score of %s%d%s for this round.\n",
                        ConsoleUtil.CYAN, player.getName(), ConsoleUtil.RESET,
                        ConsoleUtil.PURPLE, player.getRollResult(), ConsoleUtil.RESET);
                ConsoleUtil.sleep(2000);
                if (player.getRollResult() >= banker.getRollResult()) {
                    System.out.printf("%s%s%s beats the %sbanker%s (who has a score of %s%d%s) " +
                            "and collects their wager from the %sbanker%s!\n",
                            ConsoleUtil.CYAN, player.getName(), ConsoleUtil.RESET,
                            ConsoleUtil.CYAN, ConsoleUtil.RESET,
                            ConsoleUtil.PURPLE, banker.getRollResult(), ConsoleUtil.RESET,
                            ConsoleUtil.CYAN, ConsoleUtil.RESET);
                    bankerModifyPlayerWagers(player, "pay");
                } else {
                    System.out.printf("%s%s%s loses to the %sbanker%s (who has a score of %s%d%s) " +
                            "and gives their wager to the %sbanker%s!\n",
                            ConsoleUtil.CYAN, player.getName(), ConsoleUtil.RESET,
                            ConsoleUtil.CYAN, ConsoleUtil.RESET,
                            ConsoleUtil.PURPLE, banker.getRollResult(), ConsoleUtil.RESET,
                            ConsoleUtil.CYAN, ConsoleUtil.RESET);
                    bankerModifyPlayerWagers(player, "collect");
                }
                yield true;
            }
        };
    }

    private void printStats() {
        System.out.println(banker);
        System.out.println();
        ConsoleUtil.sleep();
        for (Player player : players) {
            System.out.println(player);
            System.out.println();
            ConsoleUtil.sleep();
        }
    }

    private boolean ifContinueRound() {
        return 1 <= banker.getRollResult() && banker.getRollResult() <= 6;
    }

    private void bankerModifyPlayerWagers(Player player, String action) {
        ConsoleUtil.sleep(2000);
        switch (action) {
            case "collect" -> {
                System.out.printf("The %sbanker%s takes %s%d%s chips from %s%s%s.\n",
                        ConsoleUtil.CYAN, ConsoleUtil.RESET,
                        ConsoleUtil.YELLOW, player.getWager(), ConsoleUtil.RESET,
                        ConsoleUtil.CYAN, player.getName(), ConsoleUtil.RESET);
                banker.modifyChips(player.getWager());
                player.modifyChips(-player.getWager());
            }
            case "pay" -> {
                System.out.printf("The %sbanker%s gives %s%d%s chips to %s%s%s.\n",
                        ConsoleUtil.CYAN, ConsoleUtil.RESET,
                        ConsoleUtil.YELLOW, player.getWager(), ConsoleUtil.RESET,
                        ConsoleUtil.CYAN, player.getName(), ConsoleUtil.RESET);
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

    private Player playerWithMostChips() {
        Player tempPlayer = players[0];

        for (Player player : players) {
            if (player.getChips() > tempPlayer.getChips()) {
                tempPlayer = player;
            }
        }

        return tempPlayer;
    }

    private boolean atLeastOnePlayerInGame() {
        for (Player player : players) {
            if (player.isInGame()) {
                return true;
            }
        }

        return false;
    }

    private int repeatUntil() {
        while (!SCANNER.hasNextInt()) {
            System.out.print("Enter a number: ");
            SCANNER.next();
        }

        int integer = SCANNER.nextInt();
        SCANNER.nextLine();
        return integer;
    }

    private String repeatUntil(String[] strings) {
        String input = SCANNER.nextLine().trim().toLowerCase();
        while (!ArrayUtility.ifValueInArray(input, MENU_CHOICES)) {
            System.out.print("Error, please type in a valid response: ");
            input = SCANNER.nextLine().trim().toLowerCase();
        }

        return input;
    }
}
