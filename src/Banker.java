public class Banker extends Person{
    private static final int INITIAL_CHIPS = 1000;

    public Banker() {
        super(INITIAL_CHIPS);
    }

    public String rollDice() {
        int[] rolledValues = Ceelo.rollDice();
        if (ArrayUtility.ifEqual(rolledValues, Ceelo.ROLL_WIN) || ArrayUtility.allEqual(rolledValues)) {
            return "win";
        }
        if (ArrayUtility.ifEqual(rolledValues, Ceelo.ROLL_LOST)) {
            return "lost";
        }
        if (ArrayUtility.ifDuplicateValues(rolledValues)) {
            return String.valueOf(ArrayUtility.getRarestValue(rolledValues));
        }

        return "indeterminate";
    }


}
