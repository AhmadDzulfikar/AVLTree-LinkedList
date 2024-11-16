import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Stack;

public class TP1 {
    private static InputReader in;
    private static PrintWriter out;
    public static long N, M, Q;
    public static ArrayList<Integer> HargaIkan = new ArrayList<>();
    public static ArrayList<Integer> HargaSuvenir = new ArrayList<>();
    public static ArrayList<Integer> KebahagiaanSuvenir = new ArrayList<>();
    public static PriorityQueue<Pelanggan> antrian = new PriorityQueue<>();
    public static HashMap<Integer, Pelanggan> pelangganToko = new HashMap<>();
    public static int nextId = 0; // ID Pelanggan dalam kasus ini UNIK
    public static Stack<Integer> kupon = new Stack<>();
    public static int querySekarang = 0;

    public static void main(String[] args) {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);

        // Baca input
        N = in.nextInteger(); // Banyak ikan
        M = in.nextInteger(); // Banyak suvenir
        Q = in.nextInteger(); // Banyak aktivitas

        // Baca harga ikan
        for (int i = 0; i < N; i++) {
            HargaIkan.add(in.nextInteger());
        }

        Collections.sort(HargaIkan); // Untuk mengurutkan harga ikan supaya binary search dapat dengan mudah dilakukan

        // Baca harga suvenir
        for (int i = 0; i < M; i++) {
            HargaSuvenir.add(in.nextInteger());
        }

        // Baca nilai kebahagiaan suvenir
        for (int i = 0; i < M; i++) {
            KebahagiaanSuvenir.add(in.nextInteger());
        }

        for (int i = 0; i < Q; i++) {
            querySekarang++;
            String query = in.next(); // Baca query

            if (query.equals("A")) {
                int UangDimiliki = in.nextInteger(); // UangDimiliki para pelanggan
                int KesabaranPelanggan = in.nextInteger(); // kesabaran pelanggan, wokaokaokaowkaok

                Pelanggan pelangganBaru = new Pelanggan(nextId++, UangDimiliki, KesabaranPelanggan, querySekarang); // querysekarang itu kaya ngetrack dia dimana, nanti dikurang pas di L
                antrian.add(pelangganBaru);

                // Menyimpan pelanggan di hashMap dengan key: ID_PELANGGAN dan values: PELANGGAN
                pelangganToko.put(pelangganBaru.id, pelangganBaru);

                // Keluarkan ID pelanggan yang baru datang
                out.println(pelangganBaru.id);
            } else if (query.equals("S")) {
                int cariHargaIkan = in.nextInteger();
                int SelisihHargaIkan = MencariSelisihIkan(cariHargaIkan);
                out.println(SelisihHargaIkan);
            } else if (query.equals("L")) {
                int idPelanggan = in.nextInteger(); // membaca id pelanggan
                if (pelangganToko.containsKey(idPelanggan)) { // ngecek ada idpelanggan yg sesuai di pelangganToko ga
                    Pelanggan pelangganPergi = pelangganToko.get(idPelanggan);
                    int sisaKesabaran = pelangganPergi.KesabaranPelanggan - (querySekarang - pelangganPergi.queryMasuk);
                    if (sisaKesabaran <= 0 ) {
                        pelangganToko.remove(idPelanggan);
                        antrian.remove(pelangganPergi);
                        out.println(-1);
                    } else {
                        // Menghapus jika L sudah dijalankan
                        out.println(pelangganPergi.UangDimiliki);
                        pelangganToko.remove(idPelanggan);
                        antrian.remove(pelangganPergi);
                    }
                } else {
                    out.println(-1);
                }
            } else if (query.equals("D")) {
                // Mengambil nilai angka diskon dan memasukkan (push) kedalam stack
                int diskon = in.nextInteger();
                kupon.push(diskon);
                out.println(diskon);
            } else if (query.equals("B")) {
                while (!antrian.isEmpty()) {
                    Pelanggan pelangganPalingDepan = antrian.peek(); // Lihat pelanggan paling depan tanpa mengeluarkannya
                    int sisaKesabaran = pelangganPalingDepan.KesabaranPelanggan - (querySekarang - pelangganPalingDepan.queryMasuk);

                    if (sisaKesabaran <= 0) {
                        // Keluarkan pelanggan yang kesabarannya sudah habis
                        antrian.poll();
                        pelangganToko.remove(pelangganPalingDepan.id);
                    } else {
                        break; // Keluar dari loop jika pelanggan di depan masih punya kesabaran
                    }
                }
            
                if (antrian.isEmpty()) {
                    out.println(-1);
                } else {
                    Pelanggan pelangganPalingDepan = antrian.poll();
                    int indexIkan = cariIkanTermahal(pelangganPalingDepan.UangDimiliki);

                    if (indexIkan == -1) { // gaada ikan harganya di jangkauan
                        // Usir pelanggan kalo gabisa beli ikan
                        out.println(pelangganPalingDepan.id);
                        pelangganToko.remove(pelangganPalingDepan.id);
                    } else {
                        // Kalau bisa beli ikan 
                        int hargaIkan = HargaIkan.get(indexIkan);
                        int hargaIkanDiskon = hargaIkan;

                        // Cuman pelanggan yang bisa beli ikan dan punya uang pas yang bisa dapet diskon
                        if (!kupon.isEmpty() && pelangganPalingDepan.UangDimiliki == hargaIkan) {
                            int diskon = kupon.peek(); // Lihat kupon yang paling atas
                            hargaIkanDiskon = hargaIkan - diskon;
                        
                            // Pastikan harga ikan tidak kurang dari 1
                            hargaIkanDiskon = Math.max(hargaIkanDiskon, 1);
                        
                            kupon.pop(); // Ambil kupon dari stack setelah digunakan
                        }
                        
                        if (pelangganPalingDepan.UangDimiliki >= hargaIkanDiskon) {
                            int sisaUang = pelangganPalingDepan.UangDimiliki - hargaIkanDiskon; // beli ikan (kurangin uang dengan ikan yg sudah didiskon)
            
                            // sisa uang abis beli ikan
                            out.println(sisaUang);
            
                            // Memasukkan kembalian ke stack kupon jika ada sisa uang
                            if (sisaUang > 0) {
                                kupon.push(sisaUang);
                            }
            
                            // pelanggan dengan uang sisaan
                            pelangganPalingDepan.UangDimiliki = sisaUang;

                            // cek apakah pelanggan yg abis belanja masih punya duit
                            if (pelangganPalingDepan.UangDimiliki > 0) {
                                pelangganPalingDepan.queryMasuk = querySekarang;
                                antrian.add(pelangganPalingDepan);
                            } else {
                                // Usir pelanggan kalo gaada duit
                                pelangganToko.remove(pelangganPalingDepan.id);
                            }
                        } else {
                            // kalau uang gak cukup meski didiskon
                            out.println(pelangganPalingDepan.id);
                            pelangganToko.remove(pelangganPalingDepan.id);
                        }
                    }
                }
            } else if (query.equals("O")) {
            }
            
        }

