package model;

import javafx.util.Pair;

import java.util.*;

/**
 * Created by vviital on 20/09/15.
 */
public class Flow {

    private List<Edge> edges;

    private List<Integer>[] indexes;

    private int[] dist;

    private int[] phi;

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
        this.phi = new int[n];
        this.dist = new int[n];
        this.from = from;
        this.sink = sink;
    }

    private Pair<Long, Long> minCost(int from, int sink){
        Pair<Long, Long> answer = new Pair(0, 0);
        return answer;
    }

    public Pair<Long, Long> getFlow(){
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