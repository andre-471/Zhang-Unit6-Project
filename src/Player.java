public class Player extends Person {
    private static final int INITIAL_CHIPS = 100;
    private String name;
    private int wager;

    public Player(String name) {
        super(INITIAL_CHIPS);
        this.name = name;
        wager = -1;
    }

    public String getName() {
        return name;
    }

    public int getWager() {
        return wager;
    }

    public boolean setWager(int wager) {
        if (wager > getChips()) {
            return false;
        }
        this.wager = wager;
        return true;
    }

    public String toString() {
        return "Player " + ConsoleUtil.CYAN + name + ConsoleUtil.RESET + ":\n" +
                "Chips: " + ConsoleUtil.YELLOW + getChips() + ConsoleUtil.RESET + "\n" +
                (isInGame() ? ConsoleUtil.GREEN + "IS" : ConsoleUtil.RED + "IS NOT") +
                ConsoleUtil.RESET + " In Game ";
    }
}
