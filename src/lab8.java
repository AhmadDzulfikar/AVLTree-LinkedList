import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class Lab8 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int V = in.nextInt(); // jumlah halte
        int E = in.nextInt(); // jumlah jalur
        Graph graph = new Graph(V);

        // Membangun graf
        for (int i = 0; i < E; i++) {
            int X = in.nextInt();
            int Y = in.nextInt();
            long W = in.nextLong();
            graph.addEdge(X, Y, W);
        }

        int D = in.nextInt(); // jumlah hari
        int M = in.nextInt(); // durasi hingga gerbang sekolah ditutup

        // Menghitung rute setiap hari
        while (D-- > 0) {
            int N = in.nextInt(); // banyak siswa yang akan dijemput
            List<Integer> students = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                students.add(in.nextInt());
            }

            // Mencari rute yang meminimalkan waktu tempuh
            long totalTime = 0;
            int lastStop = -1;
            int currentPosition = 1; // Mulai dari halte 1 (sekolah)

            for (int stop : students) {
                long timeToNext = graph.dijkstra(currentPosition, stop);
                if (totalTime + timeToNext <= M) {
                    totalTime += timeToNext;
                    lastStop = stop;
                    currentPosition = stop;
                } else {
                    break;
                }
            }

            if (lastStop == -1) {
                out.println("-1 -1");
            } else {
                long timeBack = graph.dijkstra(lastStop, 1);
                if (totalTime + timeBack <= M) {
                    out.println(totalTime + timeBack + " " + lastStop);
                } else {
                    out.println("-1 -1");
                }
            }
        }

        out.close();
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}

// Implementasi Graph
class Graph {
    private final int V;
    private final List<List<Edge>> adjList;

    public Graph(int V) {
        this.V = V;
        adjList = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to, long weight) {
        adjList.get(from - 1).add(new Edge(to - 1, weight));
        adjList.get(to - 1).add(new Edge(from - 1, weight));
    }

    // Menggunakan algoritma Dijkstra untuk mencari jarak terpendek
    public long dijkstra(int source, int destination) {
        long[] dist = new long[V];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[source - 1] = 0;
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingLong(edge -> edge.weight));
        pq.add(new Edge(source - 1, 0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            int u = current.to;
            if (dist[u] < current.weight) continue;

            for (Edge neighbor : adjList.get(u)) {
                int v = neighbor.to;
                long weight = neighbor.weight;
                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.add(new Edge(v, dist[v]));
                }
            }
        }
        return dist[destination - 1];
    }

    static class Edge {
        int to;
        long weight;

        public Edge(int to, long weight) {
            this.to = to;
            this.weight = weight;
        }
    }
}
