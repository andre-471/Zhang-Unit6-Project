public class Main {
    public static void main(String[] args) {
        Dice dice = new Dice();
        dice.rollDice();
        System.out.println(dice.getValue());
        Player player = new Player("joe");
    }
}