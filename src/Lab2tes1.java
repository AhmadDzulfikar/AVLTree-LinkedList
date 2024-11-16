import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Lab2tes1 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Baca input string
        String kata = in.next();

        // Panggil fungsi solve dan cetak hasilnya
        out.println(solve(kata));
        out.flush();
    }

    // Kelas InputReader untuk pembacaan input yang lebih cepat
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
    }

    // Fungsi untuk menghitung jumlah kemunculan substring "sda"
    public static long solve(String kata) {
        String target = "sda";
        long count = 0;

        // Iterasi melalui setiap karakter pada string (hingga panjang - 3)
        for (int i = 0; i <= kata.length() - target.length(); i++) {
            // Ambil substring sepanjang "sda" mulai dari indeks i
            if (kata.substring(i, i + target.length()).equals(target)) {
                count++;
            }
        }
        return count;
    }
}