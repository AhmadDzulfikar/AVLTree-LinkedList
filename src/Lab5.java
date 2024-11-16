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
        ListNode node = first;
        while (node != null) {
            System.out.print(node.data);
            node = node.next;
        }
        System.out.println();
    }

    public void add(char data, char direction) {
        ListNode newNode = new ListNode(data);
        if (size == 0) {
            first = last = current = newNode;
        } 
        
        else if (direction == 'L') {
            newNode.next = current;
            newNode.prev = current.prev; 
            if (current.prev != null) current.prev.next = newNode;
            else first = newNode;
            current.prev = newNode;
            current = newNode;
        } 
        
        else {
            newNode.prev = current;
            newNode.next = current.next;
            if (current.next != null) current.next.prev = newNode;
            else last = newNode;
            current.next = newNode;
            current = newNode;
        }
        size++;
    }

    public void delete() {
        if (size == 0 || current == null) return;
        
        if (size == 1) {
            first = last = current = null;
        } 
        
        else if (current == first) {
            first = first.next;
            first.prev = null;
            current = first;
        } 
        
        else if (current == last) {
            last = last.prev;
            last.next = null;
            current = last;
        } 
        
        else {
            current.prev.next = current.next;
            current.next.prev = current.prev;
            current = current.prev; 
        }
        size--;
    }

    public void move(char direction) {
        if (direction == 'L' && current != first && current.prev != null) {
            current = current.prev;
        } 

        else if (direction == 'R' && current != last && current.next != null) {
            current = current.next;
        }
    }

    public void start() {
        current = first;
    }

    public void end() {
        current = last;
    }

    public void swap() {
        if (current == first || size <= 1) return;
        
        ListNode firstNew = current;
        ListNode lastNew = current.prev;
        
        last.next = first;
        first.prev = last;
        
        first = firstNew;
        last = lastNew;
        
        first.prev = null;
        last.next = null;
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