import java.util.*;
import java.io.*;

public class Lab7 {
    private static InputReader in;
    private static PrintWriter out;
    private static FilmHeap heap;
    private static ArrayList<Film> filmList;

    public static void main(String[] args) {
        // Setup buat input dan output biar cepet dan nggak ribet
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        in = new InputReader(inputStream);
        out = new PrintWriter(outputStream);

        // Baca jumlah film awal
        int N = in.nextInt();

        heap = new FilmHeap();
        filmList = new ArrayList<>();

        // Loop buat nambahin semua film awal ke heap dan daftar film
        for (int i = 0; i < N; i++) {
            long vote = in.nextLong(); // Baca vote tiap film
            Film film = new Film(vote); // Buat objek Film baru
            filmList.add(film); // Tambahin ke daftar film
            heap.insert(film); // Masukin ke heap
        }

        // Baca jumlah query yang bakal dijalanin
        int Q = in.nextInt();

        long vote;
        int filmSatu, filmDua, filmNum;

        // Loop buat proses semua query
        for (int i = 0; i < Q; i++) {
            String query = in.next(); // Baca jenis query
            switch (query) {
                case "T":
                    // Query "T" buat nambah film baru dengan vote tertentu
                    vote = in.nextLong();
                    Film newFilm = new Film(vote); // Buat film baru
                    filmList.add(newFilm); // Tambahin ke daftar film
                    heap.insert(newFilm); // Masukin ke heap
                    Film top = heap.peek(); // Cek film teratas di heap
                    out.println(top.id + " " + top.vote); // Cetak ID dan vote film teratas
                    break;

                case "K":
                    // Query "K" buat ngurangin vote film teratas
                    vote = in.nextLong();
                    Film bestFilm = heap.peek(); // Ambil film teratas
                    bestFilm.vote -= vote; // Kurangi votenya
                    heap.percolateDown(0); // Perbarui posisi film di heap
                    top = heap.peek(); // Cek lagi film teratas setelah ngurangin vote
                    out.println(top.id + " " + top.vote); // Cetak ID dan vote film teratas yang baru
                    break;

                case "R":
                    // Query "R" buat nampilin film teratas sebanyak filmNum
                    filmNum = in.nextInt();
                    if (filmNum > heap.size) {
                        out.println("-1"); // Kalau minta lebih banyak dari yang ada
                    } else {
                        // Ambil filmNum film teratas langsung dari heap
                        List<Film> filmTerbaik = heap.getFilmTerbaik(filmNum);
                        for (int j = 0; j < filmTerbaik.size(); j++) {
                            out.print(filmTerbaik.get(j).id + (j == filmTerbaik.size() - 1 ? "\n" : " "));
                        }
                    }
                    break;

                case "B":
                    // Query "B" buat bandingin dua film berdasarkan vote
                    filmSatu = in.nextInt(); // Baca ID film pertama
                    filmDua = in.nextInt(); // Baca ID film kedua

                    // Ambil objek film dari daftar film berdasarkan ID
                    Film f1 = filmList.get(filmSatu);
                    Film f2 = filmList.get(filmDua);

                    int winnerId;

                    // Tentuin siapa yang lebih gede votenya
                    if (f1.isGreaterThan(f2)) {
                        winnerId = f1.id; // Pemenangnya film1
                        f1.vote /= 2; // Kurangin vote pemenang setengahnya
                        heap.updateFilm(f1.id); // Update posisi film di heap
                    } else {
                        winnerId = f2.id; // Pemenangnya film2
                        f2.vote /= 2; // Kurangin vote pemenang setengahnya
                        heap.updateFilm(f2.id); // Update posisi film di heap
                    }

                    // Cetak ID pemenang
                    out.println(winnerId);
                    break;

                default:
                    break;
            }
            // heap.traverse(); // Bisa dipake buat liat keadaan heap tiap query (untuk debug)
        }
        out.close(); // Tutup output biar nggak bocor
    }

    // Kelas Film buat nyimpen data tiap film
    static class Film {
        int id;
        long vote;
        static int idCounter;

        Film(long vote) {
            id = idCounter++; // Auto-increment ID tiap film baru
            this.vote = vote;
        }

        // Metode buat nentuin film mana yang lebih gede
        public boolean isGreaterThan(Film other) {
            if (this.vote != other.vote) {
                return this.vote > other.vote; // Kalau vote beda, yang vote-nya lebih gede menang
            }
            return this.id < other.id; // Kalau vote sama, yang ID-nya lebih kecil menang
        }
    }

    // Kelas Heap buat manage film dengan vote tertinggi di atas
    static class FilmHeap {
        ArrayList<Film> heap;
        HashMap<Integer, Integer> idToIndexMap; // Pemetaan ID film ke indeks di heap
        int size;

        public FilmHeap() {
            heap = new ArrayList<>();
            idToIndexMap = new HashMap<>();
            size = 0;
        }

        // Metode buat dapetin parent index di heap
        public static int getParentIndex(int i) {
            return (i - 1) / 2;
        }

