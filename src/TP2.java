import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class TP2 {
    private static InputReader in;
    private static PrintWriter out;
    private static CircularDoublyLinkedList<Kelompok> kelompoks;
    private static CircularDoublyLinkedList<Kelompok>.TeamNode teamNodeSekarang;
    private static CircularDoublyLinkedList<Kelompok>.TeamNode teamWithPenjoki;
    private static int counter = 1;

    public static void main(String[] args) {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);

        // Menentukan jumlah tim
        int M = in.nextInteger(); // Jumlah tim
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

        // Menentukan tim dengan total poin terendah untuk penempatan penjoki
        teamWithPenjoki = findPenjokiTeam(null); // Tidak ada tim yang dikecualikan
        if (teamWithPenjoki != null) {
            teamWithPenjoki.data.setPenjoki(true);
        }

        // Inisialisasi tim yang diawasi oleh Sofita
        if (M > 0) {
            teamNodeSekarang = kelompoks.getHead(); // Mulai dari tim pertama
        } else {
            teamNodeSekarang = null; // Tidak ada tim
        }

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

            else if (query.equals("M")) {
                String arah = in.next();
                if (kelompoks.size() > 0) {
                    // If Sofita is null, stay null
                    if (teamNodeSekarang == null) {
                        out.println("-1");
                        continue;
                    }
            
                    // Move to the next non-eliminated team
                    CircularDoublyLinkedList<Kelompok>.TeamNode nextTeamNode = findNextNonEliminatedTeam(teamNodeSekarang, arah);
            
                    // If there are no non-eliminated teams, Sofita remains null
                    if (nextTeamNode == null) {
                        teamNodeSekarang = null;
                        out.println("-1");
                        continue;
                    }
            
                    Kelompok nextTeam = nextTeamNode.data;
            
                    // Check if the destination team has Penjoki
                    if (nextTeam.hasPenjoki()) {
                        // Attempt to move Penjoki to another team
                        Set<CircularDoublyLinkedList<Kelompok>.TeamNode> excludeTeams = new HashSet<>();
                        excludeTeams.add(nextTeamNode); // Exclude the team where Penjoki is now (and where Sofita is moving to)
                        excludeTeams.add(teamNodeSekarang); // Exclude current team of Sofita (before moving)
                        CircularDoublyLinkedList<Kelompok>.TeamNode newPenjokiTeam = findPenjokiTeam(excludeTeams);
            
                        // Move Penjoki to new team if possible
                        if (newPenjokiTeam != null) {
                            nextTeam.setPenjoki(false);
                            newPenjokiTeam.data.setPenjoki(true);
                            teamWithPenjoki = newPenjokiTeam;
                        } else {
                            // Penjoki cannot be moved; remains in the team
                            // Penalties will be applied below
                        }
            
                        // Apply penalties regardless of whether Penjoki was moved
                        nextTeam.incrementCatchCount();
                        int catches = nextTeam.getCatchCount();
            
                        if (catches == 1) {
                            nextTeam.removeTopKPeserta(3);
                        } else if (catches == 2) {
                            nextTeam.setAllPesertaPoins(1);
                        } else if (catches == 3) {
                            // Eliminate the team
                            nextTeam.setEliminated(true);
                            kelompoks.remove(nextTeamNode);
                            nextTeam.setPenjoki(false);
            
                            // Reassign Penjoki to the lowest scoring team
                            if (kelompoks.size() > 0) {
                                CircularDoublyLinkedList<Kelompok>.TeamNode newPenjokiTeamAfterElimination = findPenjokiTeam(null);
                                if (newPenjokiTeamAfterElimination != null) {
                                    newPenjokiTeamAfterElimination.data.setPenjoki(true);
                                    teamWithPenjoki = newPenjokiTeamAfterElimination;
                                } else {
                                    teamWithPenjoki = null;
                                }
                            } else {
                                teamWithPenjoki = null;
                            }
            
                            // Reset catch count
                            nextTeam.resetCatchCount();
                        }
            
                        // Check if team has less than 7 members after penalties
                        if (!nextTeam.isEliminated() && nextTeam.getPesertas().getSize() < 7) {
                            // Eliminate the team
                            nextTeam.setEliminated(true);
                            kelompoks.remove(nextTeamNode);
            
                            // If the team had Penjoki, move Penjoki
                            if (nextTeam.hasPenjoki()) {
                                nextTeam.setPenjoki(false);
                                if (kelompoks.size() > 0) {
                                    CircularDoublyLinkedList<Kelompok>.TeamNode newPenjokiTeamAfterElimination = findPenjokiTeam(null);
                                    if (newPenjokiTeamAfterElimination != null) {
                                        newPenjokiTeamAfterElimination.data.setPenjoki(true);
                                        teamWithPenjoki = newPenjokiTeamAfterElimination;
                                    } else {
                                        teamWithPenjoki = null;
                                    }
                                } else {
                                    teamWithPenjoki = null;
                                }
                            }
                        }
                    }
            
                    // Move Sofita to the new team
                    teamNodeSekarang = nextTeamNode;
            
                    // If the team was eliminated, move Sofita to the team with highest total points
                    if (nextTeam.isEliminated()) {
                        if (kelompoks.size() > 0) {
                            teamNodeSekarang = findHighestTeam();
                        } else {
                            teamNodeSekarang = null;
                        }
                    }
            
                    // Output the team ID or -1 if Sofita is not observing any team
                    if (teamNodeSekarang != null) {
                        out.println(teamNodeSekarang.data.getTeamId());
                    } else {
                        out.println("-1");
                    }
                } else {
                    out.println("-1");
                }
            }

            else if (query.equals("J")) {
                String arah = in.next();
                if (teamWithPenjoki != null && kelompoks.size() > 0) {
                    CircularDoublyLinkedList<Kelompok>.TeamNode targetTeam = null;

                    if (arah.equals("L")) {
                        targetTeam = teamWithPenjoki.prev;
                    } else if (arah.equals("R")) {
                        targetTeam = teamWithPenjoki.next;
                    } else {
                        // Arah tidak valid
                        out.println("-1");
                        continue;
                    }

                    // Cek apakah tim tujuan sedang diawasi oleh Sofita
                    if (targetTeam == teamNodeSekarang) {
                        // Penjoki tetap di tim saat ini
                        // Tidak ada perubahan pada teamWithPenjoki
                    } else {
                        // Penjoki berpindah ke tim tujuan
                        // Update flag penjoki di tim lama dan tim baru
                        teamWithPenjoki.data.setPenjoki(false);
                        targetTeam.data.setPenjoki(true);
                        teamWithPenjoki = targetTeam;
                    }

                    // Cetak ID tim penjoki sekarang
                    out.println(teamWithPenjoki.data.getTeamId());
                } else {
                    // Tidak ada tim atau penjoki
                    out.println("-1");
                }
            }

            else if (query.equals("U")) {
                // Implementasi Query U
                if (teamNodeSekarang != null) {
                    Kelompok currentTeam = teamNodeSekarang.data;
                    List<Peserta> pesertaList = currentTeam.getPesertas().getAllPeserta();

                    Set<Integer> uniquePoints = new HashSet<>();
                    for (Peserta p : pesertaList) {
                        uniquePoints.add(p.getPoins());
                    }
                    out.println(uniquePoints.size());
                } else {
                    // Sofita tidak mengawasi tim manapun
                    out.println("-1");
                }
            } 

            else if (query.equals("B")) {
                // Implementasi Query B
            }
            else if (query.equals("T")) {
                int senderId = in.nextInteger();
                int receiverId = in.nextInteger();
                int points = in.nextInteger();
                // Implementasi Query T
            } 
            else if (query.equals("G")) {
                String position = in.next();
                // Implementasi Query G
            } 
            else if (query.equals("V")) {
                int id1 = in.nextInteger();
                int id2 = in.nextInteger();
                int teamId = in.nextInteger();
                int result = in.nextInteger();
                // Implementasi Query V
            } 
            else if (query.equals("E")) {
                int minPoints = in.nextInteger();
                // Implementasi Query E
            } 
            else if (query.equals("R")) {
                // Implementasi Query R
            } 
        }

        out.close(); // Jangan lupa menutup PrintWriter
    }

    // Kelas Peserta
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

        public void setPoins(int p) {
            this.poins = p;
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

    // Kelas Kelompok
    static class Kelompok {
        private int teamId;
        private PesertaBST pesertas;
        private boolean hasPenjoki;
        private int catchCount;
        private boolean isEliminated;

        public Kelompok(int teamId) {
            this.teamId = teamId;
            this.pesertas = new PesertaBST();
            this.hasPenjoki = false;
            this.catchCount = 0;
        }

        public int getTeamId() {
            return teamId;
        }

        public PesertaBST getPesertas() {
            return pesertas;
        }

        public boolean hasPenjoki() {
            return hasPenjoki;
        }

        public void setPenjoki(boolean hasPenjoki) {
            this.hasPenjoki = hasPenjoki;
        }

        public int getCatchCount() {
            return catchCount;
        }

        public void incrementCatchCount() {
            this.catchCount++;
        }

        public void resetCatchCount() {
            this.catchCount = 0;
        }

        public void addPeserta(Peserta p) {
            pesertas.insert(p);
        }

        public int getTotalPoints() {
            return pesertas.getTotalPoints();
        }

        public boolean isEliminated() {
            return isEliminated;
        }
    
        public void setEliminated(boolean eliminated) {
            isEliminated = eliminated;
        }

        @Override
        public String toString() {
            return "Team{id=" + teamId + ", pesertas=" + pesertas.getSize() + "}";
        }

        // Menghapus k peserta dengan poin terbesar
        public void removeTopKPeserta(int k) {
            List<Peserta> topKPeserta = pesertas.getTopKPeserta(k);
            for(Peserta p : topKPeserta) {
                pesertas.removePeserta(p.getId());
            }
        }

        // Mengubah semua peserta' poin menjadi nilai tertentu
        public void setAllPesertaPoins(int newPoins) {
            pesertas.setAllPoins(newPoins);
        }
    }

    // AVL Tree untuk Peserta
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
            
            // Update tinggi Node
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

            // Balance factor
            int balance = getBalance(node);

            // Balancing tree
            // Case 1: Left Left
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
            inorderRec(root); // memulai traversal dari akar
        }

        // Rekursif untuk traversal
        private void inorderRec(NodePeserta node) {
            if (node != null) {
                inorderRec(node.left);
                System.out.println(node.peserta);
                inorderRec(node.right);
            }
        }

        // Mengumpulkan semua peserta
        public List<Peserta> getAllPeserta() {
            List<Peserta> allPeserta = new ArrayList<>();
            inorderCollect(root, allPeserta);
            return allPeserta;
        }

        private void inorderCollect(NodePeserta node, List<Peserta> list) {
            if(node != null) {
                inorderCollect(node.left, list);
                list.add(node.peserta);
                inorderCollect(node.right, list);
            }
        }

        // Mengambil k peserta dengan poin terbesar
        public List<Peserta> getTopKPeserta(int k) {
            List<Peserta> allPeserta = getAllPeserta();
            allPeserta.sort((a, b) -> b.getPoins() - a.getPoins());
            return allPeserta.subList(0, Math.min(k, allPeserta.size()));
        }

        // Menghapus peserta berdasarkan ID
        public void removePeserta(int id) {
            root = removeRec(root, id);
        }

        private NodePeserta removeRec(NodePeserta node, int id) {
            if (node == null) return null;
            if (id < node.peserta.getId()) {
                node.left = removeRec(node.left, id);
            } else if (id > node.peserta.getId()) {
                node.right = removeRec(node.right, id);
            } else {
                // Node to be deleted
                if (node.left == null || node.right == null) {
                    NodePeserta temp = null;
                    if (node.left != null) temp = node.left;
                    else temp = node.right;

                    if (temp == null) { // No child
                        temp = node;
                        node = null;
                    }
                    else { // One child
                        node = temp;
                    }
                }
                else {
                    // Node dengan dua anak: Get inorder successor (smallest in the right subtree)
                    NodePeserta temp = minValueNode(node.right);
                    node.peserta = temp.peserta;
                    node.right = removeRec(node.right, temp.peserta.getId());
                }
            }

            // If the tree had only one node
            if (node == null) return node;

            // Update height
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

            // Get balance factor
            int balance = getBalance(node);

            // Balance the tree
            // Left Left
            if (balance > 1 && getBalance(node.left) >= 0) {
                return rightRotate(node);
            }

            // Left Right
            if (balance > 1 && getBalance(node.left) < 0) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            // Right Right
            if (balance < -1 && getBalance(node.right) <= 0) {
                return leftRotate(node);
            }

            // Right Left
            if (balance < -1 && getBalance(node.right) > 0) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            return node;
        }

        private NodePeserta minValueNode(NodePeserta node) {
            NodePeserta current = node;
            while (current.left != null) {
                current = current.left;
            }
            return current;
        }

        // Mengubah semua peserta' poin menjadi nilai tertentu
        public void setAllPoins(int newPoins) {
            setAllPoinsRec(root, newPoins);
        }

        private void setAllPoinsRec(NodePeserta node, int newPoins) {
            if(node != null) {
                setAllPoinsRec(node.left, newPoins);
                totalPoints -= node.peserta.getPoins();
                node.peserta.setPoins(newPoins);
                totalPoints += newPoins;
                setAllPoinsRec(node.right, newPoins);
            }
        }

        // Mendapatkan peserta dengan poin terbesar
        public Peserta getMaxPeserta() {
            if (root == null) return null;
            NodePeserta current = root;
            while (current.right != null) {
                current = current.right;
            }
            return current.peserta;
        }

        // Kelas Node AVL Tree
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

    // Kelas Circular Doubly Linked List (CDLL)
    static class CircularDoublyLinkedList<T> {
        // Kelas inner non-static agar dapat mengakses tipe parameter T
        class TeamNode {
            T data;
            TeamNode prev, next;

            public TeamNode(T data) {
                this.data = data;
                prev = next = null;
            }
        }

        private TeamNode kepala;
        private TeamNode ekor; // Menyimpan referensi ke ekor untuk akses O(1)
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

        public TeamNode getHead() {
            return kepala;
        }

        public int size() {
            return size;
        }

        // Menghapus sebuah node dari CDLL
        public boolean remove(TeamNode node) {
            if (kepala == null || node == null) return false;

            if (kepala == node && ekor == node) {
                kepala = ekor = null;
            }
            else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                if (kepala == node) {
                    kepala = node.next;
                }
                if (ekor == node) {
                    ekor = node.prev;
                }
            }
            size--;
            return true;
        }

        public void display() {
            if (kepala == null) {
                System.out.println("List Kosong");
                return;
            }
            TeamNode current = kepala;
            for(int i = 0; i < size; i++) {
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

    // Kelas InputReader untuk efisiensi pembacaan input
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

    private static CircularDoublyLinkedList<Kelompok>.TeamNode findPenjokiTeam(Set<CircularDoublyLinkedList<Kelompok>.TeamNode> exclude) {
        if (kelompoks.size() == 0) return null;
        
        CircularDoublyLinkedList<Kelompok>.TeamNode minTeam = null;
        int minPoints = Integer.MAX_VALUE;
        CircularDoublyLinkedList<Kelompok>.TeamNode current = kelompoks.getHead();
        
        for (int i = 0; i < kelompoks.size(); i++) {
            if ((exclude == null || !exclude.contains(current)) && !current.data.isEliminated()) {
                Kelompok team = current.data;
                if (team.getTotalPoints() < minPoints) {
                    minPoints = team.getTotalPoints();
                    minTeam = current;
                }
            }
            current = current.next;
        }
        return minTeam;
    }

    private static CircularDoublyLinkedList<Kelompok>.TeamNode findHighestTeam() {
        if (kelompoks.size() == 0) return null;
    
        CircularDoublyLinkedList<Kelompok>.TeamNode maxTeam = null;
        int maxPoints = Integer.MIN_VALUE;
        CircularDoublyLinkedList<Kelompok>.TeamNode current = kelompoks.getHead();
    
        for (int i = 0; i < kelompoks.size(); i++) {
            if (!current.data.isEliminated()) {
                Kelompok team = current.data;
                if (team.getTotalPoints() > maxPoints) {
                    maxPoints = team.getTotalPoints();
                    maxTeam = current;
                }
            }
            current = current.next;
        }
        return maxTeam;
    }

    private static CircularDoublyLinkedList<Kelompok>.TeamNode findNextNonEliminatedTeam(CircularDoublyLinkedList<Kelompok>.TeamNode startNode, String direction) {
        CircularDoublyLinkedList<Kelompok>.TeamNode currentNode = startNode;
        do {
            if (direction.equals("L")) {
                currentNode = currentNode.prev;
            } else if (direction.equals("R")) {
                currentNode = currentNode.next;
            } else {
                return null;
            }
        } while (currentNode.data.isEliminated() && currentNode != startNode);
        if (currentNode.data.isEliminated()) {
            return null; // Semua tim telah dieliminasi
        } else {
            return currentNode;
        }
    }
    
}
