import java.util.*;
import java.io.*;

public class Lab7Sendiri {
    private static InputReader in;
    private static PrintWriter out;
    private static FilmHeap heap;
    private static ArrayList<Film> filmList;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        in = new InputReader(inputStream);
        out = new PrintWriter(outputStream);

        int N = in.nextInt();

        heap = new FilmHeap();
        filmList = new ArrayList<>();

        Film.idCounter = 0;

        for (int i = 0; i < N; i++) {
            long vote = in.nextLong();
            Film film = new Film(vote);
            filmList.add(film);
            heap.insert(film);
        }

        int Q = in.nextInt();

        long vote;
        int film1, film2, filmNum;
        Film film;
        
        for (int i = 0; i < Q; i++) {
            String query = in.next();
            switch (query) {
                case "T":
                    vote = in.nextLong(); // Membaca jumlah vote yang akan diberikan kepada film baru
                    Film filmBaru = new Film(vote);
                    filmList.add(filmBaru);
                    heap.insert(filmBaru);

                    Film filmTerkeren = heap.peek();

                    // Ngecek apakah heapnya kosong apa enggak
                    if (filmTerkeren != null) {
                        System.out.println(filmTerkeren.id + " " + filmTerkeren.vote);
                    } else {
                        System.out.println("-1 -1");
                    }
                    break;

                case "K":
                    vote = in.nextLong();
                    // TODO
                    break;
            
                case "R":
                    filmNum = in.nextInt();
                    // TODO
                    break;

                case "B":
                    film1 = in.nextInt();
                    film2 = in.nextInt();

                    // TODO
                    break;

                default:
                    break;
            }
            // heap.traverse(); // for debugging after each query
        }
        out.close();
    }

    static class Film {
        int id;
        long vote;
        static int idCounter;

        Film(long vote) {
            id = idCounter++;
            this.vote = vote;
        }

        @Override
        public int compareTo(Film urutanFilm) {
            if (this.vote != urutanFilm.vote) {
                return Long.compare(this.vote, urutanFilm.vote);
            }
            return Integer.compare(urutanFilm.id, this.id);
            }

    }

    static class FilmHeap {
        ArrayList<Film> heap;
        HashMap<Integer, Integer> idToIndexMap;
        int size;

        public FilmHeap() {
            heap = new ArrayList<>();
            idToIndexMap = new HashMap<>();
            size = 0;
        }

        public static int getParentIndex(int i) {
            return (i - 1) / 2;
        }

        public static int getLeftChildIndex(int i) {
            return 2 * i + 1;
        }

        public static int getRightChildIndex(int i) {
            return 2 * i + 2;
        }

        public void percolateUp(int i) {

        }

        public void percolateDown(int i) {
            while (true) {
                int kiri = getLeftChildIndex(i);
                int kanan = getRightChildIndex(i);
                int largest = i;

                if (kiri < size && heap.get(kiri).compareTo(heap.get(largest)) > 0) {
                    largest = kiri;
                }

                if (kanan < size) {
                    
                }
            }
        }

        public void updateFilm(int id) {
            // TODO
        }

        public void swap(int i, int j) {
            // TODO
        }

        public void insert(Film film) {
            // TODO
        }

        public Film peek() {
            return heap.get(0);
        }

        public Film poll() {
            // TODO
            return null;
        }

        
        // =============== HELPER METHOD FOR DEBUGGING HEAP ===============
        public void traverse() {
            out.println("=============================");
            traverseHelper(0, 0);
            out.println("=============================");
        }

        // =============== HELPER METHOD FOR DEBUGGING HEAP ===============
        private void traverseHelper(int index, int depth) {
            if (index >= size) {
                return;
            }

            // Print the current node with indentation based on depth
            for (int i = 0; i < depth; i++) {
                out.print("  ");
            }
            out.println(heap.get(index).id + " (" + heap.get(index).vote + ")");

            // Traverse left and right children
            traverseHelper(2 * index + 1, depth + 1);
            traverseHelper(2 * index + 2, depth + 1);
        }
        
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

        public char nextChar() {
            return next().charAt(0);
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong()
        {
            return Long.parseLong(next());
        }
    }
}