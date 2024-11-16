import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class LabEnam {
    private static InputReader in;
    private static PrintWriter out;
    private static AVLTree tree = new AVLTree();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInteger();
        for (int i = 0; i < N; i++) {
            int id = in.nextInteger();
            String name = in.next();
            FileEntry file = new FileEntry(id, name);
            tree.root = tree.insert(tree.root, file); // Initial tree population
        }

        int Q = in.nextInteger();
        for (int i = 0; i < Q; i++) {
            String query = in.next();
            switch (query) {
                case "A":
                    int addId = in.nextInteger();
                    String addName = in.next();
                    FileEntry addFile = new FileEntry(addId, addName);
                    tree.root = tree.insert(tree.root, addFile);
                    break;
                case "D":
                    int deleteId = in.nextInteger();
                    FileEntry deleteFile = new FileEntry(deleteId, null); // Only ID needed to delete
                    tree.root = tree.delete(tree.root, deleteFile);
                    break;
                case "P":
                    printTree(tree.root, "", true);
                    break;
            }
        }

        out.close();
    }

    static void printTree(Node currPtr, String indent, boolean last) {
        if (currPtr != null) {
            out.print(indent);
            if (last) {
                out.print("R----");
                indent += "   ";
            } else {
                out.print("L----");
                indent += "|  ";
            }
            out.println(currPtr.file);
            printTree(currPtr.left, indent, false);
            printTree(currPtr.right, indent, true);
        }
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

class FileEntry {
    int id;
    String name;

    FileEntry(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "File ID: " + id + ", Name: " + name;
    }
}

class Node {
    FileEntry file;
    Node left, right;
    int height;

    Node(FileEntry file) {
        this.file = file;
        this.height = 1;
    }
}

class AVLTree {
    Node root;

    Node insert(Node node, FileEntry file) {
        if (node == null) {
            return new Node(file);
        }

        if (file.id < node.file.id) {
            node.left = insert(node.left, file);
        } else if (file.id > node.file.id) {
            node.right = insert(node.right, file);
        } else {
            return node; // No duplicate IDs allowed
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    Node delete(Node node, FileEntry file) {
        if (node == null) {
            return null;
        }

        if (file.id < node.file.id) {
            node.left = delete(node.left, file);
        } else if (file.id > node.file.id) {
            node.right = delete(node.right, file);
        } else {
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node successor = minValueNode(node.right);
                node.file = successor.file;
                node.right = delete(node.right, successor.file);
            }
        }

        if (node == null) return node;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    int height(Node node) {
        return node == null ? 0 : node.height;
    }

    int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    Node balance(Node node) {
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.left) >= 0) {
                return rotateRight(node);
            } else {
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }
        } else if (balance < -1) {
            if (getBalance(node.right) <= 0) {
                return rotateLeft(node);
            } else {
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }
        }
        return node;
    }

    Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }
}