        // Metode buat nyari k film teratas tanpa ngubah heap asli
        public List<Film> getFilmTerbaik(int k) {
            List<Film> result = new ArrayList<>();
            List<Integer> temukanKFilmTeratas = new ArrayList<>(); // List buat nyimpen indeks yang perlu dieksplore

            // Mulai dari root heap
            temukanKFilmTeratas.add(0);

            while (!temukanKFilmTeratas.isEmpty() && result.size() < k) {
                // Cari film terbaik di temukanKFilmTeratas
                int bestIndex = 0;
                for (int i = 1; i < temukanKFilmTeratas.size(); i++) {
                    int indexSekarang = temukanKFilmTeratas.get(i);
                    if (heap.get(indexSekarang).isGreaterThan(heap.get(temukanKFilmTeratas.get(bestIndex)))) {
                        bestIndex = i;
                    }
                }

                // Ambil film terbaik dan tambahin ke hasil
                int index = temukanKFilmTeratas.remove(bestIndex);
                result.add(heap.get(index));

                // Tambahin anak kiri dan kanan dari film yang diambil ke daftar eksplorasi
                int kiriChild = 2 * index + 1;
                int kananChild = 2 * index + 2;

                if (kiriChild < size) {
                    temukanKFilmTeratas.add(kiriChild);
                }
                if (kananChild < size) {
                    temukanKFilmTeratas.add(kananChild);
                }
            }

            return result;
        }

        // Metode buat naikin film di heap kalau vote-nya naik
        public void percolateUp(int i) {
            while (i > 0 && heap.get(i).isGreaterThan(heap.get(getParentIndex(i)))) {
                swap(i, getParentIndex(i)); // Tuker posisi film di heap
                i = getParentIndex(i); // Update indeks sekarang ke parent
            }
        }

        // Metode buat nurunin film di heap kalau vote-nya turun
        public void percolateDown(int i) {
            int terbesar = i; // Awali dengan indeks sekarang
            int kiri = 2 * i + 1; // Indeks anak kiri
            int kanan = 2 * i + 2; // Indeks anak kanan

            // Cek apakah anak kiri lebih gede dari parent
            if (kiri < size && heap.get(kiri).isGreaterThan(heap.get(terbesar))) {
                terbesar = kiri;
            }
            // Cek apakah anak kanan lebih gede dari yang terbesar sekarang
            if (kanan < size && heap.get(kanan).isGreaterThan(heap.get(terbesar))) {
                terbesar = kanan;
            }

            // Kalau ada anak yang lebih gede, tukar
            if (terbesar != i) {
                swap(i, terbesar); // Tukar posisi
                percolateDown(terbesar); // Rekursif buat lanjut nurunin film yang baru
            }
        }

        // Metode buat update posisi film di heap setelah vote-nya berubah
        public void updateFilm(int id) {
            Integer index = idToIndexMap.get(id); // Cari indeks film di heap
            if (index != null) {
                percolateDown(index); // Coba nurunin dulu
                percolateUp(index); // Abis itu coba naikin
            }
        }

        // Metode buat tuker dua film di heap dan update peta indeks
        public void swap(int i, int j) {
            Film temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
            idToIndexMap.put(heap.get(i).id, i); // Update indeks film yang di-tukar
            idToIndexMap.put(heap.get(j).id, j);
        }

        // Metode buat nambah film baru ke heap
        public void insert(Film film) {
            heap.add(film); // Tambahin film ke akhir heap
            idToIndexMap.put(film.id, size); // Simpen indeks film di peta
            size++; // Tambahin ukuran heap
            percolateUp(size - 1); // Naikin posisi film di heap biar tetep teratur
        }

        // Metode buat ngeliat film teratas tanpa ngambilnya
        public Film peek() {
            if (size == 0) return null; // Kalau heap kosong
            return heap.get(0); // Kembalikan film teratas
        }

        // Metode buat ngambil film teratas dan ngilangin dari heap
        public Film poll() {
            if (size == 0) return null; // Kalau heap kosong
            Film result = heap.get(0); // Ambil film teratas
            heap.set(0, heap.get(size - 1)); // Ganti film teratas dengan film paling akhir
            idToIndexMap.remove(result.id); // Hapus mapping film teratas yang diambil
            idToIndexMap.put(heap.get(0).id, 0); // Update mapping film yang diganti
            heap.remove(size - 1); // Hapus film paling akhir dari heap
            size--; // Kurangi ukuran heap
            if (size > 0) percolateDown(0); // Nurunin film yang baru di posisi teratas
            return result; // Kembalikan film yang diambil
        }

        // Metode buat liat struktur heap (untuk debugging)
        public void traverse() {
            out.println("=============================");
            traverseHelper(0, 0);
            out.println("=============================");
        }

        // Bantuin metode traverse buat rekursi lewat heap
        private void traverseHelper(int index, int depth) {
            if (index >= size) {
                return; // Kalo udah di luar ukuran heap, berhenti
            }

            // Cetak film dengan indentasi berdasarkan depth
            for (int i = 0; i < depth; i++) {
                out.print("  ");
            }
            out.println(heap.get(index).id + " (" + heap.get(index).vote + ")");

            // Rekursif ke anak kiri dan kanan
            traverseHelper(2 * index + 1, depth + 1);
            traverseHelper(2 * index + 2, depth + 1);
        }
    }

    // Kelas buat baca input dengan cepet
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        // Metode buat baca next token
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

        // Metode buat baca karakter
        public char nextChar() {
            return next().charAt(0);
        }

        // Metode buat baca integer
        public int nextInt() {
            return Integer.parseInt(next());
        }

        // Metode buat baca long
        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}
