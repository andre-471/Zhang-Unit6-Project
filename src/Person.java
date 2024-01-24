public abstract class Person {
    private int score;
    private int chips;
    private boolean inGame;
    private int rollResult;

    public Person(int initialChips) {
        chips = initialChips;
        inGame = true;
        rollResult = 0;
    }

    public int getChips() {
        return chips;
    }

    public int getRollResult() {
        return rollResult;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setRollResult(int rollResult) {
        this.rollResult = rollResult;
    }

    public void modifyChips(int chips) {
        this.chips += chips;
        if (this.chips <= 0) {
            this.chips = 0;
            inGame = false;
        }
    }

    public abstract String toString();
}
