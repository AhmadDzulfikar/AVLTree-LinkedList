import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.ArrayList;

public class TP1_Backup_AS {
    private static InputReader in;
    private static PrintWriter out;
    public static long N, M, Q;
    public static PriorityQueue<Pelanggan> antrian = new PriorityQueue<>(); // Menggunakan PriorityQueue untuk antrian pelanggan
    public static ArrayList<Integer> HargaIkan = new ArrayList<>();
    public static ArrayList<Integer> HargaSuvenir = new ArrayList<>();
    public static ArrayList<Integer> KebahagiaanSuvenir = new ArrayList<>();

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

        // Proses setiap aktivitas (query)
        for (int i = 0; i < Q; i++) {
            String query = in.next(); // Baca query

            if (query.equals("A")) {
                int UangDimiliki = in.nextInteger(); // UangDimiliki pelanggan
                int KesabaranPelanggan = in.nextInteger(); // Kesabaran pelanggan

                // Buat pelanggan baru dan tambahkan ke antrian PriorityQueue
                Pelanggan pelangganBaru = new Pelanggan(antrian.size(), UangDimiliki, KesabaranPelanggan);
                antrian.add(pelangganBaru);

                // Keluarkan ID pelanggan yang baru datang
                out.println(pelangganBaru.id);
            } else if (query.equals("S")) {
                int cariHargaIkan = in.nextInteger();
                int SelisihHargaIkan = MencariSelisihIkan(cariHargaIkan);
                out.println(SelisihHargaIkan);
            } else if (query.equals("L")) {

            } else if (query.equals("D")) {

            } else if (query.equals("B")) {

            } else if (query.equals("O")) {

            }
        }

        // Jangan lupa untuk menutup/membersihkan output
        out.close();
    }

    // Fungsi untuk mencari selisih minimum antara harga yang dicari dengan harga ikan terdekat
    public static int MencariSelisihIkan(int cariHargaIkan) {
        int BarisKiri = 0, BarisKanan = HargaIkan.size() - 1;

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

    // Static class Pelanggan, implements Comparable untuk digunakan dalam PriorityQueue
    static class Pelanggan implements Comparable<Pelanggan> {
        int id; // ID pelanggan
        int UangDimiliki;
        int KesabaranPelanggan;

        public Pelanggan(int id, int UangDimiliki, int KesabaranPelanggan) {
            this.id = id;
            this.UangDimiliki = UangDimiliki;
            this.KesabaranPelanggan = KesabaranPelanggan;
        }

        // Implementasikan metode compareTo untuk menentukan urutan dalam antrian
        @Override
        public int compareTo(Pelanggan pelangganLain) {
            // Pelanggan dengan ID yang lebih kecil memiliki prioritas lebih tinggi (diutamakan dalam antrian)
            return Integer.compare(this.id, pelangganLain.id);
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
