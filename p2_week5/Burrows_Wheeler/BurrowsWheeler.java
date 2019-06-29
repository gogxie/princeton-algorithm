/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 06/08/2019
 *  Description: BurrowsWheeler.java
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {

        final Integer[] suffix;
        final int len;
        List<Byte> in = new ArrayList<Byte>();
        while (!BinaryStdIn.isEmpty()) {
            in.add(BinaryStdIn.readByte());
        }

        len = in.size();

        suffix = new Integer[len];
        for (int i = 0; i < len; i++) {
            suffix[i] = i;
        }
        Arrays.sort(suffix, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int idx1 = o1;
                int idx2 = o2;
                for (int i = 0; i < len; i++) {
                    int i1 = in.get((idx1 + i) % len) & 0xff;
                    int i2 = in.get((idx2 + i) % len) & 0xff;

                    if (i1 < i2)
                        return -1;
                    if (i1 > i2)
                        return 1;
                }
                return 0;
            }
        });
        int pos = 0;
        while (suffix[pos] != 0)
            pos++;
        BinaryStdOut.write(pos);
        for (int i = 0; i < len; i++) {
            BinaryStdOut.write(in.get((suffix[i] + (2 * len - 1)) % len));
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int start = BinaryStdIn.readInt();
        List<Pair> in = new ArrayList<>();
        int idx = 0;
        while (!BinaryStdIn.isEmpty()) {
            in.add(new Pair(BinaryStdIn.readByte(), idx++));
        }

        Collections.sort(in);
        int cur = start;
        BinaryStdOut.write(in.get(cur).info);
        cur = in.get(cur).next;
        int cnt = 1;

        while (cnt < in.size()) {
            BinaryStdOut.write(in.get(cur).info);
            cur = in.get(cur).next;
            cnt++;
        }
        BinaryStdOut.flush();
    }

    private static class Pair implements Comparable<Pair> {
        private final byte info;
        private final int next;

        public Pair(byte info, int next) {
            this.info = info;
            this.next = next;
        }

        @Override
        public int compareTo(Pair that) {
            int i1 = this.info & 0xff;
            int i2 = that.info & 0xff;

            return i1 - i2;
        }
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        if (args[0].equals("+"))
            inverseTransform();
    }
}
