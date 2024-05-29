import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private final String TITLE = "Dungeon Crawler Game";

    GamePanel gamePanel;
    private int size;
    private int tileSize;

    public GameWindow(int size, int tileSize){
        this.size = size;
        this.tileSize = tileSize;

        setTitle(this.TITLE);

        Insets insets = getInsets();
        setSize(size*tileSize+insets.right+insets.left + 40,
                size*tileSize+insets.top+insets.bottom + 60);
        setLayout(null);

        //setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public void drawGamePanel(int[][] map, Player player) {
        if (gamePanel != null) {
            remove(gamePanel);
        }

        gamePanel = new GamePanel(tileSize);
        gamePanel.setBounds(10,10,
                size*tileSize, size*tileSize);

        gamePanel.setPlayer(player);
        gamePanel.drawMap(map, size);

        add(gamePanel);

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                StringBuffer tile = new StringBuffer();
                switch (map[x][y]) {
                    case 0:
                        tile.append("\u001B[42m ");
                        break;
                    case 1:
                        tile.append("\u001B[41m ");
                        break;
                    case 2:
                        tile.append("\u001B[43m ");
                        break;
                }
                tile.append(map[x][y]);
                tile.append(" \u001B[0m");
                System.out.print(tile);
            }
            System.out.println();
        }
    }
}
