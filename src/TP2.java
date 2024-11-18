import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

public class TP2 {
    private static InputReader in;
    private static PrintWriter out;
    private static CircularDoublyLinkedList<Kelompok> kelompoks;
    private static CircularDoublyLinkedList<Kelompok>.TeamNode teamNodeSekarang;
    private static CircularDoublyLinkedList<Kelompok>.TeamNode penjokiNode = null;
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
        // Inisialisasi tim yang diawasi oleh Sofita
        if (M > 0) {
            teamNodeSekarang = kelompoks.getHead(); // Sofita starts supervising team 1

            // Find the team with the lowest total points
            CircularDoublyLinkedList<Kelompok>.TeamNode tempNode = kelompoks.getHead();
            Kelompok timTerbawah = null;
            penjokiNode = null; // Reset penjokiNode

            for (int i = 0; i < kelompoks.size(); i++) {
                if (tempNode != teamNodeSekarang) {
                    if (timTerbawah == null
                            || tempNode.data.getTotalPoinKelompok() < timTerbawah.getTotalPoinKelompok()) {
                        timTerbawah = tempNode.data;
                        penjokiNode = tempNode; // Update penjokiNode to the new lowest team
                    }
                }
                tempNode = tempNode.next;
            }

            // Place the 'Penjoki' on the team with the lowest total points
            if (penjokiNode != null) {
                penjokiNode.data.setPenjoki(true);
            }
        } else {
            teamNodeSekarang = null; // No teams exist
        }

        int idTimSelanjutnya = M + 1;

        // Membaca Jumlah Query
        int Q = in.nextInteger();

