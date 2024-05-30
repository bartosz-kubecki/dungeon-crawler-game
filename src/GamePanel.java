import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class GamePanel extends JLayeredPane {
    public int tileSize;

    private Player player;
    private JPanel playerPanel;

    public GamePanel(int tileSize) {
        this.tileSize = tileSize;
        setLayout(null);
    }

    public void setPlayer(Player player) {
        this.player = player;
        if (this.playerPanel != null) {
            remove(this.playerPanel);
        }
        this.playerPanel = null;

        putKeyEventActions(getInputMap(WHEN_IN_FOCUSED_WINDOW));
        player.setGamePanel(this);
        drawPlayer();
    }

    public void drawMap(int[][] map, int size) {
        PanelListener listener = new PanelListener(player);

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                JPanel panel = new GameTile(x, y);
                panel.setBounds(tileSize * x,tileSize * y,tileSize,tileSize);
                panel.addMouseListener(listener);

                if (map[x][y] == MapGenerator.EXIT) {
                    panel.setBackground(Color.GREEN);
                } else if (map[x][y] == 1) {
                    panel.setBackground(Color.BLACK);
                } else if (map[x][y] <= 0) {
                    panel.setBackground(x%2 == 0 ^ y%2 == 0 ? Color.LIGHT_GRAY : null);
                }

                add(panel, JLayeredPane.DEFAULT_LAYER);
            }
        }
    }

    public void drawPlayer() {
        if (playerPanel == null) {
            this.playerPanel = new JPanel();
            this.playerPanel.setBackground(Color.BLUE);

            add(this.playerPanel, JLayeredPane.PALETTE_LAYER);
        }

        int x = player.getX();
        int y = player.getY();

        playerPanel.setBounds(tileSize * x,tileSize * y,tileSize,tileSize);
    }

    private void putKeyEventActions(InputMap im) {
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), 0);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0), 0);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), 0);

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), 1);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0), 1);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), 1);

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), 2);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), 2);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), 2);

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), 3);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), 3);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), 3);
    }

    private class GameTile extends JPanel {
        int x, y;

        public GameTile(int x, int y) {
            super();
            this.x = x;
            this.y = y;
        }
    }

    private class PanelListener extends MouseAdapter {
        private Player player;

        public PanelListener(Player player) {
            this.player = player;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Object source = e.getSource();
            if(source instanceof GameTile panelPressed){
                int x = panelPressed.x;
                int y = panelPressed.y;

                System.out.println("("+x+","+y+")");
                player.moveTo(x, y);
            }
        }
    }
}
