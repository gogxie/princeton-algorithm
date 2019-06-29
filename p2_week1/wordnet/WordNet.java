/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 05/27/2019
 *  Description: WordNet.java
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WordNet {
    private final Map<String, List<Integer>> map = new HashMap<>();
    private final Map<Integer, String> mapr = new HashMap<>();
    private final Digraph G;
    private String s1;
    private String s2;
    private int dis;
    private String word;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new java.lang.IllegalArgumentException("error");

        In in = new In(synsets);
        int max = 0;
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] tokens = line.split(",");
            int idx = Integer.parseInt(tokens[0]);
            mapr.put(idx, tokens[1]);
            String[] keys = tokens[1].split(" ");
            for (String key : keys) {
                if (!map.containsKey(key))
                    map.put(key, new ArrayList<Integer>());
                map.get(key).add(idx);
            }
            max = Math.max(max, idx);
        }
        G = new Digraph(max + 1);
        int[] outdegs = new int[max + 1];

        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] tokens = line.split(",");
            int n = tokens.length;
            int s = Integer.parseInt(tokens[0]);
            for (int i = 1; i < n; i++) {
                int d = Integer.parseInt(tokens[i]);
                G.addEdge(s, d);
                outdegs[s]++;
            }
        }
        DirectedCycle dc = new DirectedCycle(G);
        if (dc.cycle)
            throw new java.lang.IllegalArgumentException("cycle exist");
        int cnt = 0;
        for (int i = 0; i < outdegs.length; i++) {
            if (outdegs[i] == 0) {
                cnt++;
                if (cnt == 2)
                    throw new java.lang.IllegalArgumentException("multiple roots");
            }
        }
    }

    private class DirectedCycle {
        private boolean[] marked;        // marked[v] = has vertex v been marked?
        private boolean[] onStack;       // onStack[v] = is vertex on the stack?
        private boolean cycle;    // directed cycle (or null if no such cycle)

        public DirectedCycle(Digraph g) {
            marked = new boolean[g.V()];
            onStack = new boolean[g.V()];
            cycle = false;
            for (int v = 0; v < g.V(); v++)
                if (!marked[v] && !cycle)
                    dfs(g, v);
        }

        private void dfs(Digraph g, int v) {
            onStack[v] = true;
            marked[v] = true;
            for (int w : g.adj(v)) {
                if (cycle) return;

                if (!marked[w])
                    dfs(g, w);
                else if (onStack[w]) {
                    cycle = true;
                    return;
                }
            }
            onStack[v] = false;
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new java.lang.IllegalArgumentException("error");
        return map.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB))
            throw new java.lang.IllegalArgumentException("error");
        if (nounA.equals(s1) && nounB.equals(s2))
            return dis;
        call(nounA, nounB);
        return dis;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB))
            throw new java.lang.IllegalArgumentException("error");
        if (nounA.equals(s1) && nounB.equals(s2))
            return word;
        call(nounA, nounB);
        return word;
    }

    private void call(String nounA, String nounB) {
        List<Integer> vs = map.get(nounA);
        List<Integer> ws = map.get(nounB);
        int len = Integer.MAX_VALUE;
        int lca = -1;

        HashMap<Integer, Integer> pv = new HashMap<>();
        HashMap<Integer, Integer> pw = new HashMap<>();

        LinkedList<Integer> q1 = new LinkedList<>();
        LinkedList<Integer> q2 = new LinkedList<>();

        for (Integer v : vs) {
            if (v == null || v < 0 || v >= G.V())
                throw new java.lang.IllegalArgumentException("out of range");

            q1.offerLast(v);
            pv.put(v, 0);
        }
        for (Integer w : ws) {
            if (w == null || w < 0 || w >= G.V())
                throw new java.lang.IllegalArgumentException("out of range");
            q2.offerLast(w);
            if (pv.containsKey(w)) {
                s1 = nounA;
                s2 = nounB;
                dis = 0;
                word = mapr.get(w);
                return;
            }
            pw.put(w, 0);
        }
        while (!q1.isEmpty() || !q2.isEmpty()) {
            if (!q1.isEmpty()) {
                int t = q1.pollFirst();
                for (int i : G.adj(t)) {
                    int dis = pv.get(t) + 1;
                    if (!pv.containsKey(i) || pv.get(i) > dis) {
                        pv.put(i, dis);
                        if (pw.containsKey(i) && pv.get(i) + pw.get(i) < len) {
                            len = pv.get(i) + pw.get(i);
                            lca = i;
                        }
                        if (!pw.containsKey(i) || pv.get(i) <= len)
                            q1.offerLast(i);
                    }
                }
            }

            if (!q2.isEmpty()) {
                int t = q2.pollFirst();
                for (int i : G.adj(t)) {
                    int dis = pw.get(t) + 1;
                    if (!pw.containsKey(i) || pw.get(i) > dis) {
                        pw.put(i, dis);
                        if (pv.containsKey(i) && pv.get(i) + pw.get(i) < len) {
                            len = pv.get(i) + pw.get(i);
                            lca = i;
                        }
                        if (!pv.containsKey(i) || pw.get(i) <= len)
                            q2.offerLast(i);
                    }
                }
            }
        }

        s1 = nounA;
        s2 = nounB;
        dis = len;
        word = mapr.get(lca);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // client
        String s1 = "prayer";
        String s2 = "Hugo_Wolf";
        WordNet test = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println(test.distance(s1, s2));
        System.out.println(test.sap(s1, s2));
    }
}
