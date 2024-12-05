import java.io.*;
import java.util.*;

public class TP3CobaM {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);

        // Input jumlah kota (V) dan jumlah seluruh jalan (E)
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

            // Energy = Kekuatan
            if (query.equals("R")) {
                long kekuatan = in.nextLong();
                out.println(graph.maksimalKotaYangDapatDitempuh(kekuatan));

                // Query F untuk tujuan mencari jarak terpendek
            } else if (query.equals("F")) {
                int targetKota = in.nextInt();
                out.println(graph.kotaJarakTerpendek(targetKota));

                // M untuk ID dan password (password rumah sofita)
            } else if (query.equals("M")) {
                int id = in.nextInt();
                int password = in.nextInt();
                out.println(graph.minPasswordCombinations(id, digitPasswordRumah, password));

                // Query J untuk total jarak terpendek
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
                    String temp = reader.readLine();
                    if (temp == null) return null;
                    tokenizer = new StringTokenizer(temp);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            String temp = next();
            if (temp == null) return -1;
            return Integer.parseInt(temp);
        }

        public long nextLong() {
            String temp = next();
            if (temp == null) return -1;
            return Long.parseLong(temp);
        }
    }

    static class Graph {
        private int V;
        private List<Edge>[] adjList;
        private String currentPassword;
        private int currentCity; // Menambahkan variabel untuk melacak lokasi saat ini

        public Graph(int V) {
            this.V = V;
            adjList = new ArrayList[V + 1];
            for (int i = 1; i <= V; i++) {
                adjList[i] = new ArrayList<>();
            }
            currentPassword = "0000"; // Initialize the current password
            currentCity = 1; // Inisialisasi lokasi awal ke kota 1
        }

        public void addEdge(int u, int v, long weight) {
            adjList[u].add(new Edge(u, v, weight));
            adjList[v].add(new Edge(v, u, weight));
        }

        public long maksimalKotaYangDapatDitempuh(long kekuatan) {
            // Implementasikan algoritma BFS untuk mencari jumlah kota maksimum yang dapat dikunjungi
            boolean[] dikunjungi = new boolean[V + 1];

            Queue<Integer> queue = new LinkedList<>();
            int start = currentCity; // Mulai dari kota saat ini
            dikunjungi[start] = true;
            queue.offer(start);
            int count = 0; // Start from 0 to count cities other than starting city

            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (Edge edge : adjList[u]) {
                    int v = edge.v;
                    long weight = edge.weight;
                    if (weight <= kekuatan && !dikunjungi[v]) {
                        dikunjungi[v] = true;
                        queue.offer(v);
                        count++;
                    }
                }
            }

            // Kembalikan jumlah kota yang dikunjungi, tidak termasuk kota awal
            if (count > 0) {
                return count;
            } else {
                // Jika tidak ada kota lain yang dapat dikunjungi
                return -1;
            }
        }

        public long kotaJarakTerpendek(int targetKota) {
            long[] jarak = new long[V + 1];
            Arrays.fill(jarak, Long.MAX_VALUE);
            jarak[currentCity] = 0;

            PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> Long.compare(a.weight, b.weight));
            pq.offer(new Edge(currentCity, currentCity, 0));

            while (!pq.isEmpty()) {
                Edge current = pq.poll();
                int kotaSaatIni = current.v;
                long jarakSaatIni = current.weight;

                if (kotaSaatIni == targetKota) {
                    return jarakSaatIni;
                }

                if (jarakSaatIni > jarak[kotaSaatIni]) {
                    continue;
                }

                for (Edge tetangga : adjList[kotaSaatIni]) {
                    int v = tetangga.v;
                    long w = tetangga.weight;
                    long jarakBaru = jarakSaatIni + w;

                    if (jarakBaru < jarak[v]) {
                        jarak[v] = jarakBaru;
                        pq.offer(new Edge(currentCity, v, jarakBaru));
                    }
                }
            }
            return -1;
        }

        public int minPasswordCombinations(int id, int[] digitPasswordRumah, int password) {
            // Set lokasi saat ini ke id yang diberikan
            currentCity = id;

            // Convert the target password to a 4-digit string with leading zeros
            String targetPasswordStr = String.format("%04d", password);

            // If the current password is already the target password
            if (currentPassword.equals(targetPasswordStr)) {
                return 0;
            }

            // BFS setup
            Queue<String> queue = new LinkedList<>();
            Map<String, Integer> visited = new HashMap<>();

            queue.offer(currentPassword);
            visited.put(currentPassword, 0);

            while (!queue.isEmpty()) {
                String current = queue.poll();
                int steps = visited.get(current);

                for (int num : digitPasswordRumah) {
                    String numStr = String.format("%04d", num);

                    // Compute the next password using the addition rule
                    String nextPassword = addPasswords(current, numStr);

                    // If the target password is reached
                    if (nextPassword.equals(targetPasswordStr)) {
                        currentPassword = targetPasswordStr; // Update the current password
                        return steps + 1;
                    }

                    // If this password hasn't been visited yet
                    if (!visited.containsKey(nextPassword)) {
                        visited.put(nextPassword, steps + 1);
                        queue.offer(nextPassword);
                    }
                }
            }

            // If the target password cannot be reached
            return -1;
        }

        // Helper method to perform digit-wise addition of passwords
        private String addPasswords(String p1, String p2) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                int digit1 = p1.charAt(i) - '0';
                int digit2 = p2.charAt(i) - '0';
                int sum = digit1 + digit2;
                int lastDigit = sum % 10;
                result.append(lastDigit);
            }
            return result.toString();
        }

        public long totalShortestDistance(int kotaRespawnSofita) {
            List<EdgeMST> allEdges = new ArrayList<>();

            // Kumpulkan semua jalan unik
            for (int u = 1; u <= V; u++) {
                for (Edge edge : adjList[u]) {
                    int v = edge.v;
                    long weight = edge.weight;
                    if (u < v) { // Pastikan setiap jalan hanya dihitung sekali
                        allEdges.add(new EdgeMST(u, v, weight));
                    }
                }
            }

            // Pisahkan jalan yang terhubung langsung dengan kotaRespawnSofita
            List<EdgeMST> connected = new ArrayList<>();
            List<EdgeMST> others = new ArrayList<>();

            for (EdgeMST edge : allEdges) {
                if (edge.u == kotaRespawnSofita || edge.v == kotaRespawnSofita) {
                    connected.add(edge);
                } else {
                    others.add(edge);
                }
            }

            // Urutkan jalan yang terhubung langsung dengan kotaRespawnSofita berdasarkan bobot ascending
            connected.sort(Comparator.comparingLong(e -> e.weight));

            // Urutkan jalan lainnya berdasarkan bobot ascending
            others.sort(Comparator.comparingLong(e -> e.weight));

            // Gabungkan jalan: jalan yang terhubung langsung dengan kotaRespawnSofita terlebih dahulu
            List<EdgeMST> sortedEdges = new ArrayList<>();
            sortedEdges.addAll(connected);
            sortedEdges.addAll(others);

            // Implementasikan Union-Find
            int[] parent = new int[V + 1];
            for (int i = 1; i <= V; i++) {
                parent[i] = i;
            }

            long totalSum = 0;
            int edgesUsed = 0;

            for (EdgeMST edge : sortedEdges) {
                int u = edge.u;
                int v = edge.v;
                long w = edge.weight;

                int pu = find(parent, u);
                int pv = find(parent, v);

                if (pu != pv) {
                    parent[pu] = pv;
                    totalSum += w;
                    edgesUsed++;
                    if (edgesUsed == V - 1) {
                        break;
                    }
                }
            }

            // Periksa apakah semua kota terhubung
            int root = find(parent, 1);
            for (int i = 2; i <= V; i++) {
                if (find(parent, i) != root) {
                    return -1;
                }
            }

            return totalSum;
        }

        private int find(int[] parent, int x) {
            if (parent[x] != x) {
                parent[x] = find(parent, parent[x]);
            }
            return parent[x];
        }

        // Kelas khusus untuk MST dengan atribut u, v, weight
        static class EdgeMST {
            int u;
            int v;
            long weight;

            public EdgeMST(int u, int v, long weight) {
                this.u = u;
                this.v = v;
                this.weight = weight;
            }
        }
    }

    // Kelas Edge yang diperbarui dengan atribut u dan v
    static class Edge {
        int u;
        int v;
        long weight;

        public Edge(int u, int v, long weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
    }
}
