import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Lab2 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        String kata = in.next();

        long TotalJumlahKataSDA = solve(kata);
        
        // Output hasilnya
        out.println(TotalJumlahKataSDA);
        out.flush();
    }

    // InputReader untuk input cepat
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

    public static long solve(String kata) {
        long sda_s = 0;   
        long sda_sd = 0;   
        long sda_sda = 0;  
        
        // Iterasi melalui setiap karakter pada string
        for (int i = 0; i < kata.length(); i++) {
            char c = kata.charAt(i);
            if (c == 's') {
                sda_s += 1;  
            } else if (c == 'd') {
                sda_sd += sda_s;  
            } else if (c == 'a') {
                sda_sda += sda_sd;  
            }
        }

        return sda_sda;
    }

}