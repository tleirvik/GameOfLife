package Model.util;

import Model.GameOfLife.Boards.Board;


/**
 * Created by tleirvik on 02.05.2016.
 */
public class DepthFirstSearch {
    private boolean[] marked;
    private int counter;

    public DepthFirstSearch(Board b, int s) {
        marked = new boolean[b.V()];
        dfs(G, s);
    }

    private void dfs(Board b, int v) {
        counter++;
        marked[v] = true;
        for (int w : b.adj(v)) {
            if (!marked[w]) {
                dfs(b, w);
            }
        }
    }
    public boolean marked(int v) {
        return marked[v];
    }


    public int count() {
        return counter;
    }
}
