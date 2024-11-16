import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TP1_A {
    private static InputReader in;
    private static PrintWriter out;
    public static long N, M, Q;
    public static ArrayList<Integer> HargaIkan = new ArrayList<>();
    public static ArrayList<Integer> HargaSuvenir = new ArrayList<>();
    public static ArrayList<Integer> KebahagiaanSuvenir = new ArrayList<>();
    public static ArrayList<Pelanggan> antrian = new ArrayList<>(); // untuk enyimpan pelanggan yang ngantri

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
                int KesabaranPelanggan = in.nextInteger(); // ID ikan favorit pelanggan
                // Buat pelanggan baru dan tambahkan ke antrian
                Pelanggan pelangganBaru = new Pelanggan(antrian.size(), UangDimiliki, KesabaranPelanggan);
                antrian.add(pelangganBaru);

                // Keluarkan ID pelanggan yang baru datang
                out.println(pelangganBaru.id);
            } else if (query.equals("S")) {

            } else if (query.equals("L")) {

            } else if (query.equals("D")) {

            } else if (query.equals("B")) {

            } else if (query.equals("O")) {

            }
        }

        // Jangan lupa untuk menutup/membersihkan output
        out.close();
    }

    // Static class Pelanggan
    static class Pelanggan {
        int id; 
        int UangDimiliki;
        int KesabaranPelanggan; 

        public Pelanggan(int id, int UangDimiliki, int KesabaranPelanggan) {
            this.id = id;
            this.UangDimiliki = UangDimiliki;
            this.KesabaranPelanggan = KesabaranPelanggan;
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
