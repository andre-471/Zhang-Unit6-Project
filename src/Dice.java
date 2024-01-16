import java.util.Arrays;

public class Dice {
    private int[] values;
    public Dice(int count) {
        values = new int[count];
    }

    public Dice() {
        values = new int[1];
    }

    public int[] rollDice() {
        for (int i = 0; i < values.length; i++) {
            values[i] = (int) (Math.random() * 6) + 1;
        }
        return getValues();
    }

    public int[] getValues() {
        return Arrays.copyOf(values, values.length);
    }
}
