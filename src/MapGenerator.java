import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

public class MapGenerator {
    private int[][] map;
    private int size;

    private NoiseGenerator noiseGenerator;
    private Random random;

    private int playerX, playerY;

    public static final int EXIT = -1;

    public MapGenerator(int size) {
        this.noiseGenerator = new NoiseGenerator();
        this.random = new Random();
        this.size = size;
        this.map = new int[size][size];
    }

    private int[][] generateNoise(int noiseSize) {
        int[][] map = new int[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                map[x][y] = (int) (this.noiseGenerator.noise(x, y, 0, noiseSize) + 0.9);
            }
        }

        return map;
    }

    private int fillUnreachableTiles() {
        int maxSize = 0;

        int maxX = 0, maxY = 0;

        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                if (map[x][y] == 0) {
                    int size = fillArea(x, y, 0, 2);
                    if (size > maxSize) {
                        maxSize = size;
                        maxX = x;
                        maxY = y;
                    }
                }
            }
        }

        int size = fillArea(maxX, maxY, 2, 0);

        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                if (map[x][y] == 2) {
                    fillArea(x, y, 2, 1);
                }
            }
        }

        return size;
    }

    private int fillArea(int x, int y, int value, int fillValue) {
        int count = 0;

        if (map[x][y] != value) return 0;
        if (map[x][y] == fillValue) return 0;

        map[x][y] = fillValue;
        count++;

        if (x > 0) count += fillArea(x - 1, y, value, fillValue);
        if (y > 0) count += fillArea(x, y - 1, value, fillValue);
        if (x < size - 1) count += fillArea(x + 1, y, value, fillValue);
        if (y < size - 1) count += fillArea(x, y + 1, value, fillValue);

        return count;
    }

    private boolean findPlayerSpawn() {
        ArrayList<Integer> availableTiles = new ArrayList<>();

        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                if (checkTileAvailability(x, y)) {
                    availableTiles.add(x*size+y);
                }
            }
        }

        if (availableTiles.isEmpty()) {
            return false;
        }

        int randomTileIndex = random.nextInt(availableTiles.size());

        playerX = availableTiles.get(randomTileIndex) / size;
        playerY = availableTiles.get(randomTileIndex) % size;

        return true;
    }

    private boolean checkTileAvailability(int x, int y) {
        try {
            if (map[x+1][y+1] != 0) return false;
            if (map[x][y+1] != 0) return false;
            if (map[x-1][y+1] != 0) return false;

            if (map[x+1][y] != 0) return false;
            if (map[x][y] != 0) return false;
            if (map[x-1][y] != 0) return false;

            if (map[x+1][y-1] != 0) return false;
            if (map[x][y-1] != 0) return false;
            if (map[x-1][y-1] != 0) return false;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        return true;
    }

    private void findLevelExit() {
        Random r = new Random();

        int x, y;
        do {
            x = r.nextInt(size);
            y = r.nextInt(size);
        } while (map[x][y] != 0);

        map[x][y] = EXIT;
    }

    public int[][] generate(int noiseSize) {
        this.noiseGenerator.setSeed(new Random().nextGaussian() * 255);

        do {
            this.noiseGenerator.setSeed(new Random().nextGaussian() * 255);
            this.map = generateNoise(noiseSize);
        } while (fillUnreachableTiles() < size*size/4 || !findPlayerSpawn());

        findLevelExit();

        return map;
    }

    public int[][] generate(int noiseSize, int startX, int startY) {
        do {
            this.noiseGenerator.setSeed(new Random().nextGaussian() * 255);
            this.map = generateNoise(noiseSize);
        } while (fillUnreachableTiles() < size*size/4 || this.map[startX][startY] != 0);

        fillUnreachableTiles();
        findLevelExit();

        return map;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }
}