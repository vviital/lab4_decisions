package model;

import java.util.*;

/**
 * Created by vviital on 20/09/15.
 */
public class Flow {

    private List<Edge> edges;

    private List<Integer>[] indexes;

    private long[] dist;

    private int[] pred;

    private long[] phi;

    private int n;

    private int from;

    private int sink;

    private List<FlowMatrix> memory;

    private Flow(){

    }


    private void addEdge(int from, int to, long cap, long cost){
        this.indexes[from].add(this.edges.size());
        this.edges.add(new Edge(from, to, cap, cost));
        this.indexes[to].add(this.edges.size());
        this.edges.add(new Edge(to, from, 0, -cost));
        this.memory = new ArrayList();
    }

    public Flow(List<Edge> e, int n, int from, int sink){
        this.n = n;
        this.indexes = new ArrayList[n];
        this.edges = new ArrayList();
        for(int i = 0; i < indexes.length; ++i) this.indexes[i] = new ArrayList();
        for(Edge x : e){
            addEdge(x.getFrom(), x.getTo(), x.getCap(), x.getCost());
        }
        this.phi = new long[n];
        this.dist = new long[n];
        this.pred = new int[n];
        this.from = from;
        this.sink = sink;
    }

    Pair dij(int from, int to){
        PriorityQueue<Pair> q = new PriorityQueue<Pair>(1, (a, b) -> {
            if (a.first != b.first){
                return Long.compare(a.second, b.second);
            }
            return -Long.compare(a.first, b.first);
        });
        for(int i = 0; i < this.dist.length; ++i) dist[i] = Long.MAX_VALUE;
        for(int i = 0; i < this.pred.length; ++i) pred[i] = -1;
        dist[from] = 0;
        q.add(new Pair(0, from));
        while(q.size() != 0){
            long d = q.element().first;
            int v = (int)q.element().second;
            q.remove();
            if (dist[v] != d) continue;
            for(int x : indexes[v]){
                Edge e = this.edges.get(x);
                int tov = e.getTo();
                long w = e.getCost() + phi[v] - phi[tov];
                if (dist[tov] > dist[v] + w && e.getCap() > e.getFlow()){
                    dist[tov] = dist[v] + w;
                    pred[tov] = x;
                    q.add(new Pair(dist[tov], tov));
                }
            }
        }
        if (dist[to] == Long.MAX_VALUE) return new Pair(0, 0);
        int cur = to;
        Pair ans = new Pair(1, 0);
        while(cur != from){
            int x = pred[cur];
            Edge e = edges.get(x);
            ans.second += e.getCost();
            this.edges.get(x).addFlow(1);
            this.edges.get(x ^ 1).addFlow(-1);
            cur = e.getFrom();
        }
        for(int i = 0; i < this.phi.length; ++i)
            if (dist[i] != Long.MAX_VALUE)
                phi[i] += dist[i];
        return ans;

    }

    private Pair minCost(int from, int sink){
        Pair answer = new Pair(0, 0);
        for(int i = 0; i < this.phi.length; ++i) phi[i] = 0;
        while(true){
            Pair curans = dij(from, sink);
            if (curans.first == 0) break;
            answer = answer.add(curans);
        }
        return answer;
    }

    public Pair getFlow(){
        return minCost(from, sink);
    }

    private void persist(long fl){
        FlowMatrix matrix = new FlowMatrix(this.n);
        for(Edge x : this.edges){
            int f = x.getFrom();
            int t = x.getTo();
            long cost = x.getCost();
            long flow = x.getFlow();
            matrix.addFlow(f, t, flow);
            matrix.setCost(f, t, cost);
        }
        for(int i = 0; i < this.dist.length; ++i){
            matrix.setDist(i, dist[i]);
        }
        matrix.setFlow(fl);
        memory.add(matrix);
    }

    public FlowMatrix getStep(int i){
        return this.memory.get(i);
    }

    public int getStepNumber(){
        return this.memory.size();
    }

}