import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class Player {
    private int x, y;

    Game game;
    GamePanel gamePanel;
    int[][] map;
    int mapSize;

    Deque<Integer> movementQueueX = new LinkedList<>();
    Deque<Integer> movementQueueY = new LinkedList<>();

    public Player(Game game) {
        this.game = game;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        ActionMap am = gamePanel.getActionMap();

        am.put(0, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (y < 1) return;
                if (map[x][y-1] > 0) return;

                setPosition(x, y-1);
            }
        });

        am.put(1, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (x > mapSize - 2) return;
                if (map[x+1][y] > 0) return;

                setPosition(x+1, y);
            }
        });

        am.put(2, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (y > mapSize - 2) return;
                if (map[x][y+1] > 0) return;

                setPosition(x, y+1);
            }
        });

        am.put(3, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (x < 1) return;
                if (map[x-1][y] > 0) return;

                setPosition(x-1, y);
            }
        });
    }

    public void setMap(int[][] map, int mapSize) {
        this.map = map;
        this.mapSize = mapSize;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveTo(int x, int y) {
        if (map[x][y] > 0) return;

        setPath(x, y);
    }

    private void setPath(int x, int y) {
        Graph graph = new Graph(map);

        graph.findPath(this.x, this.y, x, y, movementQueueX, movementQueueY);
    }

    public void nextMove() {
        if (movementQueueX == null || movementQueueY == null) return;
        if (movementQueueX.isEmpty() || movementQueueY.isEmpty()) return;

        int nextX = movementQueueX.peek();
        int nextY = movementQueueY.peek();

        if (nextX != this.x && nextY != this.y) {
            if (nextX == this.x + 1 && nextY == this.y + 1) {
                if (map[this.x + 1][this.y] <= 0) {
                    setPosition(this.x + 1, this.y);
                } else if (map[this.x][this.y + 1] <= 0) {
                    setPosition(this.x, this.y + 1);
                }
            } else if (nextX == this.x + 1 && nextY == this.y - 1) {
                if (map[this.x + 1][this.y] <= 0) {
                    setPosition(this.x + 1, this.y);
                } else if (map[this.x][this.y - 1] <= 0) {
                    setPosition(this.x, this.y - 1);
                }
            } else if (nextX == this.x - 1 && nextY == this.y - 1) {
                if (map[this.x - 1][this.y] <= 0) {
                    setPosition(this.x - 1, this.y);
                } else if (map[this.x][this.y - 1] <= 0) {
                    setPosition(this.x, this.y - 1);
                }
            } else if (nextX == this.x - 1 && nextY == this.y + 1) {
                if (map[this.x - 1][this.y] <= 0) {
                    setPosition(this.x - 1, this.y);
                } else if (map[this.x][this.y + 1] <= 0) {
                    setPosition(this.x, this.y + 1);
                }
            }
        } else {
            setPosition(movementQueueX.poll(), movementQueueY.poll());
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;

        if (map[x][y] == MapGenerator.EXIT) {
            game.nextLevel();
        }

        if (gamePanel != null) {
            gamePanel.drawPlayer();
        }
    }
}
