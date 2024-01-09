public class Dice {
    private int value;
    public Dice() {
        value = -1;
    }

    public void rollDice() {
        value = (int) (Math.random() * 6) + 1;
    }

    public int getValue() {
        return value;
    }
}
