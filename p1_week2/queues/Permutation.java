/* *****************************************************************************
 *  Name: Guodong
 *  Date: 05/22/2018
 *  Description: Permuation.java
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            rq.enqueue(str);
        }
        int n = Integer.parseInt(args[0]);
        Iterator<String> it = rq.iterator();
        while (n > 0) {
            System.out.println(it.next());
            n--;
        }
    }
}
