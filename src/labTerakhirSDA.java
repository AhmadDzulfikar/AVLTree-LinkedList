import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class labTerakhirSDA {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int V = in.nextInt(); // Number of bus stops
        int E = in.nextInt(); // Number of routes
        Graph graph = new Graph(V);

        // TODO: Bangun graf
        for (int i = 0; i < E; i++) {
            int X = in.nextInt();
            int Y = in.nextInt();
            long W = in.nextLong();
            // TODO: Inisiasi waktu tempuh antar halte X dengan Y
            graph.addEdge(X, Y, W);
        }

        int D = in.nextInt(); // Banyak hari aturan diberlakukan
        long M = in.nextLong(); // Durasi hingga gerbang sekolah ditutup

        // List to store all days' pick-up lists
        List<List<Integer>> allDays = new ArrayList<>();
        // Set to collect unique bus stops (including school stop)
        Set<Integer> uniqueStops = new HashSet<>();
        uniqueStops.add(1); // School bus stop

        // TODO: Read all days' data and collect unique stops
        while (D-- > 0) {
            int T = in.nextInt(); // Number of students to pick up on this day
            List<Integer> pickupList = new ArrayList<>();
            while (T-- > 0) {
                int H = in.nextInt(); // Bus stop to pick up a student
                pickupList.add(H);
                uniqueStops.add(H);
            }
            allDays.add(pickupList);
        }

        // Map each unique bus stop to an index for distance matrix
        List<Integer> stopList = new ArrayList<>(uniqueStops);
        Map<Integer, Integer> stopToIndex = new HashMap<>();
        for (int i = 0; i < stopList.size(); i++) {
            stopToIndex.put(stopList.get(i), i);
        }

        // Precompute shortest paths between all relevant bus stops
        int totalStops = stopList.size();
        long[][] shortestPaths = new long[totalStops][totalStops];
        for (int i = 0; i < totalStops; i++) {
            int currentStop = stopList.get(i);
            long[] distances = graph.dijkstra(currentStop);
            for (int j = 0; j < totalStops; j++) {
                int targetStop = stopList.get(j);
                shortestPaths[i][j] = distances[targetStop];
            }
        }

        // TODO: Process each day's pick-up schedule
        for (List<Integer> pickupList : allDays) {
            long totalTime = 0;
            int lastPickedStop = -1;
            int numPickups = pickupList.size();

            if (numPickups == 0) {
                // No students to pick up
                out.println("-1 -1");
                continue;
            }

            // Start from the school bus stop (1)
            int schoolIdx = stopToIndex.get(1);
            int firstStopIdx = stopToIndex.get(pickupList.get(0));
            totalTime += shortestPaths[schoolIdx][firstStopIdx];
            long returnTime = shortestPaths[firstStopIdx][schoolIdx];

            if (totalTime + returnTime > M) {
                // Cannot pick up the first student within the time limit
                out.println("-1 -1");
                continue;
            }

            // Successfully picked up the first student
            lastPickedStop = pickupList.get(0);
            int currentPickup = 1;

            // Attempt to pick up as many students as possible
            while (currentPickup < numPickups) {
                int prevStop = pickupList.get(currentPickup - 1);
                int currentStop = pickupList.get(currentPickup);
                int prevIdx = stopToIndex.get(prevStop);
                int currentIdx = stopToIndex.get(currentStop);
                totalTime += shortestPaths[prevIdx][currentIdx];
                returnTime = shortestPaths[currentIdx][schoolIdx];

                if (totalTime + returnTime <= M) {
                    // Can pick up this student
                    lastPickedStop = currentStop;
                    currentPickup++;
                } else {
                    // Cannot pick up this student without exceeding time
                    totalTime -= shortestPaths[prevIdx][currentIdx];
                    break;
                }
            }

            // Add time to return to school after the last pick-up
            totalTime += shortestPaths[stopToIndex.get(lastPickedStop)][schoolIdx];
            out.println(totalTime + " " + lastPickedStop);
        }

        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
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
                    String s = reader.readLine();
                    if (s == null)
                        return null;
                    tokenizer = new StringTokenizer(s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            String s = next();
            if (s == null)
                return -1;
            return Integer.parseInt(s);
        }

        public long nextLong() {
            String s = next();
            if (s == null)
                return -1;
            return Long.parseLong(s);
        }
    }
}

// TODO: Implementasi Graph
class Graph {
    int vertices;
    List<Edge>[] adjacency;

    @SuppressWarnings("unchecked")
    public Graph() {
        // Default constructor; actual number of vertices will be set later
        this.vertices = 0;
        adjacency = new List[1]; // Temporary initialization
    }

    @SuppressWarnings("unchecked")
    public Graph(int V) {
        this.vertices = V;
        adjacency = new List[V + 1]; // 1-based indexing
        for (int i = 0; i <= V; i++) {
            adjacency[i] = new ArrayList<>();
        }
    }

    // TODO: Implementasi tambahkan edge ke graph
    public void addEdge(int from, int to, long weight) {
        adjacency[from].add(new Edge(to, weight));
        adjacency[to].add(new Edge(from, weight)); // Since the graph is undirected
    }

    // TODO: Implementasi shortest path (bebas mengubah fungsi ini)
    public long[] dijkstra(int source) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        boolean[] visited = new boolean[vertices + 1];
        long[] dist = new long[vertices + 1];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[source] = 0;
        pq.add(new Node(source, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int u = current.id;

            if (visited[u])
                continue;
            visited[u] = true;

            for (Edge edge : adjacency[u]) {
                int v = edge.to;
                long weight = edge.weight;

                if (!visited[v] && dist[v] > dist[u] + weight) {
                    dist[v] = dist[u] + weight;
                    pq.add(new Node(v, dist[v]));
                }
            }
        }

        return dist;
    }
}

class Edge {
    int to;
    long weight;

    public Edge(int to, long weight) {
        this.to = to;
        this.weight = weight;
    }
}

class Node implements Comparable<Node> {
    int id;
    long dist;

    public Node(int id, long dist) {
        this.id = id;
        this.dist = dist;
    }

    // Nodes are compared based on their distance for the priority queue
    public int compareTo(Node other) {
        return Long.compare(this.dist, other.dist);
    }
}

