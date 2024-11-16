// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.InputStreamReader;
// import java.io.PrintWriter;
// import java.util.StringTokenizer;

// public class TP2 {
//     private static InputReader in;
//     private static PrintWriter out;
//     public static int indeksKelompokSekarang;
//     public static CircularDoublyLinkedList<Kelompok> kelompoks;
//     public static int counter = 1;

//     public static void main(String[] args) {
//         in = new InputReader(System.in);
//         out = new PrintWriter(System.out);
        
//         // Menentukan jumlah tim
//         int M = in.nextInteger(); // Jumlah query yang akan dijalankan
//         kelompoks = new CircularDoublyLinkedList<>();

//         // Menentukan jumlah peserta untuk masing-masing tim
//         int[] jumlahPesertaDiTim = new int[M];
//         for (int i = 0; i < M; i++) {
//             jumlahPesertaDiTim[i] = in.nextInteger();
//         }

//         // Menentukan total peserta
//         int jumlahPeserta = 0;
//         for (int num : jumlahPesertaDiTim) {
//             jumlahPeserta += num;
//         }

//         // Menentukan awalan poin untuk setiap peserta
//         int[] poin = new int[jumlahPeserta];
//         for (int i = 0; i < jumlahPeserta; i++) {
//             poin[i] = in.nextInteger();
//         }

//         int indexPoin = 0;
//         for (int i = 0; i < M; i++) {
//             Kelompok kelompok = new Kelompok(i + 1);
//             for (int j = 0; j < jumlahPesertaDiTim[i]; j++) {
//                 Peserta peserta = new Peserta(counter++, poin[indexPoin++]);
//                 kelompok.addPeserta(peserta);
//             }
//             kelompoks.addLast(kelompok);
//         }

//         // Inisialisasi tim yang diawasi oleh Sofita
//         if (M > 0) {
//             indeksKelompokSekarang = 0; // Mulai dari tim pertama (index 0)
//         } else {
//             indeksKelompokSekarang = -1; // Tidak ada tim
//         }

//         // Membaca Jumlah Query
//         int Q = in.nextInteger();

//         for (int i = 0; i < Q; i++) {
//             String query = in.next();

//             if (query.equals("A")) {
//                 int jumlahPesertaBaru = in.nextInteger();
//                 if (indeksKelompokSekarang >= 0 && indeksKelompokSekarang < kelompoks.size()) {
//                     Kelompok currentTeam = kelompoks.get(indeksKelompokSekarang);
//                     for (int p = 0; p < jumlahPesertaBaru; p++) {
//                         Peserta newParticipant = new Peserta(counter++, 3);
//                         currentTeam.addPeserta(newParticipant);
//                     }
//                     out.println(currentTeam.getPesertas().getSize());
//                 } else {
//                     out.println("-1");
//                 }
//             } 

//             else if (query.equals("B")) {
//                 // implement the logic query
//             }
//             else if (query.equals("M")) {
//                 String direction = in.next();
//                 // Implement the logic for query "M"
//             } 
//             else if (query.equals("T")) {
//                 int senderId = in.nextInteger();
//                 int receiverId = in.nextInteger();
//                 int points = in.nextInteger();
//                 // Implement the logic for query "T"
//             } 
//             else if (query.equals("G")) {
//                 String position = in.next();
//                 // Implement the logic for query "G"
//             } 
//             else if (query.equals("V")) {
//                 int id1 = in.nextInteger();
//                 int id2 = in.nextInteger();
//                 int teamId = in.nextInteger();
//                 int result = in.nextInteger();
//                 // Implement the logic for query "V"
//             } 
//             else if (query.equals("E")) {
//                 int minPoints = in.nextInteger();
//                 // Implement the logic for query "E"
//             } 
//             else if (query.equals("U")) {
//                 // Implement the logic for query "U"
//             } 
//             else if (query.equals("R")) {
//                 // Implement the logic for query "R"
//             } 
//             else if (query.equals("J")) {
//                 // Implement the logic for query "J"
//             }
//         }

//         out.close();
//     }

//     static class Peserta {
//         private int id;
//         private int poins;

//         public Peserta(int id, int poins) {
//             this.id = id;
//             this.poins = poins;
//         }

//         public int getId() {
//             return id;
//         }

//         public int getPoins() {
//             return poins;
//         }

//         public void addPoins(int p) {
//             this.poins += p;
//         }

//         public void subtractPoins(int p) {
//             this.poins -= p;
//         }

//         @Override
//         public String toString() {
//             return "Peserta{id=" + id + ", poins=" + poins + "}";
//         }
//     }

//     static class Kelompok {
//         private int teamId;
//         private PesertaBST pesertas;

//         public Kelompok(int teamId) {
//             this.teamId = teamId;
//             this.pesertas = new PesertaBST();
//         }

