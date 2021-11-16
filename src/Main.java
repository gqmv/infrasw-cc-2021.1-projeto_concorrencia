public class Main {
    public static void main(String[] args) {
        Player player = new Player();
        Counter counter = new Counter(player.getPlayPressedCondition(), player);
        counter.start();
    }
}
