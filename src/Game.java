public class Game {
    private final int SIZE = 50;
    private final int TILE_SIZE = 15;

    private int[][] map;

    private MapGenerator mapGenerator;
    private Player player;
    private GameWindow gameWindow;

    private Game() {
        player = new Player(this);

        mapGenerator = new MapGenerator(SIZE);
        gameWindow = new GameWindow(SIZE, TILE_SIZE);

        map = mapGenerator.generate(5);
        player.setMap(map, SIZE);
        player.setPosition(mapGenerator.getPlayerX(), mapGenerator.getPlayerY());
        gameWindow.drawGamePanel(map, player);
    }

    public void nextLevel() {
        gameWindow.remove(gameWindow.gamePanel);
        gameWindow.repaint();
        map = mapGenerator.generate(5, player.getX(), player.getY());
        player.setMap(map, SIZE);
        gameWindow.drawGamePanel(map, player);
    }

    private void loop() {
        player.nextMove();
    }

    public static void main(String[] args) {
        Game game = new Game();
        while (true) {
            game.loop();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
