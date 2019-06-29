/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 05/27/2019
 *  Description: SAP.java
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class SAP {

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new java.lang.IllegalArgumentException("null");
        this.G = G.reverse().reverse();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new java.lang.IllegalArgumentException("out of range");

        int[] pv = new int[G.V()];
        int[] pw = new int[G.V()];
        for (int i = 0; i < pv.length; i++) {
            pv[i] = Integer.MAX_VALUE;
            pw[i] = Integer.MAX_VALUE;
        }
        LinkedList<Integer> q = new LinkedList<>();
        q.offerLast(v);
        pv[v] = 0;
        while (!q.isEmpty()) {
            int t = q.pollFirst();
            for (int i : G.adj(t)) {
                int dis = pv[t] + 1;
                if (pv[i] > dis) {
                    pv[i] = dis;
                    q.offerLast(i);
                }
            }
        }

        q.offerLast(w);
        pw[w] = 0;
        while (!q.isEmpty()) {
            int t = q.pollFirst();
            for (int i : G.adj(t)) {
                int dis = pw[t] + 1;
                if (pw[i] > dis) {
                    pw[i] = dis;
                    q.offerLast(i);
                }
            }
        }

        int len = Integer.MAX_VALUE;
        for (int i = 0; i < pv.length; i++) {
            if (pv[i] != Integer.MAX_VALUE && pw[i] != Integer.MAX_VALUE) {
                int lent = pv[i] + pw[i];
                if (len > lent) {
                    len = lent;
                }
            }
        }
        return len == Integer.MAX_VALUE ? -1 : len;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new java.lang.IllegalArgumentException("out of range");

        int[] pv = new int[G.V()];
        int[] pw = new int[G.V()];
        for (int i = 0; i < pv.length; i++) {
            pv[i] = Integer.MAX_VALUE;
            pw[i] = Integer.MAX_VALUE;
        }
        LinkedList<Integer> q = new LinkedList<>();
        q.offerLast(v);
        pv[v] = 0;
        while (!q.isEmpty()) {
            int t = q.pollFirst();
            for (int i : G.adj(t)) {
                int dis = pv[t] + 1;
                if (pv[i] > dis) {
                    pv[i] = dis;
                    q.offerLast(i);
                }
            }
        }

        q.offerLast(w);
        pw[w] = 0;
        while (!q.isEmpty()) {
            int t = q.pollFirst();
            for (int i : G.adj(t)) {
                int dis = pw[t] + 1;
                if (pw[i] > dis) {
                    pw[i] = dis;
                    q.offerLast(i);
                }
            }
        }

        int len = Integer.MAX_VALUE;
        int lca = -1;
        for (int i = 0; i < pv.length; i++) {
            if (pv[i] != Integer.MAX_VALUE && pw[i] != Integer.MAX_VALUE) {
                int lent = pv[i] + pw[i];
                if (len > lent) {
                    len = lent;
                    lca = i;
                }
            }
        }
        return lca;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> vs, Iterable<Integer> ws) {
        if (vs == null || ws == null)
            throw new java.lang.IllegalArgumentException("null");

        int[] pv = new int[G.V()];
        int[] pw = new int[G.V()];
        for (int i = 0; i < pv.length; i++) {
            pv[i] = Integer.MAX_VALUE;
            pw[i] = Integer.MAX_VALUE;
        }
        LinkedList<Integer> q = new LinkedList<>();
        for (Integer v : vs) {
            if (v == null || v < 0 || v >= G.V())
                throw new java.lang.IllegalArgumentException("out of range");
            q.offerLast(v);
            pv[v] = 0;
        }
        while (!q.isEmpty()) {
            int t = q.pollFirst();
            for (int i : G.adj(t)) {
                int dis = pv[t] + 1;
                if (pv[i] > dis) {
                    pv[i] = dis;
                    q.offerLast(i);
                }
            }
        }
        for (Integer w : ws) {
            if (w == null || w < 0 || w >= G.V())
                throw new java.lang.IllegalArgumentException("out of range");
            q.offerLast(w);
            pw[w] = 0;
        }
        while (!q.isEmpty()) {
            int t = q.pollFirst();
            for (int i : G.adj(t)) {
                int dis = pw[t] + 1;
                if (pw[i] > dis) {
                    pw[i] = dis;
                    q.offerLast(i);
                }
            }
        }

        int len = Integer.MAX_VALUE;
        for (int i = 0; i < pv.length; i++) {
            if (pv[i] != Integer.MAX_VALUE && pw[i] != Integer.MAX_VALUE) {
                int lent = pv[i] + pw[i];
                if (len > lent) {
                    len = lent;
                }
            }
        }
        return len == Integer.MAX_VALUE ? -1 : len;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> vs, Iterable<Integer> ws) {
        if (vs == null || ws == null)
            throw new java.lang.IllegalArgumentException("null");

        int[] pv = new int[G.V()];
        int[] pw = new int[G.V()];
        for (int i = 0; i < pv.length; i++) {
            pv[i] = Integer.MAX_VALUE;
            pw[i] = Integer.MAX_VALUE;
        }
        LinkedList<Integer> q = new LinkedList<>();
        for (Integer v : vs) {
            if (v == null || v < 0 || v >= G.V())
                throw new java.lang.IllegalArgumentException("out of range");

            q.offerLast(v);
            pv[v] = 0;
        }
        while (!q.isEmpty()) {
            int t = q.pollFirst();
            for (int i : G.adj(t)) {
                int dis = pv[t] + 1;
                if (pv[i] > dis) {
                    pv[i] = dis;
                    q.offerLast(i);
                }
            }
        }
        for (Integer w : ws) {
            if (w == null || w < 0 || w >= G.V())
                throw new java.lang.IllegalArgumentException("out of range");
            q.offerLast(w);
            pw[w] = 0;
        }
        while (!q.isEmpty()) {
            int t = q.pollFirst();
            for (int i : G.adj(t)) {
                int dis = pw[t] + 1;
                if (pw[i] > dis) {
                    pw[i] = dis;
                    q.offerLast(i);
                }
            }
        }
        int len = Integer.MAX_VALUE;
        int lca = -1;
        for (int i = 0; i < pv.length; i++) {
            if (pv[i] != Integer.MAX_VALUE && pw[i] != Integer.MAX_VALUE) {
                int lent = pv[i] + pw[i];
                if (lent < len) {
                    len = lent;
                    lca = i;
                }
            }
        }
        return lca;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
