public abstract class Person {
    private int chips;
    private int score;

    public Person(int initalChips) {
        chips = initalChips;
    }

    public int getChips() {
        return chips;
    }

    public abstract String rollDice();
}
