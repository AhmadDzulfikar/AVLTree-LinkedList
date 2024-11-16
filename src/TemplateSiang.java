import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.StringTokenizer;

public class TemplateSiang {
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
            //TODO:  process inputs
        }
        

        int Q = in.nextInteger();
        for (int i = 0; i < Q; i++) {
            String query = in.next();
            switch (query) {
                case "A":
                    //TODO: add file to tree

                    break;
                case "D":
                    //TODO: delete file from tree
                    
                    break;
                case "P":
                    //TODO: print tree
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

class File{
   // TODO: Implement File class
}

class Node {
    // TODO: modify attributes as needed
    File file;
    Node left, right;

    Node(File file) {
        this.file = file;
    }
}



class AVLTree{
    // TODO: modify attributes as needed
    Node root;
   
    
    Node findParent(Node node, File file) {
        // TODO: implement this method
        return null;
    }
    


    Node insert(Node node, File file) {
       // TODO: implement this method
       return null;
    }

    Node delete(Node root, File file) {
       // TODO: implement this method
       return null;
    }

   

    int getBalance(Node N) {
        // TODO: implement this method
        return 0;
    }

    Node findSuccessor(Node node){
        // TODO: implement this method
        return null;
    }



    Node singleRightRotate(Node y) {
       // TODO: implement this method
       return null;
    }

    Node singleLeftRotate(Node x) {
        // TODO: implement this method
        return null;
    }
}