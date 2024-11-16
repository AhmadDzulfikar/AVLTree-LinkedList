import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class lab666 {
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
            MyFile file = new MyFile(id, name);
            tree.root = tree.insert(tree.root, file);
        }

        int Q = in.nextInteger();
        for (int i = 0; i < Q; i++) {
            String query = in.next();
            switch (query) {
                case "A":
                    // Tambahkan file ke tree
                    int addId = in.nextInteger();
                    String addName = in.next();
                    MyFile addFile = new MyFile(addId, addName);
                    tree.root = tree.insert(tree.root, addFile);
                    break;
                case "D":
                    // Hapus file dari tree
                    int delId = in.nextInteger();
                    MyFile delFile = new MyFile(delId, ""); // Name tidak diperlukan untuk delete
                    tree.root = tree.delete(tree.root, delFile);
                    break;
                case "P":
                    // Cetak tree
                    printTree(tree.root, "", true);
                    break;
                default:
                    out.println("Invalid query: " + query);
                    break;
            }
        }

        // Close output
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
                    String line = reader.readLine();
                    if (line == null)
                        return null;
                    tokenizer = new StringTokenizer(line);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInteger() {
            String token = next();
            if (token == null)
                throw new RuntimeException("No more tokens");
            return Integer.parseInt(token);
        }
    }
}

class MyFile implements Comparable<MyFile> {
    int id;
    String name;

    public MyFile(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int compareTo(MyFile other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return id + ": " + name;
    }
}

class Node {
    MyFile file;
    Node left, right;
    int height;

    Node(MyFile file) {
        this.file = file;
        this.left = this.right = null;
        this.height = 1; // New node is initially added at leaf
    }
}

class AVLTree {
    Node root;

    // Insert a file into the AVL tree
    Node insert(Node node, MyFile file) {
        if (node == null) {
            return new Node(file);
        }

        if (file.compareTo(node.file) < 0) {
            node.left = insert(node.left, file);
        } else if (file.compareTo(node.file) > 0) {
            node.right = insert(node.right, file);
        } else {
            // Duplicate IDs are not allowed
            return node;
        }

        // Update height and balance the node
        updateHeight(node);
        return balance(node);
    }

    // Delete a file from the AVL tree
    Node delete(Node node, MyFile file) {
        if (node == null) {
            return null;
        }

        if (file.compareTo(node.file) < 0) {
            node.left = delete(node.left, file);
        } else if (file.compareTo(node.file) > 0) {
            node.right = delete(node.right, file);
        } else {
            // Node dengan satu atau tanpa anak
            if (node.left == null || node.right == null) {
                Node temp = null;
                if (node.left != null) {
                    temp = node.left;
                } else {
                    temp = node.right;
                }

                // Kasus tanpa anak
                if (temp == null) {
                    node = null;
                } else { // Kasus satu anak
                    node = temp;
                }
            } else {
                // Node dengan dua anak: Dapatkan penerus inorder
                Node temp = findMin(node.right);
                node.file = temp.file;
                node.right = delete(node.right, temp.file);
            }
        }

        // Jika pohon hanya memiliki satu node
        if (node == null) {
            return null;
        }

        // Update height dan balance node
        updateHeight(node);
        return balance(node);
    }

    // Find the node with minimum value
    Node findMin(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    // Get the height of the node
    int height(Node node) {
        if (node == null)
            return 0;
        return node.height;
    }

    // Update the height of the node
    void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    // Get balance factor of node N
    int getBalance(Node N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    // Balance the node
    Node balance(Node node) {
        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && getBalance(node.left) >= 0)
            return singleRightRotate(node);

        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = singleLeftRotate(node.left);
            return singleRightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && getBalance(node.right) <= 0)
            return singleLeftRotate(node);

        // Right Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = singleRightRotate(node.right);
            return singleLeftRotate(node);
        }

        return node;
    }

    // Right rotate
    Node singleRightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Rotasi
        x.right = y;
        y.left = T2;

        // Update heights
        updateHeight(y);
        updateHeight(x);

        // Return akar baru
        return x;
    }

    // Left rotate
    Node singleLeftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Rotasi
        y.left = x;
        x.right = T2;

        // Update heights
        updateHeight(x);
        updateHeight(y);

        // Return akar baru
        return y;
    }

    // Find the parent of a node (optional, dapat digunakan jika diperlukan)
    Node findParent(Node node, MyFile file) {
        Node parent = null;
        while (node != null && file.compareTo(node.file) != 0) {
            parent = node;
            if (file.compareTo(node.file) < 0)
                node = node.left;
            else
                node = node.right;
        }
        return parent;
    }
}
