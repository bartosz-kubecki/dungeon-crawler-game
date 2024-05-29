import java.util.*;
import java.util.LinkedList;

public class Graph {
    private HashMap<Integer, Vertex> vertices;
    private Vertex[][] vertexMap;

    public Graph(int[][] map) {
        vertices = new HashMap<>();

        int size = map.length;
        vertexMap = new Vertex[size][size];

        int i = 0;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                vertexMap[x][y] = new Vertex(i);
                i++;

                if (x != 0 && y != 0 && map[x][y-1] <= 0 && map[x-1][y] <= 0 && (map[x][y] <= 0 || map[x-1][y-1] <= 0)) {
                    addEdge(vertexMap[x][y-1], vertexMap[x-1][y], 3);
                }

                if (map[x][y] <= 0) {
                    if (y != 0 && map[x][y-1] <= 0) {
                        addEdge(vertexMap[x][y], vertexMap[x][y-1], 2);
                    }
                    if (x != 0 && map[x-1][y] <= 0) {
                        addEdge(vertexMap[x][y], vertexMap[x-1][y], 2);
                    }

                    if (x != 0 && y != 0 && map[x-1][y-1] <= 0 && (map[x][y-1] <= 0 || map[x-1][y] <= 0)) {
                        addEdge(vertexMap[x][y], vertexMap[x-1][y-1], 3);
                    }

                    addVertex(vertexMap[x][y]);
                }
            }
        }
    }

    private void addVertex(Vertex node) {
        vertices.put(node.getKey(), node);
    }

    private void addEdge(Vertex a, Vertex b, int weight)
    {
        Edge edge = new Edge(a, b, weight);
        a.getEdges().add(edge);
        b.getEdges().add(edge);
    }

    public void findPath(int startX, int startY, int endX, int endY, Deque<Integer> pathX, Deque<Integer> pathY) {
        int size = vertexMap.length;

        for (Vertex vertex :
                dijkstra(vertexMap[startX][startY], vertexMap[endX][endY])) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (vertexMap[x][y] == vertex) {
                        pathX.push(x);
                        pathY.push(y);
                    }

                }
            }
        }
    }

    private LinkedList<Vertex> dijkstra(Vertex start, Vertex end) {
        HashSet<Vertex> settledVertices = new HashSet<>();
        HashSet<Edge> settledEdges = new HashSet<>();

        HashMap<Integer, Integer> distance = new HashMap<>();
        HashMap<Integer, Vertex> previousVertex = new HashMap<>();

        for (Map.Entry<Integer, Vertex> entry : this.vertices.entrySet()) {
            Vertex vertex = entry.getValue();
            distance.put(vertex.getKey(), Integer.MAX_VALUE);
        }
        distance.put(start.getKey(), 0);

        while (settledVertices.size() < vertices.size()) {
            Vertex lowestVertex = null;
            int lowestDistance = Integer.MAX_VALUE;
            for (Map.Entry<Integer, Vertex> entry : this.vertices.entrySet()) {
                Vertex vertex = entry.getValue();
                if (distance.get(vertex.getKey()) <= lowestDistance && !settledVertices.contains(vertex)) {
                    lowestVertex = vertex;
                    lowestDistance = distance.get(vertex.getKey());
                }
            }

            if (lowestVertex != null) {
                Edge lowestEdge;
                do {
                    lowestEdge = null;
                    int lowestWeight = Integer.MAX_VALUE;
                    for (Edge edge : lowestVertex.getEdges()) {
                        if (edge.getWeight() < lowestWeight && !settledEdges.contains(edge)) {
                            lowestEdge = edge;
                            lowestWeight = edge.getWeight();
                        }
                    }

                    if (lowestEdge != null && distance.get(lowestVertex.getKey()) != Integer.MAX_VALUE) {
                        Vertex vertex = lowestEdge.getA() == lowestVertex ? lowestEdge.getB() : lowestEdge.getA();
                        int newDistance = distance.get(lowestVertex.getKey()) + lowestEdge.getWeight();
                        if (distance.get(vertex.getKey()) > newDistance) {
                            distance.put(vertex.getKey(), newDistance);
                            previousVertex.put(vertex.getKey(), lowestVertex);
                        }
                    }

                    settledEdges.add(lowestEdge);
                } while (lowestEdge != null);

                settledVertices.add(lowestVertex);
            }
        }

        LinkedList<Vertex> path = new LinkedList<>();

        Vertex vertex = end;
        while (vertex != start) {
            path.add(vertex);
            vertex = previousVertex.get(vertex.getKey());
        }

        return path;
    }

    private class Vertex {
        private LinkedList<Edge> edges;
        private Integer key;

        public Vertex(Integer value) {
            this.key = value;
            edges = new LinkedList<>();
        }

        public Integer getKey() {
            return key;
        }

        public LinkedList<Edge> getEdges() {
            return edges;
        }
    }

    private class Edge {
        private Vertex a;
        private Vertex b;

        private int weight;

        public Edge(Vertex a, Vertex b, int weight) {
            this.a = a;
            this.b = b;

            this.weight = weight;
        }

        public Vertex getA() {
            return a;
        }

        public Vertex getB() {
            return b;
        }

        public int getWeight() {
            return weight;
        }
    }
}