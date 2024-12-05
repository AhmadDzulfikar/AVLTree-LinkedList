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

        public Graph(int V) {
            this.V = V;
            adjList = new ArrayList[V + 1];
            for (int i = 1; i <= V; i++) {
                adjList[i] = new ArrayList<>();
            }
            currentPassword = "0000"; // Initialize the current password
        }

        public void addEdge(int u, int v, long weight) {
            adjList[u].add(new Edge(v, weight));
            adjList[v].add(new Edge(u, weight));
        }

        public long maksimalKotaYangDapatDitempuh(long kekuatan) {
            // Implementasikan algoritma BFS untuk mencari jumlah kota maksimum yang dapat dikunjungi
            boolean[] dikunjungi = new boolean[V + 1];

            Queue<Integer> queue = new LinkedList<>();
            int start = 1; // Mulai dari kota pertama
            dikunjungi[start] = true;
            queue.offer(start);
            int count = 0; // Start from 1 since we have visited the starting city

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
            return count > 0 ? count : -1;
        }

        public long kotaJarakTerpendek(int targetKota) {
            long[] jarak = new long[V + 1];
            Arrays.fill(jarak, Long.MAX_VALUE);
            jarak[1] = 0;

            PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> Long.compare(a.weight, b.weight));
            pq.offer(new Edge(1, 0));

            while (!pq.isEmpty()) {
                Edge current = pq.poll();
                int kotaSaatIni = current.to;
                long jarakSaatIni = current.weight;

                if (kotaSaatIni == targetKota) {
                    return jarakSaatIni;
                }

                if (jarakSaatIni > jarak[kotaSaatIni]) {
                    continue;
                }

                for (Edge tetangga : adjList[kotaSaatIni]) {
                    long jarakBaru = jarakSaatIni + tetangga.weight;

                    if (jarakBaru < jarak[tetangga.to]) {
                        jarak[tetangga.to] = jarakBaru;
                        pq.offer(new Edge(tetangga.to, jarakBaru));
                    }
                }
            }
            return -1;
        }

        public int minPasswordCombinations(int id, int[] digitPasswordRumah, int password) {
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
            // Implementasikan algoritma untuk mencari total jarak terpendek yang menyambungkan seluruh kota
            // Since the problem description is not complete for this query, returning -1
            return -1;
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