        out.close();
    }
    public static int cariIkanTermahal(int uangSekarang) {
        int kiri = 0, kanan = HargaIkan.size() - 1, hasil = - 1; // menyimpan indeks harga ikan yg ditemukan

        while (kiri <= kanan) {
            int tengah = kiri + (kanan - kiri) / 2;

            if (HargaIkan.get(tengah) <= uangSekarang) {
                hasil = tengah;
                kiri = tengah + 1;
            } else {
                kanan = tengah - 1;
            }
        }

        return hasil;
    }

    public static int MencariSelisihIkan(int cariHargaIkan) {
        int BarisKiri = 0, BarisKanan = HargaIkan.size() - 1; // Batas kanna kiri

        if (cariHargaIkan <= HargaIkan.get(0)) {
            return HargaIkan.get(0) - cariHargaIkan;
        }
        if (cariHargaIkan >= HargaIkan.get(HargaIkan.size() - 1)) {
            return cariHargaIkan - HargaIkan.get(HargaIkan.size() - 1);
        }

        // Binary Search 
        while (BarisKiri <= BarisKanan) {
            int BarisTengah = BarisKiri + (BarisKanan - BarisKiri) / 2;

            if (HargaIkan.get(BarisTengah) == cariHargaIkan) {
                return 0; // Jika binary search menemukan harga yang sama, maka selisihnya 0
            } else if (HargaIkan.get(BarisTengah) < cariHargaIkan) {
                BarisKiri = BarisTengah + 1;
            } else {
                BarisKanan = BarisTengah - 1;
            }
        }

        // Mengambil selisih yang terdekat dari alur Binary Search ini
        int selisihKiri = Math.abs(cariHargaIkan - HargaIkan.get(BarisKiri));
        int selisihKanan = Math.abs(cariHargaIkan - HargaIkan.get(BarisKanan));

        return Math.min(selisihKiri, selisihKanan);
    }

    static class Pelanggan implements Comparable<Pelanggan> {
        int id; 
        int UangDimiliki;
        int KesabaranPelanggan; 
        int queryMasuk;
        public Pelanggan(int id, int UangDimiliki, int KesabaranPelanggan, int queryMasuk) {
            this.id = id;
            this.UangDimiliki = UangDimiliki;
            this.KesabaranPelanggan = KesabaranPelanggan;
            this.queryMasuk = queryMasuk;
        }

        @Override
        // Melakukan Perbandingan urutan antriannyahhhhhhhh
        public int compareTo(Pelanggan pelangganLainnya) {
            if (this.UangDimiliki != pelangganLainnya.UangDimiliki) {
                return Integer.compare(pelangganLainnya.UangDimiliki, this.UangDimiliki);
            }

            if (this.KesabaranPelanggan != pelangganLainnya.KesabaranPelanggan) {
                return Integer.compare(pelangganLainnya.KesabaranPelanggan, this.KesabaranPelanggan);
            }

            return Integer.compare(this.id, pelangganLainnya.id);
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
                    tokenizer = new StringTokenizer(reader.readLine());
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