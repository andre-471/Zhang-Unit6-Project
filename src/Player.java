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

    public boolean setWager(int wager) {
        if (wager > super.getChips()) { return false; }
        this.wager = wager;
        return true;
    }

    public int getWager() {
        return wager;
    }

    public String toString() {
        return "player " + name + ":\n" +
                "chips: " + getChips() + "\n" +
                "in game: " + isInGame();
    }
}