//         public int getTeamId() {
//             return teamId;
//         }

//         public PesertaBST getPesertas() {
//             return pesertas;
//         }

//         public void addPeserta(Peserta p) {
//             pesertas.insert(p);
//         }

//         public int getTotalPoints() {
//             return pesertas.getTotalPoints();
//         }

//         @Override
//         public String toString() {
//             return "Team{id=" + teamId + ", pesertas=" + pesertas.getSize() + "}";
//         }
//     }

//     // BST untuk peserta
//     static class PesertaBST {
//         private NodePeserta root;
//         private int size;
//         private int totalPoints;

//         public PesertaBST() {
//             this.root = null;
//             this.size = 0;
//             this.totalPoints = 0;
//         }

//         // Menambah peserta ke BST Tree
//         public void insert(Peserta p) {
//             root = insertRec(root, p);
//         }

//         private NodePeserta insertRec(NodePeserta root, Peserta p) {
//             if (root == null) {
//                 root = new NodePeserta(p);
//                 size++;
//                 totalPoints += p.getPoins();
//                 return root;
//             }
//             if (p.getId() < root.peserta.getId()) {
//                 root.left = insertRec(root.left, p);
//             } else if (p.getId() > root.peserta.getId()) {
//                 root.right = insertRec(root.right, p);
//             }
//             // Jika ID sama, tidak ditambahkan (ID unik)
//             return root; 
//         }

//         // Mencari peserta menurut ID
//         public Peserta search(int id) {
//             return searchRec(root, id);
//         }

//         private Peserta searchRec(NodePeserta root, int id) {
//             if (root == null) return null;
//             if (id == root.peserta.getId()) return root.peserta;
//             if (id < root.peserta.getId()) return searchRec(root.left, id);
//             return searchRec(root.right, id);
//         }

//         // Untuk mendapat ukuran BST
//         public int getSize() {
//             return size;
//         }

//         // Mengambil total poin semua peserta dalam kelompok
//         public int getTotalPoints() {
//             return totalPoints;
//         }

//         // Traversal inorder untuk mendapatkan semua data peserta dalam urutan tertentu (id)
//         public void inorderTraversal() {
//             inorderRec(root); // memlai traversal dari akar
//         }

//         // rekursif untuk start traversal dari akar
//         private void inorderRec(NodePeserta root) {
//             if (root != null) {
//                 inorderRec(root.left);
//                 System.out.println(root.peserta);
//                 inorderRec(root.right);
//             }
//         }

//         // Node BST, nyimpen peserta(id, poin)
//         private class NodePeserta {
//             Peserta peserta;
//             NodePeserta left, right;

//             public NodePeserta(Peserta peserta) {
//                 this.peserta = peserta;
//                 left = right = null;
//             }
//         }

//     }

//     static class CircularDoublyLinkedList<T> {
//         private TeamNode<T> kepala;
//         private int size;

//         public CircularDoublyLinkedList() {
//             kepala = null;
//             size = 0;
//         }

//         public void addLast(T data) {
//             TeamNode<T> newNode = new TeamNode<>(data);
//             if (kepala == null) {
//                 kepala = newNode;
//                 kepala.next = kepala;
//                 kepala.prev = kepala;
//             }
//             size++;
//         }

//         public T get(int index) {
//             if (index < 0 || index >= size) return null;
//             TeamNode<T> current = kepala;
//             for(int i = 0; i < index; i++) {
//                 current = current.next;
//             }
//             return current.data;
//         }

//         public int size() {
//             return size;
//         }

//         public void display() {
//             if (kepala == null) {
//                 System.out.println("List Kosong");
//                 return;
//             }
//             TeamNode<T> current = kepala;
//             for(int i = 0; i < size; i++) {
//                 System.out.print(current.data + " ");
//                 current = current.next;
//             }
//             System.out.println();
//         }

//         // Kelas Node untuk CDLL
//         private class TeamNode<T> {
//             T data;
//             TeamNode<T> prev, next;

//             public TeamNode(T data) {
//                 this.data = data;
//                 prev = next = null;
//             }
//         }
        
//     }

//     static class InputReader {
//         public BufferedReader reader;
//         public StringTokenizer tokenizer;

//         public InputReader(InputStream stream) {
//             reader = new BufferedReader(new InputStreamReader(stream), 32768);
//             tokenizer = null;
//         }

//         public String next() {
//             while (tokenizer == null || !tokenizer.hasMoreTokens()) {
//                 try {
//                     tokenizer = new StringTokenizer(reader.readLine());
//                 } catch (IOException e) {
//                     throw new RuntimeException(e);
//                 }
//             }
//             return tokenizer.nextToken();
//         }

//         public int nextInteger() {
//             return Integer.parseInt(next());
//         }
//     }
// }
