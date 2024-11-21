import java.util.*;
import java.io.*;

public class Lab7 {
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

        // Reset Film ID counter
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
                    // Membaca jumlah vote yang akan diberikan kepada film baru
                    vote = in.nextLong();

                    // Membuat objek Film baru dengan jumlah vote yang diberikan
                    Film newFilm = new Film(vote);

                    // Menambahkan film baru ke dalam daftar film
                    filmList.add(newFilm);

                    // Memasukkan film baru ke dalam heap
                    heap.insert(newFilm);

                    // Mendapatkan film dengan jumlah vote terbanyak setelah penambahan
                    Film topFilm = heap.peek();

                    // Cek apakah heap kosong atau tidak
                    if (topFilm != null) {
                        // Mencetak ID dan jumlah vote film teratas
                        out.println(topFilm.id + " " + topFilm.vote);
                    } else {
                        out.println("-1 -1"); // Atau sesuai kebutuhan Anda
                    }
                    break;

                case "K":
                    // Membaca jumlah vote yang akan dikurangi
                    vote = in.nextLong();

                    // Mendapatkan film dengan jumlah vote terbanyak
                    Film topFilmK = heap.peek();

                    if (topFilmK != null) {
                        // Mengurangi vote film teratas
                        topFilmK.vote -= vote;

                        // Memperbarui posisi film dalam heap
                        heap.updateFilm(topFilmK.id);

                        // Mendapatkan film dengan jumlah vote terbanyak setelah pengurangan
                        Film newTopFilm = heap.peek();

                        if (newTopFilm != null) {
                            out.println(newTopFilm.id + " " + newTopFilm.vote);
                        } else {
                            out.println("-1 -1");
                        }
                    } else {
                        out.println("-1 -1"); // Heap kosong
                    }
                    break;

                case "R":
                    filmNum = in.nextInt();
                    // TODO: Implementasi untuk query R
                    break;

                case "B":
                    film1 = in.nextInt();
                    film2 = in.nextInt();
                    // TODO: Implementasi untuk query B
                    break;

                default:
                    break;
            }
            // heap.traverse(); // for debugging after each query
        }
        out.close();
    }

    static class Film implements Comparable<Film> {
        int id;
        long vote;
        static int idCounter = 0;

        Film(long vote) {
            this.id = idCounter++;
            this.vote = vote;
        }

        @Override
        public int compareTo(Film other) {
            if (this.vote != other.vote) {
                return Long.compare(this.vote, other.vote); // Ascending order: higher votes are "greater"
            }
            return Integer.compare(other.id, this.id); // Ascending order of IDs: lower ID is "greater"
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
            while (i > 0) {
                int parent = getParentIndex(i);
                if (heap.get(i).compareTo(heap.get(parent)) <= 0) { // Mengubah < 0 menjadi <= 0
                    break;
                }
                swap(i, parent);
                i = parent;
            }
        }

        public void percolateDown(int i) {
            while (true) {
                int left = getLeftChildIndex(i);
                int right = getRightChildIndex(i);
                int largest = i;

                if (left < size && heap.get(left).compareTo(heap.get(largest)) > 0) {
                    largest = left;
                }

                if (right < size && heap.get(right).compareTo(heap.get(largest)) > 0) {
                    largest = right;
                }

                if (largest != i) {
                    swap(i, largest);
                    i = largest;
                } else {
                    break;
                }
            }
        }

        public void updateFilm(int id) {
            if (!idToIndexMap.containsKey(id)) {
                return;
            }
            int index = idToIndexMap.get(id);
            percolateUp(index);
            percolateDown(index);
        }

        public void swap(int i, int j) {
            Film temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);

            // Update the indices in the map
            idToIndexMap.put(heap.get(i).id, i);
            idToIndexMap.put(heap.get(j).id, j);
        }

        public void insert(Film film) {
            heap.add(film);
            idToIndexMap.put(film.id, size);
            percolateUp(size);
            size++;
        }

        public Film peek() {
            if (size == 0) {
                return null;
            }
            return heap.get(0);
        }

        public Film poll() {
            if (size == 0) {
                return null;
            }
            Film top = heap.get(0);
            heap.set(0, heap.get(size - 1));
            idToIndexMap.put(heap.get(0).id, 0);
            heap.remove(size - 1);
            idToIndexMap.remove(top.id);
            size--;
            percolateDown(0);
            return top;
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
                    String line = reader.readLine();
                    if (line == null) {
                        return null;
                    }
                    tokenizer = new StringTokenizer(line);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public char nextChar() {
            String s = next();
            if (s == null || s.length() == 0) {
                return '\0';
            }
            return s.charAt(0);
        }

        public int nextInt() {
            String s = next();
            if (s == null) {
                throw new NoSuchElementException();
            }
            return Integer.parseInt(s);
        }

        public long nextLong() {
            String s = next();
            if (s == null) {
                throw new NoSuchElementException();
            }
            return Long.parseLong(s);
        }
    }
}
