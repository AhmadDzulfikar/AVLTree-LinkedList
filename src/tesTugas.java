import java.util.*;

class tesTugas {
    static void findMystery(int node, int visited[], ArrayList<ArrayList<Integer>> adj,
            Stack<Integer> st) {
        visited[node] = 1;
        for (Integer it : adj.get(node)) {
            if (visited[it] == 0) {
                findMystery(it, visited, adj, st);
            }
        }
        st.push(node);
    }

    static int[] mystery(int N, ArrayList<ArrayList<Integer>> adj) {
        Stack<Integer> st = new Stack<Integer>();
        int visited[] = new int[N];
        for (int i = 0; i < N; i++) {
            if (visited[i] == 0) {
                findMystery(i, visited, adj, st);
            }
        }
        int arr[] = new int[N];
        int ind = 0;
        while (!st.isEmpty()) {
            arr[ind++] = st.pop();
        }
        return arr;
    }

    public static void main(String args[]) {
        // dalam method main ini dilakukan konstruksi graf.
        ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
        int n = 5;
        for (int i = 0; i < n; i++) {
            ArrayList<Integer> arr = new ArrayList<>();
            adj.add(arr);
        }
        adj.get(4).add(0);
        adj.get(4).add(2);
        adj.get(3).add(0);
        adj.get(3).add(1);
        adj.get(1).add(0);
        adj.get(0).add(2);
        int res[] = mystery(5, adj);
        System.out.println("Keluaran pemanggilan mystery:");
        for (int i = 0; i < res.length; i++) {
            System.out.print(res[i] + " ");
        }
    }
}