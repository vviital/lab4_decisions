package model;

/**
 * Created by vviital on 20/09/15.
 */
public class Pair{

    public long first;

    public long second;

    public Pair(){

    }

    public Pair(long first, long second){
        this.first = first;
        this.second = second;
    }

    public Pair add(Pair obj){
        return new Pair(first + obj.first, second + obj.second);
    }

}
