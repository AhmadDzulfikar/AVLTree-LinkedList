package lab6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class labEnam {
    private static InputReader in;
    private static PrintWriter out;
    private static AVLTree tree = new AVLTree();
    private static int fileIdCounter = 1; 

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInteger();

        for (int i = 0; i < N; i++) {
            int size = in.nextInteger();
            int date = in.nextInteger();
            File file = new File(size, date, fileIdCounter++);
            tree.insert(file);
        }

        int Q = in.nextInteger();
        for (int i = 0; i < Q; i++) {
            String query = in.next();
            switch (query) {
                case "A":
                    int addSize = in.nextInteger();
                    int addDate = in.nextInteger();
                    File addFile = new File(addSize, addDate, fileIdCounter++);
                    int parentId = tree.insert(addFile);
                    out.println(parentId);
                    break;
                case "D":
                    int delSize = in.nextInteger();
                    int delDate = in.nextInteger();
                    File delFile = new File(delSize, delDate, -1); 
                    int deletedId = tree.delete(delFile);
                    out.println(deletedId);
                    break;
                case "P":
                    ArrayList<Integer> inOrderIds = tree.getInOrder();
                    if (inOrderIds.isEmpty()) {
                        out.println(-1);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (int id : inOrderIds) {
                            sb.append(id).append(" ");
                        }
                        // Remove the last space
                        sb.setLength(sb.length() - 1);
                        out.println(sb.toString());
                    }
                    break;
                default:
                    break;
            }
        }

        // Close output
        out.close();
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
                String line;
                try {
                    line = reader.readLine();
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

        public int nextInteger() {
            return Integer.parseInt(next());
        }
    }

    static class File implements Comparable<File> {
        int size;
        int date;
        int id;

        public File(int size, int date, int id) {
            this.size = size;
            this.date = date;
            this.id = id;
        }

        @Override
        public int compareTo(File other) {
            if (this.size != other.size) {
                return Integer.compare(this.size, other.size);
            }
            if (this.date != other.date) {
                return Integer.compare(this.date, other.date);
            }
            return Integer.compare(this.id, other.id); // To ensure uniqueness
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj instanceof File) {
                File other = (File) obj;
                return this.size == other.size && this.date == other.date && this.id == other.id;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return size * 31 + date * 17 + id;
        }
    }

    static class Node {
        File file;
        Node left, right;
        Node parent;
        int height;

        Node(File file) {
            this.file = file;
            this.height = 1;
            this.parent = null;
        }
    }

    static class AVLTree {
        private Node root;

        public int insert(File file) {
            InsertResult result = insertRecursive(root, file, null);
            root = result.node;
            if (result.insertedNode.parent == null) {
                return result.insertedNode.file.id; 
            } else {
                return result.insertedNode.parent.file.id;
            }
        }

        private class InsertResult {
            Node node;
            Node insertedNode;

            InsertResult(Node node, Node insertedNode) {
                this.node = node;
                this.insertedNode = insertedNode;
            }
        }

        private InsertResult insertRecursive(Node current, File file, Node parent) {
            if (current == null) {
                Node newNode = new Node(file);
                newNode.parent = parent;
                return new InsertResult(newNode, newNode);
            }

            if (file.compareTo(current.file) < 0) {
                InsertResult leftResult = insertRecursive(current.left, file, current);
                current.left = leftResult.node;
                if (current.left != null)
                    current.left.parent = current;
                current = updateAndBalance(current);
                return new InsertResult(current, leftResult.insertedNode);
            } else {
                InsertResult rightResult = insertRecursive(current.right, file, current);
                current.right = rightResult.node;
                if (current.right != null)
                    current.right.parent = current;
                current = updateAndBalance(current);
                return new InsertResult(current, rightResult.insertedNode);
            }
        }

        public int delete(File file) {
            DeleteResult result = deleteForRecursive(root, file);
            root = result.node;
            return result.deletedId;
        }

        private class DeleteResult {
            Node node;
            int deletedId;

            DeleteResult(Node node, int deletedId) {
                this.node = node;
                this.deletedId = deletedId;
            }
        }

        private DeleteResult deleteForRecursive(Node current, File file) {
            if (current == null) {
                return new DeleteResult(null, -1);
            }

            if (file.compareTo(current.file) < 0) {
                DeleteResult leftResult = deleteForRecursive(current.left, file);
                current.left = leftResult.node;
                if (current.left != null)
                    current.left.parent = current;
                current = updateAndBalance(current);
                return new DeleteResult(current, leftResult.deletedId);
            } else if (file.compareTo(current.file) > 0) {
                DeleteResult rightResult = deleteForRecursive(current.right, file);
                current.right = rightResult.node;
                if (current.right != null)
                    current.right.parent = current;
                current = updateAndBalance(current);
                return new DeleteResult(current, rightResult.deletedId);
            } else {
                int deletedId = current.file.id;
                if (current.left == null || current.right == null) {
                    Node temp = null;
                    if (current.left != null)
                        temp = current.left;
                    else
                        temp = current.right;

                    if (temp == null) {
                        return new DeleteResult(null, deletedId);
                    } else {
                        temp.parent = current.parent;
                        return new DeleteResult(temp, deletedId);
                    }
                } else {
                    Node successor = minValueNode(current.right);
                    current.file = successor.file;
                    DeleteResult successorResult = deleteForRecursive(current.right, successor.file);
                    current.right = successorResult.node;
                    if (current.right != null)
                        current.right.parent = current;
                    current = updateAndBalance(current);
                    return new DeleteResult(current, deletedId);
                }
            }
        }

        private Node minValueNode(Node node) {
            Node current = node;
            while (current.left != null)
                current = current.left;
            return current;
        }

        private Node updateAndBalance(Node node) {
            updateHeight(node);
            return balance(node);
        }

        private void updateHeight(Node node) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }

        private int height(Node node) {
            return node == null ? 0 : node.height;
        }

        private int getBalance(Node node) {
            return node == null ? 0 : height(node.left) - height(node.right);
        }

        private Node balance(Node node) {
            int balance = getBalance(node);

            if (balance > 1 && getBalance(node.left) >= 0)
                return singleRightRotate(node);

            if (balance > 1 && getBalance(node.left) < 0) {
                node.left = singleLeftRotate(node.left);
                return singleRightRotate(node);
            }

            if (balance < -1 && getBalance(node.right) <= 0)
                return singleLeftRotate(node);

            if (balance < -1 && getBalance(node.right) > 0) {
                node.right = singleRightRotate(node.right);
                return singleLeftRotate(node);
            }

            return node;
        }

        private Node singleRightRotate(Node y) {
            Node x = y.left;
            Node T2 = x.right;

            x.right = y;
            y.left = T2;

            x.parent = y.parent;
            y.parent = x;
            if (T2 != null)
                T2.parent = y;

            updateHeight(y);
            updateHeight(x);

            return x;
        }

        private Node singleLeftRotate(Node x) {
            Node y = x.right;
            Node T2 = y.left;

            y.left = x;
            x.right = T2;

            y.parent = x.parent;
            x.parent = y;
            if (T2 != null)
                T2.parent = x;

            updateHeight(x);
            updateHeight(y);

            return y;
        }

        public ArrayList<Integer> getInOrder() {
            ArrayList<Integer> result = new ArrayList<>();
            inOrderTraversal(root, result);
            return result;
        }

        private void inOrderTraversal(Node node, ArrayList<Integer> result) {
            if (node != null) {
                inOrderTraversal(node.left, result);
                result.add(node.file.id);
                inOrderTraversal(node.right, result);
            }
        }
    }
}
