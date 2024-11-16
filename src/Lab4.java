import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Lab4 {
    private static InputHandler input;
    private static PrintWriter output;
    public static String a, b;
    public static int n, m;
    public static final int MOD_VAL = 1000000007;
    public static long[][] memoTable;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        input = new InputHandler(inputStream);
        OutputStream outputStream = System.out;
        output = new PrintWriter(outputStream);

        // Read input strings
        a = input.next().toLowerCase();
        b = input.next().toLowerCase();

        n = a.length();
        m = b.length();

        // Initialize memoization table
        memoTable = new long[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                memoTable[i][j] = -1;
            }
        }

        // Compute the result using a new function name
        long result = solve(0, 0);

        // Output the result
        output.println(result);

        // Ensure the output is flushed
        output.close();
    }

    // Recursive function to calculate subsequences
    public static long solve(int indexA, int indexB) {
        // Base case: all characters of b matched
        if (indexB == m) {
            return 1;
        }
        // Base case: reached the end of a without matching all of b
        if (indexA >= n) {
            return 0;
        }

        // Memoization check
        if (memoTable[indexA][indexB] != -1) {
            return memoTable[indexA][indexB];
        }

        // Skip the current character in a
        long totalCount = solve(indexA + 1, indexB);

        // If characters match, count the subsequences by skipping one character in a
        if (a.charAt(indexA) == b.charAt(indexB)) {
            totalCount += solve(indexA + 2, indexB + 1);
            totalCount %= MOD_VAL;
        }

        // Store the result in the memoization table
        memoTable[indexA][indexB] = totalCount;

        return totalCount;
    }

    // Custom input handler for faster IO
    static class InputHandler {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputHandler(InputStream stream) {
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

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
}
