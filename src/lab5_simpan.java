import java.io.*;
import java.util.StringTokenizer;

public class Lab5{

    private static InputReader in;
    private static PrintWriter out;
    private static DoublyLinkedList keyboard = new DoublyLinkedList();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();

        for (int i = 0; i < N; i++) {
            String command = in.next();
            char data;
            char direction;

            switch (command) {
                case "ADD":
                    direction = in.nextChar();
                    data = in.nextChar();
                    keyboard.add(data, direction);
                    break;

                case "DEL":
                    keyboard.delete();
                    break;

                case "MOVE":
                    direction = in.nextChar();
                    keyboard.move(direction);
                    break;

                case "START":
                    keyboard.start();
                    break;

                case "END":
                    keyboard.end();
                    break;

                case "SWAP":
                    keyboard.swap();
                    break;
            }
        }

        keyboard.printList();
        out.close();
    }

    private static class InputReader {

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
    }
}

class DoublyLinkedList {

    ListNode first;
    ListNode current;
    ListNode last;
    int size = 0;

    public DoublyLinkedList() {
        this.first = null;
        this.current = null;
        this.last = null;
    }

    public void printList() {
        ListNode current = first;
        do {
            System.out.print(current.data);
            current = current.next;
        } while (current != null && current != first);
        System.out.println();
    }

    public void add(char data, char direction) {
        if (size == 0) {
            first = new ListNode(data);
            current = first;
            last = first;
            first.next = first;
            first.prev = first;
        } else {
            ListNode newNode = new ListNode(data);
            if (direction == 'R') {
                newNode.prev = current;
                newNode.next = current.next;
                newNode.prev.next = newNode;
                newNode.next.prev = newNode;
                current = newNode;
            } else {
                newNode.next = current;
                newNode.prev = current.prev;
                newNode.prev.next = newNode;
                newNode.next.prev = newNode;
                current = newNode;
            }
        }
        size++;
    }

    /*
    * Method untuk menghapus ListNode dari {@code current} ListNode
    */
    public void delete() {
        //TODO: Implement this method
    }

    /*
    * Method untuk berpindah ke kiri (prev) atau kanan (next) dari {@code current} ListNode
    */
    public void move(char direction) {
        // if (direction == 'L') {
        //     current = current.prev;
        // } else {
        //     current = current.next;
        // }

        // return direction;
    }

    /*
    * Method untuk berpindah ke node paling pertama (first) dari DoublyLinkedList
    */
    public void start() {
        //TODO: Implement this method
    }
    
    /*
    * Method untuk berpindah ke node paling terakhir (end) dari DoublyLinkedList
    */
    public void end() {
        //TODO: Implement this method
    }

    /*
    * Method untuk memindahkan semua ListNode dari  kiri {@code current} ListNode ke kanan {@code current} ListNode
    */
    public void swap() {
        // kALO CUAMNA DA SAU ELEMEN GAUSAH DPINDAH
        if (current == first || size <= 1) {
            return;
        }
    
        ListNode temp = first;
        ListNode newFirst = current;
        ListNode newLast = current.prev;
        
        // Update link terakhir
        current.prev.next = current.next;
        current.next.prev = current.prev;
    
        // Update link elemen pertama
        first = newFirst;
        first.prev = last;
        last.next = first;
    
        // Update elemen terakhir dan first yang sebelumnya
        last = newLast;
        last.next = temp;
        temp.prev = last;
    }
    
}

class ListNode {

    char data;
    ListNode next;
    ListNode prev;

    ListNode(char data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}














---------------------------------------------------------------------
import java.io.*;
import java.util.StringTokenizer;

public class Lab5{

    private static InputReader in;
    private static PrintWriter out;
    private static DoublyLinkedList keyboard = new DoublyLinkedList();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();

        for (int i = 0; i < N; i++) {
            String command = in.next();
            char data;
            char direction;

            switch (command) {
                case "ADD":
                    direction = in.nextChar();
                    data = in.nextChar();
                    keyboard.add(data, direction);
                    break;

                case "DEL":
                    keyboard.delete();
                    break;

                case "MOVE":
                    direction = in.nextChar();
                    keyboard.move(direction);
                    break;

                case "START":
                    keyboard.start();
                    break;

                case "END":
                    keyboard.end();
                    break;

                case "SWAP":
                    keyboard.swap();
                    break;
            }
        }

        keyboard.printList();
        out.close();
    }

    private static class InputReader {

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
    }
}

class DoublyLinkedList {

    ListNode first;
    ListNode current;
    ListNode last;
    int size = 0;

    public DoublyLinkedList() {
        this.first = null;
        this.current = null;
        this.last = null;
    }

    public void printList() {
        ListNode current = first;
        do {
            System.out.print(current.data);
            current = current.next;
        } while (current != null && current != first);
        System.out.println();
    }

    public void add(char data, char direction) {
        if (size == 0) {
            first = new ListNode(data);
            current = first;
            last = first;
            first.next = first;
            first.prev = first;
        } else {
            ListNode newNode = new ListNode(data);
            if (direction == 'R') {
                newNode.prev = current;
                newNode.next = current.next;
                newNode.prev.next = newNode;
                newNode.next.prev = newNode;
                current = newNode;
            } else {
                newNode.next = current;
                newNode.prev = current.prev;
                newNode.prev.next = newNode;
                newNode.next.prev = newNode;
                current = newNode;
            }
        }
        size++;
    }

    /*
    * Method untuk menghapus ListNode dari {@code current} ListNode
    */
    public void delete() {
        //TODO: Implement this method
    }

    /*
    * Method untuk berpindah ke kiri (prev) atau kanan (next) dari {@code current} ListNode
    */
    public void move(char direction) {
        // if (direction == 'L') {
        //     current = current.prev;
        // } else {
        //     current = current.next;
        // }

        // return direction;
    }

    /*
    * Method untuk berpindah ke node paling pertama (first) dari DoublyLinkedList
    */
    public void start() {
        //TODO: Implement this method
    }
    
    /*
    * Method untuk berpindah ke node paling terakhir (end) dari DoublyLinkedList
    */
    public void end() {
        if (size > 0) {
            current = last;
        }
    }

    /*
    * Method untuk memindahkan semua ListNode dari  kiri {@code current} ListNode ke kanan {@code current} ListNode
    */
/*
 * Method untuk memindahkan semua ListNode di sebelah kiri {@code current} ke akhir DoublyLinkedList
 */
/*
 * Method untuk memindahkan semua ListNode di sebelah kiri {@code current} ke akhir DoublyLinkedList
 */
public void swap() {
    // Jika ukuran list <= 1 atau current sudah di posisi first, tidak perlu swap
    if (current == first || size <= 1) {
        return;
    }

    // Tetapkan node pertama dan terakhir sebelum current
    ListNode oldFirst = first;
    ListNode oldLast = current.prev;

    // Memutus hubungan antara oldLast dan current
    oldLast.next = current;
    current.prev = oldLast;

    // Update first untuk menunjuk ke current
    first = current;

    // Update hubungan antara last dan oldFirst untuk memindahkan bagian kiri ke akhir list
    last.next = oldFirst;
    oldFirst.prev = last;

    // Update hubungan untuk oldLast dengan node pertama yang baru
    oldLast.next = first;
    first.prev = oldLast;

    // Update last ke oldLast
    last = oldLast;
}


    
}

class ListNode {

    char data;
    ListNode next;
    ListNode prev;

    ListNode(char data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}