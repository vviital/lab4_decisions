import model.Edge;
import model.Flow;
import model.Pair;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by vviital on 20/09/15.
 */
public class Runner {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out);
        new Solver().solve(in, out);
        in.close();
        out.close();
    }
}

class Solver{

    public void solve(Scanner in, PrintWriter out){
        int n = in.nextInt();
        int[][] arr = new int[n][n];
        int[] sum = new int[n];
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j) {
                arr[i][j] = in.nextInt();
                sum[i] += arr[i][j];
            }
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j)
                arr[i][j] = sum[i] - arr[i][j];
        List<Edge> edge = new ArrayList<>();
        int from = 0, sink = 1 + n + n;
        for(int i = 0; i < n; i++){
            Edge cur = new Edge(0, i + 1, 1, 0);
            edge.add(cur);
        }
        for(int i = 0; i < n; ++i){
            Edge cur = new Edge(n + i + 1, sink, 1, 0);
            edge.add(cur);
        }
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j){
                int toi = i + 1;
                int toj = j + n + 1;
                Edge cur = new Edge(toi, toj, 1, arr[i][j]);
                edge.add(cur);
            }
        Flow f = new Flow(edge, 2 + n + n, from, sink);
        Pair ans = f.getFlow();
        out.println(ans.second);
    }
}
