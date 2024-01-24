public class Banker extends Person {
    private static final int INITIAL_CHIPS = 1000;

    public Banker() {
        super(INITIAL_CHIPS);
    }

    public String toString() {
        return "The " + ConsoleUtil.CYAN + "banker" + ConsoleUtil.RESET + ":\n" +
                "Chips: " + ConsoleUtil.YELLOW + getChips() + ConsoleUtil.RESET + "\n" +
                (isInGame() ? ConsoleUtil.GREEN + "IS" : ConsoleUtil.RED + "IS NOT") +
                ConsoleUtil.RESET + " In Game ";
    }
}
