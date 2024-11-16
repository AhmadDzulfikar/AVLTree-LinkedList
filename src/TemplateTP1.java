import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TemplateTP1 {
    private static InputReader in;
    private static PrintWriter out;
    public static long N, M, Q;
    public static ArrayList<Integer> harga_ikan = new ArrayList<>();
    public static ArrayList<Integer> harga_suvenir = new ArrayList<>();
    public static ArrayList<Integer> kebahagiaan_suvenir = new ArrayList<>();

    public static void main(String[] args) {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);

        // Read inputs
        N = in.nextInteger();
        M = in.nextInteger();
        Q = in.nextInteger();

        for (int i = 0; i < N; i++) {
            harga_ikan.add(in.nextInteger());
        }

        for (int i = 0; i < M; i++) {
            harga_suvenir.add(in.nextInteger());
        }

        for (int i = 0; i < M; i++) {
            kebahagiaan_suvenir.add(in.nextInteger());
        }

        for (int i = 0; i < Q; i++) {
            String query = in.next();

            if (query.charAt(0) == 'A') {

            } else if (query.charAt(0) == 'S') {
                
            } else if (query.charAt(0) == 'L') {
                
            } else if (query.charAt(0) == 'D') {
                
            } else if (query.charAt(0) == 'B') {
                
            } else if (query.charAt(0) == 'O') {
            
            }
        }

        // Output the result
        out.println();

        // don't forget to close/flush the output
        out.close();
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
