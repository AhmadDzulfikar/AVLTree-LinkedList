import java.io.*;
import java.util.*;

public class TP3 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);

        // ini buat inputan jumlah kota (V) dan jumlah seluruh jalan (E)
        int V = in.nextInt();
        int E = in.nextInt();
        
        Graph graph = new Graph(V);

        // Input jalan antar kota (id kota, id kota tujuan, jarak)
        for (int i = 0; i < E; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            long weight = in.nextLong();
            graph.addEdge(u, v, weight);
        }

        // Input banyak angka untuk membuat passwordnya
        int P = in.nextInt();
        int[] digitPasswordRumah = new int[P];
        for (int i = 0; i < P; i++) {
            digitPasswordRumah[i] = in.nextInt();
        }

        // Input jumlah query
        int Q = in.nextInt();
        while (Q-- > 0) {
            String query = in.next();

            // Enerrgy = Kekuatan
            if (query.equals("R")) {
                long kekuatan = in.nextLong();
                out.println(graph.maksimalKotaYangDapatDitempuh(kekuatan));

            // query F untuk tujuan mencari jarak terpendek (pergi ke kota-kota rekan bisnisnya si sofita)
            } else if (query.equals("F")) {
                int targetKota = in.nextInt();
                out.println(graph.kotaJarakTerpendek(targetKota));

            // M untuk ID dan password (password rumah sofita) --> (buat buka password)
            } else if (query.equals("M")) {
                int id = in.nextInt();
                int password = in.nextInt();
                out.println(graph.minPasswordCombinations(id, digitPasswordRumah, password));

            // query J untuk total jarak terpendek
            } else if (query.equals("J")) {
                int kotaRespawnSofita = in.nextInt();
                out.println(graph.totalShortestDistance(kotaRespawnSofita));
            }
        }

        out.close();
    }

    static class InputReader {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream));
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

    static class Graph {
        private int V;
        private List<Edge>[] adjList;

        public Graph(int V) {
            this.V = V;
            adjList = new ArrayList[V + 1];
            for (int i = 1; i <= V; i++) {
                adjList[i] = new ArrayList<>();
            }
        }

        public void addEdge(int u, int v, long weight) {
            adjList[u].add(new Edge(v, weight));
            adjList[v].add(new Edge(u, weight));
        }

        public long maksimalKotaYangDapatDitempuh(long kekuatan) {
            // Implementasikan algoritma BFS/DFS untuk mencari jumlah kota maksimum yang dapat dikunjungi
            boolean[] dikunjungi = new boolean[V + 1];

            Queue<Integer> queue = new LinkedList<>();
            int start = 1; // Mulai dari kota pertama
            dikunjungi[start] = true;
            queue.offer(start);
            int count = 0;

            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (Edge edge : adjList[u]) {
                    if (edge.weight <= kekuatan && !dikunjungi[edge.to]) {
                        dikunjungi[edge.to] = true;
                        queue.offer(edge.to);
                        count++;
                    }
                }
            }
            return count > 0 ? count : -1; // Ganti dengan implementasi
        }

        public long kotaJarakTerpendek(int targetKota) {
            // Implementasikan algoritma Dijkstra atau BFS untuk mencari jarak terpendek ke kota tujuan
            return -1; // Ganti dengan implementasi
        }

        public int minPasswordCombinations(int id, int[] digitPasswordRumah, int password) {
            // Implementasikan algoritma untuk mencari jumlah kombinasi untuk membuka password
            return -1; // Ganti dengan implementasi
        }

        public long totalShortestDistance(int kotaRespawnSofita) {
            // Implementasikan algoritma untuk mencari total jarak terpendek yang menyambungkan seluruh kota
            return -1; // Ganti dengan implementasi
        }
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
