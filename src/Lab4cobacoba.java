import java.io.*;
import java.util.*;

public class Lab4 {
    private static InputReader in;
    private static PrintWriter out;
    public static String a, b;
    public static int n, m;
    static final long MOD = 1000000007;

    public static void main(String[] args) {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);
        a = in.next(); b = in.next();
        n = a.length(); m = b.length();
        out.println(solveDP(a, b, n, m));
        out.close();
    }

    public static long solveDP(String a, String b, int n, int m) {
        long[][] dp = new long[n + 1][m + 1];
        for (int i = 0; i <= n; i++) dp[i][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dp[i][j] = dp[i - 1][j];
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = (dp[i][j] + (i > 1 ? dp[i - 2][j - 1] : dp[i - 1][j - 1])) % MOD;
                }
            }
        }
        return dp[n][m];
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;
        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
        }
        public String next() {
            try {
                while (tokenizer == null || !tokenizer.hasMoreTokens())
                    tokenizer = new StringTokenizer(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return tokenizer.nextToken();
        }
    }
}




