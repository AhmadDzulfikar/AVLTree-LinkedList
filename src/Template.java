import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Template {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read inputs
        long N = in.nextInteger();
        long M = in.nextInteger();
        int K = in.nextInteger();
        int X = in.nextInteger();
        int Y = in.nextInteger();

        // Masukan:
        // N = banyaknya baris
        // M = banyaknya tempat duduk di setiap baris
        // K = banyaknya tempat duduk kosong 
        // X = lokasi baris tempat duduk Sofita
        // Y = urutan tempat duduk Sofita di baris tersebut

        long Tempat = 0;
        for (int i = 0; i < K; i++) {
            long A = in.nextInteger();
            long B = in.nextInteger();

            if (A < X ) {
                Tempat++;
            }
            else if (A == X && B < Y) {
                Tempat++;
            }

        }

        long ans = 0;

        // TODO: Write your code here

        // ! Hint:  Note that to get the full score, you might need to edit other parts of the code as well
        
        outerLoop:
        for (int row = 0; row < N; row++) {
            for (int seat = 0; seat < M; seat++) {
                ans++;
                if (row == X - 1 && seat == Y - 1) {
                    break outerLoop;
                }
            }
        }

        out.println(ans);
        // don't forget to close/flush the output
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
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
