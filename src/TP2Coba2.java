import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class TP2Coba2 {
    private static InputReader in;
    private static PrintWriter out;
    private static CircularDoublyLinkedList<Kelompok> kelompoks;
    private static CircularDoublyLinkedList<Kelompok>.TeamNode teamNodeSekarang;
    private static int counter = 1;

    public static void main(String[] args) {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);
        
        // Menentukan jumlah tim
        int M = in.nextInteger(); // Jumlah query yang akan dijalankan
        kelompoks = new CircularDoublyLinkedList<>();

        // Menentukan jumlah peserta untuk masing-masing tim
        int[] jumlahPesertaDiTim = new int[M];
        for (int i = 0; i < M; i++) {
            jumlahPesertaDiTim[i] = in.nextInteger();
        }

        // Menentukan total peserta
        int jumlahPeserta = 0;
        for (int num : jumlahPesertaDiTim) {
            jumlahPeserta += num;
        }

        // Menentukan awalan poin untuk setiap peserta
        int[] poin = new int[jumlahPeserta];
        for (int i = 0; i < jumlahPeserta; i++) {
            poin[i] = in.nextInteger();
        }

        int indexPoin = 0;
        for (int i = 0; i < M; i++) {
            Kelompok kelompok = new Kelompok(i + 1);
            for (int j = 0; j < jumlahPesertaDiTim[i]; j++) {
                Peserta peserta = new Peserta(counter++, poin[indexPoin++]);
                kelompok.addPeserta(peserta);
            }
            kelompoks.addLast(kelompok);
        }

        // Inisialisasi tim yang diawasi oleh Sofita
        if (M > 0) {
            teamNodeSekarang = kelompoks.getHead(); // Mulai dari tim pertama (index 0)
        } else {
            teamNodeSekarang = null; // Tidak ada tim
        }

        int nextTeamId = M + 1;

        // Membaca Jumlah Query
        int Q = in.nextInteger();

        for (int i = 0; i < Q; i++) {
            String query = in.next();

            if (query.equals("A")) {
                int jumlahPesertaBaru = in.nextInteger();
                if (teamNodeSekarang != null) {
                    Kelompok currentTeam = teamNodeSekarang.data;
                    for (int p = 0; p < jumlahPesertaBaru; p++) {
                        Peserta newParticipant = new Peserta(counter++, 3);
                        currentTeam.addPeserta(newParticipant);
                    }
                    out.println(currentTeam.getPesertas().getSize());
                } else {
                    out.println("-1");
                }
            } 

            else if (query.equals("B")) {
                // implement the logic query
            }
            else if (query.equals("M")) {
                String direction = in.next();
                // Implement the logic for query "M"
            } 
            else if (query.equals("T")) {
                int senderId = in.nextInteger();
                int receiverId = in.nextInteger();
                int points = in.nextInteger();
                // Implement the logic for query "T"
            } 
            else if (query.equals("G")) {
                String position = in.next();

            // Create a new team with the next available team ID
            Kelompok newTeam = new Kelompok(nextTeamId++);

            // Add 7 new participants with initial points of 1
            for (int j = 0; j < 7; j++) {
                Peserta newParticipant = new Peserta(counter++, 1);
                newTeam.addPeserta(newParticipant);
            }

            if (teamNodeSekarang == null) {
                // If no teams exist, add the new team and set it as the current team
                kelompoks.addLast(newTeam);
                teamNodeSekarang = kelompoks.getHead();
            } else {
                if (position.equals("L")) {
                    // Insert the new team to the left of the current team
                    kelompoks.addBefore(teamNodeSekarang, newTeam);
                } else if (position.equals("R")) {
                    // Insert the new team to the right of the current team
                    kelompoks.addAfter(teamNodeSekarang, newTeam);
                }
            }

            // Output the ID of the newly created team
            out.println(newTeam.getTeamId());
            } 
            else if (query.equals("V")) {
                int id1 = in.nextInteger();
                int id2 = in.nextInteger();
                int teamId = in.nextInteger();
                int result = in.nextInteger();
                // Implement the logic for query "V"
            } 
            else if (query.equals("E")) {
                int minPoints = in.nextInteger();
                // Implement the logic for query "E"
            } 
            else if (query.equals("U")) {
                // Implement the logic for query "U"
            } 
            else if (query.equals("R")) {
                // Implement the logic for query "R"
            } 
            else if (query.equals("J")) {
                // Implement the logic for query "J"
            }
        }

        out.close();
    }

    static class Peserta {
        private int id;
        private int poins;

        public Peserta(int id, int poins) {
            this.id = id;
            this.poins = poins;
        }

        public int getId() {
            return id;
        }

        public int getPoins() {
            return poins;
        }

        public void addPoins(int p) {
            this.poins += p;
        }

        public void subtractPoins(int p) {
            this.poins -= p;
        }

        @Override
        public String toString() {
            return "Peserta{id=" + id + ", poins=" + poins + "}";
        }
    }

    static class Kelompok {
        private int teamId;
        private PesertaBST pesertas;

        public Kelompok(int teamId) {
            this.teamId = teamId;
            this.pesertas = new PesertaBST();
        }

        public int getTeamId() {
            return teamId;
        }

        public PesertaBST getPesertas() {
            return pesertas;
        }

        public void addPeserta(Peserta p) {
            pesertas.insert(p);
        }

        public int getTotalPoints() {
            return pesertas.getTotalPoints();
        }

        @Override
        public String toString() {
            return "Team{id=" + teamId + ", pesertas=" + pesertas.getSize() + "}";
        }
    }

    // AVL untuk peserta
    static class PesertaBST {
        private NodePeserta root;
        private int size;
        private int totalPoints;

        public PesertaBST() {
            this.root = null;
            this.size = 0;
            this.totalPoints = 0;
        }

        // Menambah peserta ke AVL Tree
        public void insert(Peserta p) {
            root = insertRec(root, p);
        }

        private NodePeserta insertRec(NodePeserta node, Peserta p) {
            if (node == null) {
                node = new NodePeserta(p);
                size++;
                totalPoints += p.getPoins();
                return node;
            }
            if (p.getId() < node.peserta.getId()) {
                node.left = insertRec(node.left, p);
            } else if (p.getId() > node.peserta.getId()) {
                node.right = insertRec(node.right, p);
            } else {
                // Jika ID sama, tidak ditambahkan (ID unik)
                return node; 
            }
            
            // Tinggi Node
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

            // balance factor
            int balance = getBalance(node);

            // Balancing tree LeftLeft(LL)
            if (balance > 1 && p.getId() < node.left.peserta.getId()) {
                return rightRotate(node);
            }

            // Case 2: Right Right
            if (balance < -1 && p.getId() > node.right.peserta.getId()) {
                return leftRotate(node);
            }

            // Case 3: Left Right
            if (balance > 1 && p.getId() > node.left.peserta.getId()) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            // Case 4: Right Left
            if (balance < -1 && p.getId() < node.right.peserta.getId()) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            return node;
        }

        private int getHeight(NodePeserta node) {
            return node == null ? 0 : node.height;
        }

        private int getBalance(NodePeserta node) {
            return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
        }

        private NodePeserta rightRotate(NodePeserta y) {
            NodePeserta x = y.left;
            NodePeserta T2 = x.right;

            // Perform rotation
            x.right = y;
            y.left = T2;

            // Update heights
            y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
            x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

            // Return new root
            return x;
        }

        private NodePeserta leftRotate(NodePeserta x) {
            NodePeserta y = x.right;
            NodePeserta T2 = y.left;

            // Perform rotation
            y.left = x;
            x.right = T2;

            // Update heights
            x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
            y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;

            // Return new root
            return y;
        }

        // Mencari peserta menurut ID
        public Peserta search(int id) {
            return searchRec(root, id);
        }

        private Peserta searchRec(NodePeserta node, int id) {
            if (node == null) return null;
            if (id == node.peserta.getId()) return node.peserta;
            if (id < node.peserta.getId()) return searchRec(node.left, id);
            return searchRec(node.right, id);
        }

        // Untuk mendapat ukuran BST
        public int getSize() {
            return size;
        }

        // Mengambil total poin semua peserta dalam kelompok
        public int getTotalPoints() {
            return totalPoints;
        }

        // Traversal inorder untuk mendapatkan semua data peserta dalam urutan tertentu (id)
        public void inorderTraversal() {
            inorderRec(root); // memlai traversal dari akar
        }

        // rekursif untuk start traversal dari akar
        private void inorderRec(NodePeserta node) {
            if (root != null) {
                inorderRec(root.left);
                System.out.println(root.peserta);
                inorderRec(root.right);
            }
        }

        // Node BST, nyimpen peserta(id, poin)
        private class NodePeserta {
            Peserta peserta;
            NodePeserta left, right;
            int height;

            public NodePeserta(Peserta peserta) {
                this.peserta = peserta;
                left = right = null;
                height = 1;
            }
        }
    }

    static class CircularDoublyLinkedList<T> {
        class TeamNode {
            T data;
            TeamNode prev, next;
    
            public TeamNode(T data) {
                this.data = data;
                prev = next = null;
            }
        }
    
        private TeamNode kepala;
        private TeamNode ekor;
        private int size;
    
        public CircularDoublyLinkedList() {
            kepala = ekor = null;
            size = 0;
        }
    
        public void addLast(T data) {
            TeamNode newNode = new TeamNode(data);
            if (kepala == null) {
                kepala = ekor = newNode;
                kepala.next = kepala;
                kepala.prev = kepala;
            } else {
                ekor.next = newNode;
                newNode.prev = ekor;
                newNode.next = kepala;
                kepala.prev = newNode;
                ekor = newNode;
            }
            size++;
        }
    
        public void addAfter(TeamNode node, T data) {
            if (node == null) {
                return;
            }
            TeamNode newNode = new TeamNode(data);
    
            newNode.next = node.next;
            newNode.prev = node;
            node.next.prev = newNode;
            node.next = newNode;
    
            if (node == ekor) {
                ekor = newNode;
            }
            size++;
        }
    
        public void addBefore(TeamNode node, T data) {
            if (node == null) {
                return;
            }
            TeamNode newNode = new TeamNode(data);
    
            newNode.prev = node.prev;
            newNode.next = node;
            node.prev.next = newNode;
            node.prev = newNode;
    
            if (node == kepala) {
                kepala = newNode;
            }
            size++;
        }
    
        public TeamNode getHead() {
            return kepala;
        }
    
        public int size() {
            return size;
        }
    
        public void display() {
            if (kepala == null) {
                System.out.println("List Kosong");
                return;
            }
            TeamNode current = kepala;
            for (int i = 0; i < size; i++) {
                System.out.print(current.data + " ");
                current = current.next;
            }
            System.out.println();
        }
    
        // Getter untuk ekor jika diperlukan
        public TeamNode getTail() {
            return ekor;
        }
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
                    String line = reader.readLine();
                    if (line == null) {
                        return null;
                    }
                    tokenizer = new StringTokenizer(line);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInteger() {
            return Integer.parseInt(next());
        }
    }
}