// SOURCE:
// https://rosettacode.org/wiki/Visualize_a_tree#Java

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

            // Nemuin tim dengan tim dengan point terkecil
            CircularDoublyLinkedList<Kelompok>.TeamNode tempNode = kelompoks.getHead();
            Kelompok timTerbawah = null;
            penjokiNode = null; // Reset penjokiNode

            for (int i = 0; i < kelompoks.size(); i++) {
                if (tempNode != teamNodeSekarang) {
                    if (timTerbawah == null
                            || tempNode.data.getTotalPoinKelompok() < timTerbawah.getTotalPoinKelompok()) {
                        timTerbawah = tempNode.data;
                        penjokiNode = tempNode; // Update penjokiNode ke lowest team yang baru
                    }
                }
                tempNode = tempNode.next;
            }

            // Letakkan penjoki ke tim dengan point paling kecil
            if (penjokiNode != null) {
                penjokiNode.data.setPenjoki(true);
            }
        } else {
            teamNodeSekarang = null; // gaada tim tersedia 
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
                String extremeBound = in.next();
                if (teamNodeSekarang == null) {
                    out.println("-1");
                    continue;
                }
                Kelompok timSekarang = teamNodeSekarang.data;
                List<Peserta> partisipan = timSekarang.getPesertas().getSemuaPartisipan();
                if (partisipan.size() == 0) {
                    out.println("0");
                    continue;
                }
                // Mengambil semua point peserta trus diurutin pakee sort
                int[] pointYangDikumpulin = new int[partisipan.size()];
                for (int ii = 0; ii < partisipan.size(); ii++) {
                    pointYangDikumpulin[ii] = partisipan.get(ii).getPoin();
                }
                // Insertion sort, KELAS
                for (int ii = 1; ii < pointYangDikumpulin.length; ii++) {
                    int key = pointYangDikumpulin[ii];
                    int jj = ii - 1;
                    while (jj >= 0 && pointYangDikumpulin[jj] > key) {
                        pointYangDikumpulin[jj + 1] = pointYangDikumpulin[jj];
                        jj = jj - 1;
                    }
                    pointYangDikumpulin[jj + 1] = key;
                }
                // RUMUS DARI SOAL
                int K = pointYangDikumpulin.length;
                int indexQ1 = Math.max(0, (int)Math.floor((1.0 / 4.0) * (K -1)));
                int indexQ3 = Math.min(K - 1, (int)Math.floor((3.0 / 4.0) * (K -1)));
                int Q1 = pointYangDikumpulin[indexQ1];
                int Q3 = pointYangDikumpulin[indexQ3];
                int IQR = Q3 - Q1;
                int floorTemp = (int)Math.floor(1.5 * IQR);
                int L = Q1 - floorTemp;
                int U = Q3 + floorTemp;
                int count = 0;
                if (extremeBound.equals("L")) {
                    for (int point : pointYangDikumpulin) {
                        if (point < L) {
                            count++;
                        }
                    }
                } else if (extremeBound.equals("U")) {
                    for (int point : pointYangDikumpulin) {
                        if (point > U) {
                            count++;
                        }
                    }
                }
                out.println(count);
            }
            

            else if (query.equals("M")) {
                // Pergerakan Sofita
                String arahGerakSofita = in.next();
                if (teamNodeSekarang != null) {
                    if (arahGerakSofita.equals("L")) {
                        teamNodeSekarang = teamNodeSekarang.prev;
                    } else if (arahGerakSofita.equals("R")) {
                        teamNodeSekarang = teamNodeSekarang.next;
                    }
                    // Cek jika tim yang didatangi sofita ada penjoki
                    Kelompok timSekarang = teamNodeSekarang.data;
                    if (timSekarang.hasPenjoki()) {
                        timSekarang.setPenjoki(false); // Mengusir penjoki dari node/team sekarang

                        // Mencari total point terendah yang tidak ada Sofitanya
                        CircularDoublyLinkedList<Kelompok>.TeamNode nodeTimTerendah = getTimDenganPoinTerendah(
                                teamNodeSekarang);

                        // Memindahkan penjoki ke tim baru
                        if (nodeTimTerendah != null) {
                            penjokiNode = nodeTimTerendah;
                            penjokiNode.data.setPenjoki(true);
                        } else {
                            // Gaada tim buat penjoki
                            penjokiNode = null;
                        }

                        // Penalti untuk tim curang
                        timSekarang.incrementPenjokiTertangkap();
                        int pointerTertangkap = timSekarang.getPenjokiTertangkap();
                        if (pointerTertangkap == 1) {
                            timSekarang.getPesertas().menghapusPesertaPoinTinggi(3);
                        } else if (pointerTertangkap == 2) {
                            timSekarang.getPesertas().setSemuaPoinJadiSatu();
                        } else if (pointerTertangkap >= 3) {
                            // Eliminasi tim jika sudah 3x ketauan joki
                            CircularDoublyLinkedList<Kelompok>.TeamNode toRemove = teamNodeSekarang;
                            // Kembalikan Sofita ke tim dengan point tertinggi
                            teamNodeSekarang = getTimDenganPoinTertinggi();
                            kelompoks.remove(toRemove);
                            if (kelompoks.size() == 0) {
                                teamNodeSekarang = null;
                            }
                        }

                        // Update total point team setelah mengalami PENENDANGAN
                        timSekarang.updateTotalPoints();

                        // Cek apakah tim ada yang kurang dari 7 setelah dilakukan penalti
                        if (timSekarang.getPesertas().getSize() < 7) {
                            // Hapus tim dari list
                            CircularDoublyLinkedList<Kelompok>.TeamNode toRemove = teamNodeSekarang;
                            // Move Sofita to the team with highest total points
                            teamNodeSekarang = getTimDenganPoinTertinggi();
                            kelompoks.remove(toRemove);
                            if (kelompoks.size() == 0) {
                                teamNodeSekarang = null;
                            }
                        }
                    }

                    // OUTPUT
                    if (teamNodeSekarang != null) {
                        out.println(teamNodeSekarang.data.getTeamId());
                    } else {
                        out.println("-1");
                    }
                }
            }

            else if (query.equals("T")) {
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

                // Mengupdate total point tim
                timSekarang.updateTotalPoints();

                // Cetak poin pengirim dan penerima
                out.println(pengirim.getPoin() + " " + penerima.getPoin());
            }

            else if (query.equals("G")) {
                String position = in.next();

                // bikin tim dengan ID baru dari yang sebelumnya
                Kelompok newTeam = new Kelompok(idTimSelanjutnya++);

                // Add 7 partisipan baru dengan poin awal 1
                for (int j = 0; j < 7; j++) {
                    Peserta PartisipanBaru = new Peserta(counter++, 1);
                    newTeam.addPeserta(PartisipanBaru);
                }

                if (teamNodeSekarang == null) {
                    // Jika gaada tim maka add tim baru dan bikin dia jadi current team
                    kelompoks.addLast(newTeam);
                    teamNodeSekarang = kelompoks.getHead();
                } else {
                    if (position.equals("L")) {
                        // Insert the team baru ke kirinya current team
                        kelompoks.addBefore(teamNodeSekarang, newTeam);
                    } else if (position.equals("R")) {
                        // Kanannya
                        kelompoks.addAfter(teamNodeSekarang, newTeam);
                    }
                }

                out.println(newTeam.getTeamId());
            }

            else if (query.equals("V")) {
                int id_peserta1 = in.nextInteger();
                int id_peserta2 = in.nextInteger();
                int teamId = in.nextInteger();
                int result = in.nextInteger();

                // Validsai peserta dan tim
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

                // Mendapatkan tim dengan ID timnya
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

                // Hapus peserta dari AVL-nya
                teamSofita.getPesertas().remove(id_peserta1);
                timLawan_V.getPesertas().remove(id_peserta2);

                // Update point hasil sebagai hadiah pertarungannya
                if (result == 0) {
                    player1.addPoins(1);
                    player2.addPoins(1);
                } else if (result == 1) {
                    player1.addPoins(3);
                    player2.subtractPoins(3);
                    if (player2.getPoin() < 0)
                        player2.poins = 0; // pastikan gaada point negatif
                } else if (result == -1) {
                    player2.addPoins(3);
                    player1.subtractPoins(3);
                    if (player1.getPoin() < 0)
                        player1.poins = 0; // validasi gaada point negatif
                }

                // Memasukkan kembali peserta kedalam pohon AVL-nya
                teamSofita.getPesertas().insert(player1);
                timLawan_V.getPesertas().insert(player2);

                // update total point masing-masing tim
                teamSofita.updateTotalPoints();
                timLawan_V.updateTotalPoints();

                // Cek tim yang akan dieliminasi
                List<CircularDoublyLinkedList<Kelompok>.TeamNode> timAkanDieliminasi = new ArrayList<>();
                boolean timSofitaTereliminasi = false;
                boolean timLawanTereliminasi = false;

                // Check tim yang ada sofitanya
                if (teamSofita.getPesertas().getSize() < 7) {
                    timAkanDieliminasi.add(teamNodeSekarang);
                    timSofitaTereliminasi = true;
                }

                // Cek tim yang ada penjokinya
                if (timLawan_V.getPesertas().getSize() < 7) {
                    timAkanDieliminasi.add(nodeTimLawan);
                    if (penjokiNode == nodeTimLawan) {
                        timLawanTereliminasi = true;
                    }
                }

                // Eliminasi Tim
                for (CircularDoublyLinkedList<Kelompok>.TeamNode nodeToRemove : timAkanDieliminasi) {
                    // Cek apakah tim sedang diawasi Sofita
                    if (teamNodeSekarang == nodeToRemove) {
                        timSofitaTereliminasi = true;
                        teamNodeSekarang = null;
                    }

                    // Cek apakah timnya ada penjokinya
                    if (penjokiNode == nodeToRemove) {
                        timLawanTereliminasi = true;
                        penjokiNode.data.setPenjoki(false); // Hapus penjoki dari tim
                        penjokiNode = null;
                    }

                    // Remove team dari list
                    kelompoks.remove(nodeToRemove);
                }

                // Jika tim Sofita tereliminasi pindahkan Sofita ke tim dengan total point
                // tertinggi
                if (timSofitaTereliminasi && kelompoks.size() > 0) {
                    teamNodeSekarang = getTimDenganPoinTertinggi();

                    // Untuk menghandle kalau Sofita dan penjoki berada dalam satu tim
                    while (penjokiNode != null && teamNodeSekarang != null && teamNodeSekarang == penjokiNode) {
                        // apus penjoki dari tim tersebut
                        teamNodeSekarang.data.setPenjoki(false);

                        // Jalankan pinalti
                        teamNodeSekarang.data.incrementPenjokiTertangkap();
                        int hitungBerapaKaliKecidukJoki = teamNodeSekarang.data.getPenjokiTertangkap();
                        if (hitungBerapaKaliKecidukJoki == 1) {
                            teamNodeSekarang.data.getPesertas().menghapusPesertaPoinTinggi(3);
                        } else if (hitungBerapaKaliKecidukJoki == 2) {
                            teamNodeSekarang.data.getPesertas().setSemuaPoinJadiSatu();
                        } else if (hitungBerapaKaliKecidukJoki >= 3) {
                            // Eliminate team
                            kelompoks.remove(teamNodeSekarang);
                            if (kelompoks.size() > 0) {
                                teamNodeSekarang = getTimDenganPoinTertinggi();
                            } else {
                                teamNodeSekarang = null;
                                break;
                            }
                        }

                        // Jika penalti udah dijalankan dan tim tersebut harus dieliminasi karna kurang
                        // orankkkk (< 7)
                        if (teamNodeSekarang != null && teamNodeSekarang.data.getPesertas().getSize() < 7) {
                            // Remove dari list
                            kelompoks.remove(teamNodeSekarang);
                            if (kelompoks.size() > 0) {
                                teamNodeSekarang = getTimDenganPoinTertinggi();
                            } else {
                                teamNodeSekarang = null;
                                break;
                            }
                        }

                        // Mencari tim selanjutnya buat si penjoki
                        penjokiNode = getTimDenganPoinTerendah(teamNodeSekarang);
                        if (penjokiNode != null) {
                            penjokiNode.data.setPenjoki(true);
                        } else {
                            penjokiNode = null;
                        }
                    }
                }

                // Kalau tim penjoki keeliminasi maka pindahkan ke total tim terkecil pointnya
                if (timLawanTereliminasi && kelompoks.size() > 0) {
                    CircularDoublyLinkedList<Kelompok>.TeamNode lowestTeamNode = getTimDenganPoinTerendah(
                            teamNodeSekarang);
                    if (lowestTeamNode != null) {
                        penjokiNode = lowestTeamNode;
                        penjokiNode.data.setPenjoki(true);
                    } else {
                        penjokiNode = null;
                    }
                }

                // OUTPUT
                if (result == 0) {
                    out.println(player1.getPoin() + " " + player2.getPoin());
                } else if (result == 1) {
                    out.println(player1.getPoin());
                } else if (result == -1) {
                    out.println(player2.getPoin());
                }
            }

            else if (query.equals("E")) {
                int minPoints = in.nextInteger();
                int teamsEliminated = 0;

                // List tim yang akan dihapus
                List<CircularDoublyLinkedList<Kelompok>.TeamNode> timAkanDihapus = new ArrayList<>();

                // Cek tim mana yang akan dihapus
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

                // Flag untuk mengecek apakah Sofita dan Penjoki harus dihapus
                boolean timSofitaTereliminasi = false;
                boolean timPenjokiTereliminasi = false;

                // Eliminasi timnya
                for (CircularDoublyLinkedList<Kelompok>.TeamNode nodeToRemove : timAkanDihapus) {
                    // Check jika tim yang kehapus ada Sofitanya
                    if (teamNodeSekarang == nodeToRemove) {
                        timSofitaTereliminasi = true;
                        teamNodeSekarang = null;
                    }

                    // Check apakah tim tersebut ada penjokinya
                    if (penjokiNode == nodeToRemove) {
                        timPenjokiTereliminasi = true;
                        penjokiNode.data.setPenjoki(false); // Hapus penjoki dari tim sekarang
                        penjokiNode = null;
                    }

                    // Hapus tim dari LIST-nya
                    kelompoks.remove(nodeToRemove);
                    teamsEliminated++;
                }

                // Jika tim Sofita kehapus maka pindahkan Sofita ke tim dengan total point
                // tertinggi
                if (timSofitaTereliminasi && kelompoks.size() > 0) {
                    teamNodeSekarang = getTimDenganPoinTertinggi();

                    // Handle jika si Sofita dan Penjoki berada dalam satu tim yang sama
                    while (penjokiNode != null && teamNodeSekarang != null && teamNodeSekarang == penjokiNode) {
                        // Usir penjoki dari situ
                        teamNodeSekarang.data.setPenjoki(false);

                        // Pinalti buat tim sekarang
                        teamNodeSekarang.data.incrementPenjokiTertangkap();
                        int hitungBerapaKaliKecidukJoki = teamNodeSekarang.data.getPenjokiTertangkap();
                        if (hitungBerapaKaliKecidukJoki == 1) {
                            teamNodeSekarang.data.getPesertas().menghapusPesertaPoinTinggi(3);
                        } else if (hitungBerapaKaliKecidukJoki == 2) {
                            teamNodeSekarang.data.getPesertas().setSemuaPoinJadiSatu();
                        } else if (hitungBerapaKaliKecidukJoki >= 3) {
                            // Eliminate the team
                            kelompoks.remove(teamNodeSekarang);
                            if (kelompoks.size() > 0) {
                                teamNodeSekarang = getTimDenganPoinTertinggi();
                            } else {
                                teamNodeSekarang = null;
                                break;
                            }
                        }

                        // Setelah pinalti maka di cek apakah ada yang < 7, jika ada akan dihapus
                        if (teamNodeSekarang != null && teamNodeSekarang.data.getPesertas().getSize() < 7) {
                            kelompoks.remove(teamNodeSekarang); // remove
                            if (kelompoks.size() > 0) {
                                teamNodeSekarang = getTimDenganPoinTertinggi();
                            } else {
                                teamNodeSekarang = null;
                                break;
                            }
                        }

                        // Cari tim baru untuk Penjoki
                        penjokiNode = getTimDenganPoinTerendah(teamNodeSekarang);
                        if (penjokiNode != null) {
                            penjokiNode.data.setPenjoki(true);
                        } else {
                            penjokiNode = null;
                        }
                    }
                }

                // Jika tim penjoki kehapus maka pindahkan penjoki ke tim terendah
                if (timPenjokiTereliminasi && kelompoks.size() > 0) {
                    CircularDoublyLinkedList<Kelompok>.TeamNode nodeTimTerendah = getTimDenganPoinTerendah(
                            teamNodeSekarang);
                    if (nodeTimTerendah != null) {
                        penjokiNode = nodeTimTerendah;
                        penjokiNode.data.setPenjoki(true);
                    } else {
                        penjokiNode = null;
                    }
                }

                // OUTPUT
                out.println(teamsEliminated);
            }

            else if (query.equals("U")) {
                if (teamNodeSekarang != null) {
                    Kelompok timSekarang = teamNodeSekarang.data;
                    // dapetin semua partisipan
                    List<Peserta> partisipan = timSekarang.getPesertas().getSemuaPartisipan();
                    // menggunakan HashSet buat store unique point values
                    HashSet<Integer> poinUnik = new HashSet<>();
                    for (Peserta p : partisipan) {
                        poinUnik.add(p.getPoin());
                    }
                    out.println(poinUnik.size());
                } else {
                    out.println("-1"); // Sofita ngga ngecek any team
                }
            }

            else if (query.equals("R")) {
                if (kelompoks.size() > 0) {
                    CircularDoublyLinkedList<Kelompok>.TeamNode toRemove = null;

                    // MergeSORT di CDLL
                    kelompoks.mergeSort();

                    // Tim dengan rank tertinggi akan menjadi kepala setelah melakukan sorting
                    teamNodeSekarang = kelompoks.getHead();

                    // output adalah ID tim atau -1 jika Sofita sedang tidak mengawas
                    if (teamNodeSekarang != null) {
                        out.println(teamNodeSekarang.data.getTeamId());
                    } else {
                        out.println("-1");
                    }

                    // Handle jika Sofita dan Penjoki berada dalam satu tim yang sama
                    if (penjokiNode != null && teamNodeSekarang == penjokiNode) {
                        // USIR penjoki dari tim sekarang
                        teamNodeSekarang.data.setPenjoki(false);

                        // PENALTIEEEEEEEEEEEEEESSSSSSSSSSSSSSSSSSSS
                        teamNodeSekarang.data.incrementPenjokiTertangkap();
                        int hitungBerapaKaliKecidukJoki = teamNodeSekarang.data.getPenjokiTertangkap();
                        if (hitungBerapaKaliKecidukJoki == 1) {
                            teamNodeSekarang.data.getPesertas().menghapusPesertaPoinTinggi(3);
                        } else if (hitungBerapaKaliKecidukJoki == 2) {
                            teamNodeSekarang.data.getPesertas().setSemuaPoinJadiSatu();
                        } else if (hitungBerapaKaliKecidukJoki >= 3) {
                            // Eliminasi tim
                            toRemove = teamNodeSekarang;
                            kelompoks.remove(toRemove);
                            if (kelompoks.size() > 0) {
                                teamNodeSekarang = kelompoks.getHead();
                            } else {
                                teamNodeSekarang = null;
                            }
                        }

                        // Cek masih diatas 7 gak anggota timnya abis dilakukan pinalties
                        if (teamNodeSekarang != null && teamNodeSekarang.data.getPesertas().getSize() < 7) {
                            // Usir tim dari list-nya
                            toRemove = teamNodeSekarang;
                            kelompoks.remove(toRemove);
                            if (kelompoks.size() > 0) {
                                teamNodeSekarang = kelompoks.getHead();
                            } else {
                                teamNodeSekarang = null;
                            }
                        }

                        // Cari tim baru untuk penjoki
                        penjokiNode = getTimDenganPoinTerendah(teamNodeSekarang);
                        if (penjokiNode != null) {
                            penjokiNode.data.setPenjoki(true);
                        } else {
                            penjokiNode = null;
                        }

                        // Untuk menghandle repeated encounters
                        while (penjokiNode != null && teamNodeSekarang != null && penjokiNode == teamNodeSekarang) {
                            // Usir penjoki dari list
                            teamNodeSekarang.data.setPenjoki(false);

                            // PENALTI
                            teamNodeSekarang.data.incrementPenjokiTertangkap();
                            hitungBerapaKaliKecidukJoki = teamNodeSekarang.data.getPenjokiTertangkap();
                            if (hitungBerapaKaliKecidukJoki == 1) {
                                teamNodeSekarang.data.getPesertas().menghapusPesertaPoinTinggi(3);
                            } else if (hitungBerapaKaliKecidukJoki == 2) {
                                teamNodeSekarang.data.getPesertas().setSemuaPoinJadiSatu();
                            } else if (hitungBerapaKaliKecidukJoki >= 3) {
                                // Eliminasi TIM
                                toRemove = teamNodeSekarang;
                                kelompoks.remove(toRemove);
                                if (kelompoks.size() > 0) {
                                    teamNodeSekarang = kelompoks.getHead();
                                } else {
                                    teamNodeSekarang = null;
                                    break;
                                }
                            }

                            // CEK MASIH DIATAS 7 GAK
                            if (teamNodeSekarang != null && teamNodeSekarang.data.getPesertas().getSize() < 7) {
                                // Remove the team from the list
                                toRemove = teamNodeSekarang;
                                kelompoks.remove(toRemove);
                                if (kelompoks.size() > 0) {
                                    teamNodeSekarang = kelompoks.getHead();
                                } else {
                                    teamNodeSekarang = null;
                                    break;
                                }
                            }

                            // CARI RUMAH LAGI UNTUK PENJOKI
                            penjokiNode = getTimDenganPoinTerendah(teamNodeSekarang);
                            if (penjokiNode != null) {
                                penjokiNode.data.setPenjoki(true);
                            } else {
                                penjokiNode = null;
                            }
                        }
                    }
                } else {
                    out.println("-1"); // Output -1 if there are no teams
                }
            }

            else if (query.equals("J")) {
                String direction = in.next();
                if (penjokiNode != null) {
                    CircularDoublyLinkedList<Kelompok>.TeamNode targetNode = null;
                    if (direction.equals("L")) {
                        targetNode = penjokiNode.prev;
                    } else if (direction.equals("R")) {
                        targetNode = penjokiNode.next;
                    }

                    // Ngecek Sofita ngecek bagian ini ngga
                    // eliminated
                    if (targetNode != null && targetNode != teamNodeSekarang && targetNode.data != null) {
                        // Move Penjoki to the new team
                        penjokiNode.data.setPenjoki(false); // Hapus Penjoki dari current team
                        penjokiNode = targetNode;
                        penjokiNode.data.setPenjoki(true); // Place Penjoki on new team
                    }
                    // Outputnya ID of the team dimana Penjoki is currently helping
                    out.println(penjokiNode.data.getTeamId());
                } else {
                    out.println("-1"); // Penjoki tidak menolong tim mana-mana
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
        private int penjokihitungBerapaKaliKecidukJoki;

        public Kelompok(int teamId) {
            this.teamId = teamId;
            this.pesertas = new PesertaBST();
            this.hasPenjoki = false;
            this.penjokihitungBerapaKaliKecidukJoki = 0;
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
            return penjokihitungBerapaKaliKecidukJoki;
        }

        public void incrementPenjokiTertangkap() {
            this.penjokihitungBerapaKaliKecidukJoki++;
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
            // Compare total points dengan syarat higher is better
            if (this.getTotalPoinKelompok() > other.getTotalPoinKelompok()) {
                return -1; // ini higher
            } else if (this.getTotalPoinKelompok() < other.getTotalPoinKelompok()) {
                return 1; // ini lower
            } else {
                // Total pointsnya equal, badningin number of members yang mana fewer is better
                if (this.getPesertas().getSize() < other.getPesertas().getSize()) {
                    return -1; // ini memiliki fewer members, maka ini higher
                } else if (this.getPesertas().getSize() > other.getPesertas().getSize()) {
                    return 1; // ini memiliki more members, maka it's lower
                } else {
                    // Nmor membersnya equal, compare/bandingin team IDs (lower ID is better)
                    if (this.getTeamId() < other.getTeamId()) {
                        return -1; // IDnya lebih kecil
                    } else if (this.getTeamId() > other.getTeamId()) {
                        return 1; // misal memiliki ID lebih gede
                    } else {
                        return 0; // Semua atributnya sama (= equal)
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
            // Sort partisipan dengan points di descending order menggunakan insertion sort
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

                // Move elements of list[0..i-1], yang mana less dari keynya, ke posisi ahead
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
                if (node.left == null || node.right == null) {
                    // Node dengan satu child or no child
                    NodePeserta temp = (node.left != null) ? node.left : node.right;
                    totalPoints -= node.peserta.getPoin();
                    size--;
                    node = temp;
                } else {
                    // Node with two children
                    // Get the inorder successor (smallest in the right subtree)
                    NodePeserta temp = minValueNode(node.right);
                    // ngeCopy inorder successor's content buat node ini
                    node.peserta = temp.peserta;
                    // Delete si inorder successor
                    node.right = removeRekursif(node.right, temp.peserta.getId());
                }
            }
            if (node == null) {
                return node;
            }

            // Update height
            node.height = 1 + Math.max(getTinggiTree(node.left), getTinggiTree(node.right));

            // seimbang the node
            int seimbang = getTreeSeimbang(node);

            // LL
            if (seimbang > 1 && getTreeSeimbang(node.left) >= 0) {
                return rotasiTreeKeKanan(node);
            }
            // LR
            if (seimbang > 1 && getTreeSeimbang(node.left) < 0) {
                node.left = rotasiTreeKeKiri(node.left);
                return rotasiTreeKeKanan(node);
            }
            // RR
            if (seimbang < -1 && getTreeSeimbang(node.right) <= 0) {
                return rotasiTreeKeKiri(node);
            }
            // RL
            if (seimbang < -1 && getTreeSeimbang(node.right) > 0) {
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
            if (node == null)
                return 0;
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

        // Inside the CircularDoublyLinkedList<T> class
        public void mergeSort() {
            if (kepala == null || kepala.next == kepala) {
                return; // Listnya kosong kalo ga cuman ada 1 elemen
            }
            // Break the circular buat sorting
            ekor.next = null;
            kepala.prev = null;
            // Perform merge sort di listnya
            kepala = mergeSortRec(kepala);
            // benerin si circular
            // Update ekor
            ekor = kepala;
            while (ekor.next != null) {
                ekor = ekor.next;
            }
            // Ngejadiin circular
            ekor.next = kepala;
            kepala.prev = ekor;
        }

        private TeamNode mergeSortRec(TeamNode head) {
            if (head == null || head.next == null) {
                return head;
            }
            // Split the list into two halves
            TeamNode middle = getMiddle(head);
            TeamNode nextOfMiddle = middle.next;
            middle.next = null;
            if (nextOfMiddle != null) {
                nextOfMiddle.prev = null;
            }
            // Rekursif sort buat sublistnya
            TeamNode left = mergeSortRec(head);
            TeamNode right = mergeSortRec(nextOfMiddle);
            // Merge si sort buat sublistnya
            TeamNode sortedList = sortedMerge(left, right);
            return sortedList;
        }

        private TeamNode getMiddle(TeamNode head) {
            if (head == null) {
                return head;
            }
            TeamNode slow = head;
            TeamNode fast = head;
            while (fast.next != null && fast.next.next != null) {
                fast = fast.next.next;
                slow = slow.next;
            }
            return slow;
        }

        @SuppressWarnings("unchecked")
        private TeamNode sortedMerge(TeamNode left, TeamNode right) {
            if (left == null) {
                return right;
            }
            if (right == null) {
                return left;
            }
            TeamNode result;
            if (((Comparable<T>) left.data).compareTo(right.data) <= 0) {
                result = left;
                result.next = sortedMerge(left.next, right);
                if (result.next != null) {
                    result.next.prev = result;
                }
                result.prev = null;
            } else {
                result = right;
                result.next = sortedMerge(left, right.next);
                if (result.next != null) {
                    result.next.prev = result;
                }
                result.prev = null;
            }
            return result;
        }

    }

    private static CircularDoublyLinkedList<Kelompok>.TeamNode getTimDenganPoinTertinggi() {
        if (kelompoks.size() == 0)
            return null;

        CircularDoublyLinkedList<Kelompok>.TeamNode tempNode = kelompoks.getHead();
        CircularDoublyLinkedList<Kelompok>.TeamNode highestTeamNode = null;

        do {
            if (highestTeamNode == null || tempNode.data.compareTo(highestTeamNode.data) < 0) {
                highestTeamNode = tempNode;
            }
            tempNode = tempNode.next;
        } while (tempNode != kelompoks.getHead());

        return highestTeamNode;
    }

    private static CircularDoublyLinkedList<Kelompok>.TeamNode getTimDenganPoinTerendah(
            CircularDoublyLinkedList<Kelompok>.TeamNode excludeNode) {
        if (kelompoks.size() == 0)
            return null;

        CircularDoublyLinkedList<Kelompok>.TeamNode tempNode = kelompoks.getHead();
        CircularDoublyLinkedList<Kelompok>.TeamNode nodeTimTerendah = null;

        do {
            if (tempNode != excludeNode) {
                if (nodeTimTerendah == null || tempNode.data.compareTo(nodeTimTerendah.data) > 0) {
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