        for (int i = 0; i < Q; i++) {
            String query = in.next();

            if (query.equals("A")) {
                int jumlahPesertaBaru = in.nextInteger();
                if (teamNodeSekarang != null) {
                    Kelompok timSekarang = teamNodeSekarang.data;
                    for (int p = 0; p < jumlahPesertaBaru; p++) {
                        Peserta PartisipanBaru = new Peserta(counter++, 3);
                        timSekarang.addPeserta(PartisipanBaru);
                    }
                    out.println(timSekarang.getPesertas().getSize());
                } else {
                    out.println("-1");
                }
            }

            else if (query.equals("B")) {
                // implement the logic query
            } else if (query.equals("M")) {
                String direction = in.next();
                if (teamNodeSekarang != null) {
                    if (direction.equals("L")) {
                        teamNodeSekarang = teamNodeSekarang.prev;
                    } else if (direction.equals("R")) {
                        teamNodeSekarang = teamNodeSekarang.next;
                    }
                    Kelompok timSekarang = teamNodeSekarang.data;
                    if (timSekarang.hasPenjoki()) {
                        // Remove Penjoki from current team
                        timSekarang.setPenjoki(false);

                        // Find the team with the lowest total points not supervised by Sofita
                        CircularDoublyLinkedList<Kelompok>.TeamNode tempNode = kelompoks.getHead();
                        CircularDoublyLinkedList<Kelompok>.TeamNode nodeTimTerendah = null;
                        int totalPoinTerendah = Integer.MAX_VALUE;

                        do {
                            if (tempNode != teamNodeSekarang && tempNode.data != null) {
                                int poinTim = tempNode.data.getTotalPoinKelompok();
                                if (poinTim < totalPoinTerendah) {
                                    totalPoinTerendah = poinTim;
                                    nodeTimTerendah = tempNode;
                                }
                            }
                            tempNode = tempNode.next;
                        } while (tempNode != kelompoks.getHead());

                        // Move Penjoki to the new team
                        if (nodeTimTerendah != null) {
                            penjokiNode = nodeTimTerendah;
                            penjokiNode.data.setPenjoki(true);
                        } else {
                            // No other team available for Penjoki
                            penjokiNode = null;
                        }

                        // Apply penalties to the current team
                        timSekarang.incrementPenjokiTertangkap();
                        int pointerTertangkap = timSekarang.getPenjokiTertangkap();
                        if (pointerTertangkap == 1) {
                            timSekarang.getPesertas().menghapusPesertaPoinTinggi(3);
                        } else if (pointerTertangkap == 2) {
                            timSekarang.getPesertas().setSemuaPoinJadiSatu();
                        } else if (pointerTertangkap >= 3) {
                            // Eliminate the team
                            CircularDoublyLinkedList<Kelompok>.TeamNode toRemove = teamNodeSekarang;
                            // Move Sofita to the team with highest total points
                            teamNodeSekarang = getTimDenganPoinTertinggi();
                            kelompoks.remove(toRemove);
                            if (kelompoks.size() == 0) {
                                teamNodeSekarang = null;
                            }
                        }

                        // After penalties, check if team needs to be eliminated due to low participant
                        // count
                        if (timSekarang.getPesertas().getSize() < 7) {
                            // Remove the team from the list
                            CircularDoublyLinkedList<Kelompok>.TeamNode toRemove = teamNodeSekarang;
                            // Move Sofita to the team with highest total points
                            teamNodeSekarang = getTimDenganPoinTertinggi();
                            kelompoks.remove(toRemove);
                            if (kelompoks.size() == 0) {
                                teamNodeSekarang = null;
                            }
                        }
                    }

                    // Output the team ID or -1 if Sofita is not observing any team
                    if (teamNodeSekarang != null) {
                        out.println(teamNodeSekarang.data.getTeamId());
                    } else {
                        out.println("-1");
                    }
                }
            } else if (query.equals("T")) {
                int idPesertaPengirim = in.nextInteger();
                int idPesertaPenerima = in.nextInteger();
                int jumlah_poin = in.nextInteger();
            
                // Validasi Sofita
                if (teamNodeSekarang == null) {
                    out.println("-1");
                    continue;
                }
            
                Kelompok timSekarang = teamNodeSekarang.data;
            
                // Cari peserta pengirim dan penerima
                Peserta pengirim = timSekarang.getPesertas().cari(idPesertaPengirim);
                Peserta penerima = timSekarang.getPesertas().cari(idPesertaPenerima);
            
                // Validasi peserta
                if (pengirim == null || penerima == null) {
                    out.println("-1");
                    continue;
                }
            
                // Validasi jumlah poin
                if (jumlah_poin >= pengirim.getPoin()) {
                    out.println("-1");
                    continue;
                }
            
                // Hapus pengirim dan penerima dari AVL
                timSekarang.getPesertas().remove(idPesertaPengirim);
                timSekarang.getPesertas().remove(idPesertaPenerima);
            
                // Update poin pengirim dan penerima
                pengirim.subtractPoins(jumlah_poin);
                penerima.addPoins(jumlah_poin);
            
                // Masukkan kembali ke AVL
                timSekarang.getPesertas().insert(pengirim);
                timSekarang.getPesertas().insert(penerima);
            
                // Cetak poin pengirim dan penerima
                out.println(pengirim.getPoin() + " " + penerima.getPoin());
            } else if (query.equals("G")) {
                String position = in.next();

                // Create a new team with the next available team ID
                Kelompok newTeam = new Kelompok(idTimSelanjutnya++);

                // Add 7 new partisipan with initial points of 1
                for (int j = 0; j < 7; j++) {
                    Peserta PartisipanBaru = new Peserta(counter++, 1);
                    newTeam.addPeserta(PartisipanBaru);
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
            } else if (query.equals("V")) {
                int id_peserta1 = in.nextInteger();
                int id_peserta2 = in.nextInteger();
                int teamId = in.nextInteger();
                int result = in.nextInteger();
            
                // Step 1: Validate Participants and Teams
                if (teamNodeSekarang == null) {
                    out.println("-1");
                    continue;
                }
            
                Kelompok teamSofita = teamNodeSekarang.data;
                Peserta player1 = teamSofita.getPesertas().cari(id_peserta1);
                if (player1 == null) {
                    out.println("-1");
                    continue;
                }
            
                // Find the team with ID teamId
                CircularDoublyLinkedList<Kelompok>.TeamNode nodeTimLawan = kelompoks.getHead();
                boolean timDidapati = false;
                do {
                    if (nodeTimLawan.data.getTeamId() == teamId) {
                        timDidapati = true;
                        break;
                    }
                    nodeTimLawan = nodeTimLawan.next;
                } while (nodeTimLawan != kelompoks.getHead());
            
                if (!timDidapati) {
                    out.println("-1");
                    continue;
                }
            
                Kelompok timLawan_V = nodeTimLawan.data;
                Peserta player2 = timLawan_V.getPesertas().cari(id_peserta2);
                if (player2 == null) {
                    out.println("-1");
                    continue;
                }
            
                // Step 2: Remove Participants from AVL Trees
                teamSofita.getPesertas().remove(id_peserta1);
                timLawan_V.getPesertas().remove(id_peserta2);
            
                // Step 3: Update Points Based on Result
                if (result == 0) {
                    player1.addPoins(1);
                    player2.addPoins(1);
                } else if (result == 1) {
                    player1.addPoins(3);
                    player2.subtractPoins(3);
                    if (player2.getPoin() < 0) player2.poins = 0; // Ensure non-negative points
                } else if (result == -1) {
                    player2.addPoins(3);
                    player1.subtractPoins(3);
                    if (player1.getPoin() < 0) player1.poins = 0; // Ensure non-negative points
                }
            
                // Step 4: Reinsert Participants into AVL Trees
                teamSofita.getPesertas().insert(player1);
                timLawan_V.getPesertas().insert(player2);
            
                // Update total points in teams
                teamSofita.updateTotalPoints();
                timLawan_V.updateTotalPoints();
            
                // Step 5: Check for Team Eliminations
                List<CircularDoublyLinkedList<Kelompok>.TeamNode> timAkanDieliminasi = new ArrayList<>();
                boolean timSofitaTereliminasi = false;
                boolean timLawanTereliminasi = false;
            
                // Check team supervised by Sofita
                if (teamSofita.getPesertas().getSize() < 6) {
                    timAkanDieliminasi.add(teamNodeSekarang);
                    timSofitaTereliminasi = true;
                }
            
                // Check opponent team
                if (timLawan_V.getPesertas().getSize() < 6) {
                    timAkanDieliminasi.add(nodeTimLawan);
                    if (penjokiNode == nodeTimLawan) {
                        timLawanTereliminasi = true;
                    }
                }
            
                // Eliminate teams
                for (CircularDoublyLinkedList<Kelompok>.TeamNode nodeToRemove : timAkanDieliminasi) {
                    // Check if the team is being supervised by Sofita
                    if (teamNodeSekarang == nodeToRemove) {
                        timSofitaTereliminasi = true;
                        teamNodeSekarang = null; // Will be updated later
                    }
            
                    // Check if the team has the Penjoki
                    if (penjokiNode == nodeToRemove) {
                        timLawanTereliminasi = true;
                        penjokiNode.data.setPenjoki(false); // Remove Penjoki from current team
                        penjokiNode = null; // Will be updated later
                    }
            
                    // Remove the team from the list
                    kelompoks.remove(nodeToRemove);
                }
            
                // If Sofita's team was eliminated, move her to the team with highest total points
                if (timSofitaTereliminasi && kelompoks.size() > 0) {
                    teamNodeSekarang = getTimDenganPoinTertinggi();
            
                    // Check if Sofita meets the Penjoki
                    if (teamNodeSekarang == penjokiNode && penjokiNode != null) {
                        // Handle Penjoki movement and penalties as in "M" query
                        // Remove Penjoki from current team
                        teamNodeSekarang.data.setPenjoki(false);
            
                        // Find the team with the lowest total points not supervised by Sofita
                        CircularDoublyLinkedList<Kelompok>.TeamNode lowestTeamNode = getTimDenganPoinTerendah(teamNodeSekarang);
            
                        // Move Penjoki to the new team
                        if (lowestTeamNode != null) {
                            penjokiNode = lowestTeamNode;
                            penjokiNode.data.setPenjoki(true);
                        } else {
                            penjokiNode = null;
                        }
            
                        // Apply penalties to the current team
                        teamNodeSekarang.data.incrementPenjokiTertangkap();
                        int caughtCount = teamNodeSekarang.data.getPenjokiTertangkap();
                        if (caughtCount == 1) {
                            teamNodeSekarang.data.getPesertas().menghapusPesertaPoinTinggi(3);
                        } else if (caughtCount == 2) {
                            teamNodeSekarang.data.getPesertas().setSemuaPoinJadiSatu();
                        } else if (caughtCount >= 3) {
                            // Eliminate the team
                            kelompoks.remove(teamNodeSekarang);
                            if (kelompoks.size() > 0) {
                                teamNodeSekarang = getTimDenganPoinTertinggi();
                            } else {
                                teamNodeSekarang = null;
                            }
                        }
                    }
                }
            
                // If Penjoki's team was eliminated, move him to the team with lowest total points
                if (timLawanTereliminasi && kelompoks.size() > 0) {
                    CircularDoublyLinkedList<Kelompok>.TeamNode lowestTeamNode = getTimDenganPoinTerendah(teamNodeSekarang);
                    if (lowestTeamNode != null) {
                        penjokiNode = lowestTeamNode;
                        penjokiNode.data.setPenjoki(true);
                    } else {
                        penjokiNode = null;
                    }
                }
            
                // Step 6: Output the Result
                if (result == 0) {
                    out.println(player1.getPoin() + " " + player2.getPoin());
                } else if (result == 1) {
                    out.println(player1.getPoin());
                } else if (result == -1) {
                    out.println(player2.getPoin());
                }
            } else if (query.equals("E")) {
                int minPoints = in.nextInteger();
                int teamsEliminated = 0;

                // List to hold the team nodes to be removed
                List<CircularDoublyLinkedList<Kelompok>.TeamNode> timAkanDihapus = new ArrayList<>();

                // First, check which teams need to be eliminated
                CircularDoublyLinkedList<Kelompok>.TeamNode nodeSekarang = kelompoks.getHead();
                if (nodeSekarang != null) {
                    do {
                        Kelompok timSekarang = nodeSekarang.data;
                        if (timSekarang.getTotalPoinKelompok() < minPoints) {
                            timAkanDihapus.add(nodeSekarang);
                        }
                        nodeSekarang = nodeSekarang.next;
                    } while (nodeSekarang != kelompoks.getHead());
                }

                // Flags to check if Sofita or Penjoki need to be moved
                boolean timSofitaTerleliminasi = false;
                boolean timPenjokiTereliminasi = false;
                boolean penjokiPindahKarnaPaksaan = false; // To track if Penjoki moved due to conflict

                // Now, eliminate the teams
                for (CircularDoublyLinkedList<Kelompok>.TeamNode nodeToRemove : timAkanDihapus) {
                    // Check if the team is being supervised by Sofita
                    if (teamNodeSekarang == nodeToRemove) {
                        timSofitaTerleliminasi = true;
                        teamNodeSekarang = null; // Will be updated later
                    }

                    // Check if the team has the Penjoki
                    if (penjokiNode == nodeToRemove) {
                        timPenjokiTereliminasi = true;
                        penjokiNode.data.setPenjoki(false); // Remove Penjoki from current team
                        penjokiNode = null; // Will be updated later
                    }

                    // Remove the team from the list
                    kelompoks.remove(nodeToRemove);
                    teamsEliminated++;
                }

                // If Sofita's team was eliminated, move her to the team with highest total
                // points
                if (timSofitaTerleliminasi && kelompoks.size() > 0) {
                    teamNodeSekarang = getTimDenganPoinTertinggi();
                }

                // If Penjoki's team was eliminated, move him to the team with lowest total
                // points
                if (timPenjokiTereliminasi && kelompoks.size() > 0 && !penjokiPindahKarnaPaksaan) {
                    // Exclude the team being supervised by Sofita
                    CircularDoublyLinkedList<Kelompok>.TeamNode nodeTimTerendah = getTimDenganPoinTerendah(
                            teamNodeSekarang);
                    if (nodeTimTerendah != null) {
                        penjokiNode = nodeTimTerendah;
                        penjokiNode.data.setPenjoki(true);
                    }
                }
                // Output the number of teams eliminated
                out.println(teamsEliminated);
            } else if (query.equals("U")) {
                if (teamNodeSekarang != null) {
                    Kelompok timSekarang = teamNodeSekarang.data;
                    // Get all partisipan
                    List<Peserta> partisipan = timSekarang.getPesertas().getSemuaPartisipan();
                    // Use a HashSet to store unique point values
                    HashSet<Integer> poinUnik = new HashSet<>();
                    for (Peserta p : partisipan) {
                        poinUnik.add(p.getPoin());
                    }
                    out.println(poinUnik.size());
                } else {
                    out.println("-1"); // Sofita is not supervising any team
                }
            } else if (query.equals("R")) {
                // Implement the logic for query "R"
            } else if (query.equals("J")) {
                String direction = in.next();
                if (penjokiNode != null) {
                    CircularDoublyLinkedList<Kelompok>.TeamNode targetNode = null;
                    if (direction.equals("L")) {
                        targetNode = penjokiNode.prev;
                    } else if (direction.equals("R")) {
                        targetNode = penjokiNode.next;
                    }

                    // Check if the target team is not being observed by Sofita and is not
                    // eliminated
                    if (targetNode != null && targetNode != teamNodeSekarang && targetNode.data != null) {
                        // Move Penjoki to the new team
                        penjokiNode.data.setPenjoki(false); // Remove Penjoki from current team
                        penjokiNode = targetNode;
                        penjokiNode.data.setPenjoki(true); // Place Penjoki on new team
                    }
                    // Output the ID of the team where Penjoki is currently helping
                    out.println(penjokiNode.data.getTeamId());
                } else {
                    out.println("-1"); // Penjoki is not helping any team
                }
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

        public int getPoin() {
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

    static class Kelompok implements Comparable<Kelompok> {
        private int teamId;
        private PesertaBST pesertas;
        private boolean hasPenjoki;
        private int penjokiCaughtCount;

        public Kelompok(int teamId) {
            this.teamId = teamId;
            this.pesertas = new PesertaBST();
            this.hasPenjoki = false;
            this.penjokiCaughtCount = 0;
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

        public int getTotalPoinKelompok() {
            return pesertas.getTotalPoinKelompok();
        }

        public boolean hasPenjoki() {
            return hasPenjoki;
        }

        public void setPenjoki(boolean hasPenjoki) {
            this.hasPenjoki = hasPenjoki;
        }

        public int getPenjokiTertangkap() {
            return penjokiCaughtCount;
        }

        public void incrementPenjokiTertangkap() {
            this.penjokiCaughtCount++;
        }

        // query V
        public void updateTotalPoints() {
            this.pesertas.updateTotalPoints();
        }
        

        @Override
        public String toString() {
            return "Team{id=" + teamId + ", pesertas=" + pesertas.getSize() + "}";
        }

        @Override
        public int compareTo(Kelompok other) {
            // Compare total points (higher is better)
            if (this.getTotalPoinKelompok() > other.getTotalPoinKelompok()) {
                return -1; // 'this' is higher
            } else if (this.getTotalPoinKelompok() < other.getTotalPoinKelompok()) {
                return 1; // 'this' is lower
            } else {
                // Total points are equal, compare number of members (fewer is better)
                if (this.getPesertas().getSize() < other.getPesertas().getSize()) {
                    return -1; // 'this' has fewer members, so it's higher
                } else if (this.getPesertas().getSize() > other.getPesertas().getSize()) {
                    return 1; // 'this' has more members, so it's lower
                } else {
                    // Number of members is equal, compare team IDs (lower ID is better)
                    if (this.getTeamId() < other.getTeamId()) {
                        return -1; // 'this' has lower ID, so it's higher
                    } else if (this.getTeamId() > other.getTeamId()) {
                        return 1; // 'this' has higher ID, so it's lower
                    } else {
                        return 0; // All attributes are equal
                    }
                }
            }
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
            root = rekursifAVL(root, p);
        }

        private NodePeserta rekursifAVL(NodePeserta node, Peserta p) {
            if (node == null) {
                node = new NodePeserta(p);
                size++;
                totalPoints += p.getPoin();
                return node;
            }
            if (p.getId() < node.peserta.getId()) {
                node.left = rekursifAVL(node.left, p);
            } else if (p.getId() > node.peserta.getId()) {
                node.right = rekursifAVL(node.right, p);
            } else {
                // Jika ID sama, tidak ditambahkan (ID unik)
                return node;
            }

            // Tinggi Node
            node.height = 1 + Math.max(getTinggiTree(node.left), getTinggiTree(node.right));

            // Seimbang factor
            int Seimbang = getTreeSeimbang(node);

            // Balancing tree LeftLeft(LL)
            if (Seimbang > 1 && p.getId() < node.left.peserta.getId()) {
                return rotasiTreeKeKanan(node);
            }

            // Case 2: Right Right
            if (Seimbang < -1 && p.getId() > node.right.peserta.getId()) {
                return rotasiTreeKeKiri(node);
            }

            // Case 3: Left Right
            if (Seimbang > 1 && p.getId() > node.left.peserta.getId()) {
                node.left = rotasiTreeKeKiri(node.left);
                return rotasiTreeKeKanan(node);
            }

            // Case 4: Right Left
            if (Seimbang < -1 && p.getId() < node.right.peserta.getId()) {
                node.right = rotasiTreeKeKanan(node.right);
                return rotasiTreeKeKiri(node);
            }

            return node;
        }

        private int getTinggiTree(NodePeserta node) {
            return node == null ? 0 : node.height;
        }

        private int getTreeSeimbang(NodePeserta node) {
            return node == null ? 0 : getTinggiTree(node.left) - getTinggiTree(node.right);
        }

        private NodePeserta rotasiTreeKeKanan(NodePeserta y) {
            NodePeserta x = y.left;
            NodePeserta T2 = x.right;

            // Perform rotation
            x.right = y;
            y.left = T2;

            // Update heights
            y.height = Math.max(getTinggiTree(y.left), getTinggiTree(y.right)) + 1;
            x.height = Math.max(getTinggiTree(x.left), getTinggiTree(x.right)) + 1;

            // Return new root
            return x;
        }

        private NodePeserta rotasiTreeKeKiri(NodePeserta x) {
            NodePeserta y = x.right;
            NodePeserta T2 = y.left;

            // Perform rotation
            y.left = x;
            x.right = T2;

            // Update heights
            x.height = Math.max(getTinggiTree(x.left), getTinggiTree(x.right)) + 1;
            y.height = Math.max(getTinggiTree(y.left), getTinggiTree(y.right)) + 1;

            // Return new root
            return y;
        }

        // Mencari peserta menurut ID
        public Peserta cari(int id) {
            return rekursifUntukMencari(root, id);
        }

        private Peserta rekursifUntukMencari(NodePeserta node, int id) {
            if (node == null)
                return null;
            if (id == node.peserta.getId())
                return node.peserta;
            if (id < node.peserta.getId())
                return rekursifUntukMencari(node.left, id);
            return rekursifUntukMencari(node.right, id);
        }

        // Untuk mendapat ukuran BST
        public int getSize() {
            return size;
        }

        // Mengambil total poin semua peserta dalam kelompok
        public int getTotalPoinKelompok() {
            return totalPoints;
        }

        // Traversal inorder untuk mendapatkan semua data peserta dalam urutan tertentu
        // (id)
        public void inorderTraversal() {
            inOrderRekursif(root); // memlai traversal dari akar
        }

        // rekursif untuk start traversal dari akar
        private void inOrderRekursif(NodePeserta node) {
            if (root != null) {
                inOrderRekursif(root.left);
                System.out.println(root.peserta);
                inOrderRekursif(root.right);
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

        public void menghapusPesertaPoinTinggi(int k) {
            List<Peserta> partisipan = getSemuaPartisipan();
            // Sort partisipan by points in descending order using insertion sort
            insertionSort(partisipan);
            for (int i = 0; i < k && i < partisipan.size(); i++) {
                Peserta p = partisipan.get(i);
                remove(p.getId());
            }
        }

        // Custom insertion sort algorithm
        private void insertionSort(List<Peserta> list) {
            for (int i = 1; i < list.size(); i++) {
                Peserta key = list.get(i);
                int j = i - 1;

                // Move elements of list[0..i-1], that are less than key, to one position ahead
                while (j >= 0 && list.get(j).getPoin() < key.getPoin()) {
                    list.set(j + 1, list.get(j));
                    j = j - 1;
                }
                list.set(j + 1, key);
            }
        }

        public void setSemuaPoinJadiSatu() {
            setSemuaPoinJadiSatuRec(root);
            // Update totalPoints
            totalPoints = size * 1;
        }

        private void setSemuaPoinJadiSatuRec(NodePeserta node) {
            if (node != null) {
                setSemuaPoinJadiSatuRec(node.left);
                totalPoints -= node.peserta.getPoin() - 1; // Adjust totalPoints
                node.peserta.poins = 1;
                setSemuaPoinJadiSatuRec(node.right);
            }
        }

        public List<Peserta> getSemuaPartisipan() {
            List<Peserta> partisipan = new ArrayList<>();
            getSemuaPartisipanRec(root, partisipan);
            return partisipan;
        }

        private void getSemuaPartisipanRec(NodePeserta node, List<Peserta> partisipan) {
            if (node != null) {
                getSemuaPartisipanRec(node.left, partisipan);
                partisipan.add(node.peserta);
                getSemuaPartisipanRec(node.right, partisipan);
            }
        }

        public void remove(int id) {
            root = removeRekursif(root, id);
        }

        private NodePeserta removeRekursif(NodePeserta node, int id) {
            if (node == null) {
                return node;
            }
            if (id < node.peserta.getId()) {
                node.left = removeRekursif(node.left, id);
            } else if (id > node.peserta.getId()) {
                node.right = removeRekursif(node.right, id);
            } else {
                // Node to be deleted
                totalPoints -= node.peserta.getPoin();
                size--;
                if (node.left == null || node.right == null) {
                    NodePeserta temp = (node.left != null) ? node.left : node.right;
                    if (temp == null) {
                        node = null;
                    } else {
                        node = temp;
                    }
                } else {
                    // Node with two children
                    NodePeserta temp = minValueNode(node.right);
                    node.peserta = temp.peserta;
                    node.right = removeRekursif(node.right, temp.peserta.getId());
                }
            }
            if (node == null) {
                return node;
            }

            // Update height
            node.height = 1 + Math.max(getTinggiTree(node.left), getTinggiTree(node.right));

            // Seimbang the node
            int Seimbang = getTreeSeimbang(node);

            // LL
            if (Seimbang > 1 && getTreeSeimbang(node.left) >= 0) {
                return rotasiTreeKeKanan(node);
            }
            // LR
            if (Seimbang > 1 && getTreeSeimbang(node.left) < 0) {
                node.left = rotasiTreeKeKiri(node.left);
                return rotasiTreeKeKanan(node);
            }
            // RR
            if (Seimbang < -1 && getTreeSeimbang(node.right) <= 0) {
                return rotasiTreeKeKiri(node);
            }
            // RL
            if (Seimbang < -1 && getTreeSeimbang(node.right) > 0) {
                node.right = rotasiTreeKeKanan(node.right);
                return rotasiTreeKeKiri(node);
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

        public void updateTotalPoints() {
            totalPoints = calculateTotalPoints(root);
        }
        
        private int calculateTotalPoints(NodePeserta node) {
            if (node == null) return 0;
            return node.peserta.getPoin() + calculateTotalPoints(node.left) + calculateTotalPoints(node.right);
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

        public void remove(TeamNode node) {
            if (node == null || size == 0) {
                return;
            }
            if (size == 1 && node == kepala) {
                kepala = ekor = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                if (node == kepala) {
                    kepala = node.next;
                }
                if (node == ekor) {
                    ekor = node.prev;
                }
            }
            size--;
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

    private static CircularDoublyLinkedList<Kelompok>.TeamNode getTimDenganPoinTertinggi() {
        if (kelompoks.size() == 0)
            return null;
        CircularDoublyLinkedList<Kelompok>.TeamNode tempNode = kelompoks.getHead();
        CircularDoublyLinkedList<Kelompok>.TeamNode highestTeamNode = tempNode;
        int highestTotalPoints = tempNode.data.getTotalPoinKelompok();
        tempNode = tempNode.next;

        while (tempNode != kelompoks.getHead()) {
            int poinTim = tempNode.data.getTotalPoinKelompok();
            if (poinTim > highestTotalPoints) {
                highestTotalPoints = poinTim;
                highestTeamNode = tempNode;
            }
            tempNode = tempNode.next;
        }
        return highestTeamNode;
    }

    private static CircularDoublyLinkedList<Kelompok>.TeamNode getTimDenganPoinTerendah(
            CircularDoublyLinkedList<Kelompok>.TeamNode excludeNode) {
        if (kelompoks.size() == 0)
            return null;
        CircularDoublyLinkedList<Kelompok>.TeamNode tempNode = kelompoks.getHead();
        CircularDoublyLinkedList<Kelompok>.TeamNode nodeTimTerendah = null;
        int totalPoinTerendah = Integer.MAX_VALUE;

        do {
            if (tempNode != excludeNode) {
                int poinTim = tempNode.data.getTotalPoinKelompok();
                if (poinTim < totalPoinTerendah) {
                    totalPoinTerendah = poinTim;
                    nodeTimTerendah = tempNode;
                }
            }
            tempNode = tempNode.next;
        } while (tempNode != kelompoks.getHead());

        return nodeTimTerendah;
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