import java.util.HashMap;
import java.util.Scanner;

public class Ceelo {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final int RESULT_WIN = -1;
    private static final int RESULT_LOST = -2;
    private static final HashMap<Integer, String> hashMap = new HashMap<>();
    private static Dice dice = new Dice(3);
    private int topScore;
    private Player[] players;
    private Banker banker;

    public Ceelo() {
        topScore = 0;
        players = new Player[3];
        banker = null;
        hashMap.put(-1, "winner");
        hashMap.put(-2, "loser");
        for (int i = 1; i < 7; i++) {
            hashMap.put(i, "rolled " + i);
        }
        play();
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

        banker.setRollResult(dice.rollDice());
        System.out.println("banker: " + hashMap.get(banker.getRollResult()));
        processBankerRoll();
        if (!ifContinueRound()) {
            return;
        }

        for (Player player : players) {
            if (!player.isInGame()) { continue; }
            player.setRollResult(dice.rollDice());
            System.out.println("player: " + hashMap.get(player.getRollResult()));
            processPlayerRoll(player);
        }
    }

    private void createPeople() {
        banker = new Banker();
        for (int i = 0; i < players.length; i++) {
            System.out.println("name!!!!!!!!!!!!!");
            String name = SCANNER.nextLine().trim();
            players[i] = new Player(name);
        }
    }

    private void setWagers() {
        for (Player player : players) {
            if (!player.isInGame()) { continue; }
            System.out.print("Wager for " + player.getName() + ": ");
            while (!player.setWager(repeatUntilInt())) {
                System.out.print("wager too big nuh uh! ");
            }
        }
    }

    private boolean atLeastOnePlayerInGame() {
        for (Player player : players) {
            if (player.isInGame()) {
                return true;
            }
        }

        return false;
    }


    private void printStats() {
        System.out.println(banker);
        for (Player player: players) {
            System.out.println(player);
        }
    }

    private int highestChipCountForPlayers() {
        int chips = Integer.MIN_VALUE;

        for (Player player : players) {
            chips = Math.max(chips, player.getChips());
        }

        return chips;
    }
    private void processBankerRoll() {
        switch (banker.getRollResult()) {
            case RESULT_WIN -> {
                bankerModifyPlayerWagers("collect");
            }
            case RESULT_LOST -> {
                bankerModifyPlayerWagers("pay");
            }
        }
    }

    private void processPlayerRoll(Player player) {
        switch (player.getRollResult()) {
            case RESULT_WIN -> {
                bankerModifyPlayerWagers(player, "pay");
            }
            case RESULT_LOST -> {
                bankerModifyPlayerWagers(player, "collect");
            }
            default -> {
                if (player.getRollResult() >= banker.getRollResult()) {
                    bankerModifyPlayerWagers(player, "pay");
                } else {
                    bankerModifyPlayerWagers(player, "collect");
                }
            }
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
            bankerModifyPlayerWagers(player, action);
        }
    }

    private int repeatUntilInt() {
        while (!SCANNER.hasNextInt()) {
            System.out.println("nuh uh");
            SCANNER.next();
        }

        return SCANNER.nextInt();
    }
}
