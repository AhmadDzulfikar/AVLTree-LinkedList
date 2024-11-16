import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class TP1_S {
    private static InputReader in;
    private static PrintWriter out;
    public static long N, M, Q;
    public static ArrayList<Integer> HargaIkan = new ArrayList<>();
    public static ArrayList<Integer> HargaSuvenir = new ArrayList<>();
    public static ArrayList<Integer> KebahagiaanSuvenir = new ArrayList<>();
    public static ArrayList<Pelanggan> antrian = new ArrayList<>();

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

        // Mengurutkan harga ikan agar bisa menggunakan binary search
        Collections.sort(HargaIkan);

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
                int uangDimiliki = in.nextInteger(); // UangDimiliki pelanggan
                int kesabaranPelanggan = in.nextInteger(); // ID ikan favorit pelanggan
                // Buat pelanggan baru dan tambahkan ke antrian
                Pelanggan pelangganBaru = new Pelanggan(antrian.size(), uangDimiliki, kesabaranPelanggan);
                antrian.add(pelangganBaru);

                // Keluarkan ID pelanggan yang baru datang
                out.println(pelangganBaru.id);
            } else if (query.equals("S")) {
                int hargaDicari = in.nextInteger();
                int selisihMinimum = cariSelisihTerdekat(hargaDicari);
                out.println(selisihMinimum);
            } else if (query.equals("L")) {
                // Placeholder untuk query L
            } else if (query.equals("D")) {
                // Placeholder untuk query D
            } else if (query.equals("B")) {
                // Placeholder untuk query B
            } else if (query.equals("O")) {
                // Placeholder untuk query O
            }
        }

        // Jangan lupa untuk menutup/membersihkan output
        out.close();
    }

    // Fungsi untuk mencari selisih minimum antara harga yang dicari dengan harga ikan terdekat
    public static int cariSelisihTerdekat(int hargaDicari) {
        int left = 0, right = HargaIkan.size() - 1;

        // Jika hargaDicari lebih kecil dari harga ikan terendah atau lebih besar dari harga ikan tertinggi
        if (hargaDicari <= HargaIkan.get(0)) {
            return HargaIkan.get(0) - hargaDicari;
        }
        if (hargaDicari >= HargaIkan.get(HargaIkan.size() - 1)) {
            return hargaDicari - HargaIkan.get(HargaIkan.size() - 1);
        }

        // Lakukan binary search
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (HargaIkan.get(mid) == hargaDicari) {
                return 0; // Jika ditemukan harga yang sama, selisihnya 0
            } else if (HargaIkan.get(mid) < hargaDicari) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // Ambil selisih terdekat dari dua kandidat yang ada setelah binary search
        int selisihKiri = Math.abs(hargaDicari - HargaIkan.get(left));
        int selisihKanan = Math.abs(hargaDicari - HargaIkan.get(right));

        return Math.min(selisihKiri, selisihKanan);
    }

    // Static class Pelanggan
    static class Pelanggan {
        int id; // ID pelanggan (berdasarkan urutan antrian)
        int uangDimiliki; // Jumlah uang yang dimiliki pelanggan
        int kesabaranPelanggan; // ID ikan favorit pelanggan

        public Pelanggan(int id, int uangDimiliki, int kesabaranPelanggan) {
            this.id = id;
            this.uangDimiliki = uangDimiliki;
            this.kesabaranPelanggan = kesabaranPelanggan;
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
