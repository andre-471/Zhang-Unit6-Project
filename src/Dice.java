public class Dice {
    private int[] values;
    private static final int[] ROLL_WIN = {4, 5, 6};
    private static final int[] ROLL_LOST = {1, 2, 3};

    public Dice(int count) {
        values = new int[count];
    }

    public String rollDice() {
        for (int i = 0; i < values.length; i++) {
            values[i] = (int) (Math.random() * 6) + 1;
        }

        StringBuilder sb = new StringBuilder();
        for (int value : values) {
            sb.append(value);
            sb.append(" ");
        }
        return sb.toString();
    }

    public int parseRoll() {
        if (ArrayUtility.ifEqual(values, ROLL_WIN) || ArrayUtility.allEqual(values)) {
            return -1;
        }
        if (ArrayUtility.ifEqual(values, ROLL_LOST)) {
            return -2;
        }
        if (ArrayUtility.ifDuplicateValues(values)) {
            return ArrayUtility.getRarestValue(values);
        }
        return -3;
    }
}
