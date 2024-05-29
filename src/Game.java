import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Game {

    private final int size = 50;

    private int[][] map;

    private MapGenerator mapGenerator;
    private Player player;
    private GameWindow gameWindow;




    private Game() {
        player = new Player(this);

        mapGenerator = new MapGenerator(size);
        gameWindow = new GameWindow(size, 15);

        map = mapGenerator.generate(5);
        player.setMap(map, size);
        player.setPosition(mapGenerator.getPlayerX(), mapGenerator.getPlayerY());
        gameWindow.drawGamePanel(map, player);

        SwingUtilities.updateComponentTreeUI(gameWindow.gamePanel);
    }

    public void nextLevel() {
        gameWindow.remove(gameWindow.gamePanel);
        gameWindow.repaint();
        map = mapGenerator.generate(5, player.getX(), player.getY());
        player.setMap(map, size);
        gameWindow.drawGamePanel(map, player);

        SwingUtilities.updateComponentTreeUI(gameWindow.gamePanel);
    }

    private void loop() {
        player.nextMove();
        // gameWindow.gamePanel.getActionMap().get(r.nextInt(4)).actionPerformed(new ActionEvent((Object) this, 0, ""));
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
