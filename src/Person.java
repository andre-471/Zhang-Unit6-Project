public abstract class Person {
    private int chips;
    private int score;
    private boolean inGame;
    private int rollResult;

    public Person(int initalChips) {
        chips = initalChips;
        inGame = true;
        rollResult = 0;
    }

    public int getChips() {
        return chips;
    }

    public void modifyChips(int chips) {
        this.chips += chips;
         if (this.chips < 0) {
            this.chips = 0;
            inGame = false;
        }
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setRollResult(int rollResult) {
        this.rollResult = rollResult;
    }

    public int getRollResult() {
        return rollResult;
    }

    public abstract String toString();
}
