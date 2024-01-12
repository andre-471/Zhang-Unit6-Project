public abstract class Person {
    private int chips;
    private int score;

    public Person(int initalChips) {
        chips = initalChips;
    }

    public int getChips() {
        return chips;
    }

    public void modifyChips(int chips) {
        this.chips += chips;
         if (this.chips < 0) {
            this.chips = 0;
        }
    }

    public int rollDice() {
        int[] rolledValues = Ceelo.rollDice();
        if (ArrayUtility.ifEqual(rolledValues, Ceelo.ROLL_WIN) || ArrayUtility.allEqual(rolledValues)) {
            return 0;
        }
        if (ArrayUtility.ifEqual(rolledValues, Ceelo.ROLL_LOST)) {
            return -1;
        }
        if (ArrayUtility.ifDuplicateValues(rolledValues)) {
            return ArrayUtility.getRarestValue(rolledValues);
        }

        return -2;
    }
}
