/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 06/08/2019
 *  Description: CircularSuffixArray.java
 **************************************************************************** */

import java.util.Arrays;

public class CircularSuffixArray {
    // circular suffix array of s
    private final Integer[] suffix;
    private final int len;

    public CircularSuffixArray(String s) {
        if (s == null)
            throw new java.lang.IllegalArgumentException("null");

        len = s.length();
        suffix = new Integer[len];
        for (int i = 0; i < len; i++) {
            suffix[i] = i;
        }
        Arrays.sort(suffix, (a, b) -> ((s.substring(a, len) + s.substring(0, a))
                .compareTo((s.substring(b, len) + s.substring(0, b)))));
    }

    // length of s
    public int length() {
        return len;
    }
    // returns index of ith sorted suffix

    public int index(int i) {
        if (i < 0 || i >= len)
            throw new java.lang.IllegalArgumentException("out of range");
        return suffix[i];
    }

    // unit testing (required)
    public static void main(String[] args) {

        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++)
            System.out.println(csa.index(i));
    }
}
