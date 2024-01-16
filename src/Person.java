public abstract class Person {
    private int chips;
    private int score;
    private boolean inGame;

    public Person(int initalChips) {
        chips = initalChips;
        inGame = true;
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
}
