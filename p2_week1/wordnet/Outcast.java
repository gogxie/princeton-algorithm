/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 05/27/2019
 *  Description: outcast.java
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    // constructor takes a WordNet object
    private final WordNet wn;


    public Outcast(WordNet wordnet) {
        wn = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns.length <= 0)
            throw new java.lang.IllegalArgumentException("null");
        int n = nouns.length;
        int[] disMatrix = new int[n];
        int max = 0;
        int idx = -1;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int d = wn.distance(nouns[i], nouns[j]);
                // System.out.printf("Distance btween %s and %s is: %d\n", nouns[i], nouns[j], d);
                disMatrix[i] += d;
                disMatrix[j] += d;
                if (disMatrix[i] > max) {
                    max = disMatrix[i];
                    idx = i;
                }
                if (disMatrix[j] > max) {
                    max = disMatrix[j];
                    idx = j;
                }
            }
        }
        return nouns[idx];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
