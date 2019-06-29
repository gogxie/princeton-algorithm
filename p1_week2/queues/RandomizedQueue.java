/* *****************************************************************************
 *  Name: Guodong Xie
 *  Date: 05/21/2019
 *  Description:RandomizedQueue.java
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    // construct an empty randomized queue
    private Item[] q;
    private int ptr;
    private int capacity;

    public RandomizedQueue() {
        capacity = 1;
        q = (Item[]) new Object[capacity];
        ptr = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return ptr == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return ptr;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException("null item");
        if (size() == capacity - 1) {
            Item[] p = (Item[]) new Object[capacity * 2];
            for (int i = 0; i < ptr; i++)
                p[i] = q[i];
            p[ptr] = item;
            q = p;
            ptr++;
            capacity *= 2;
        }
        else {
            q[ptr] = item;
            ptr++;
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (size() == 0)
            throw new java.util.NoSuchElementException("empty");
        int t = StdRandom.uniform(size());
        Item item = q[t];
        q[t] = q[ptr - 1];
        q[ptr - 1] = null;
        ptr--;
        if (size() == capacity / 4) {
            Item[] p = (Item[]) new Object[capacity / 2];
            for (int i = 0; i < size(); i++) {
                p[i] = q[i];
            }
            q = p;
            capacity /= 2;
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size() == 0)
            throw new java.util.NoSuchElementException("emapty");
        int t = StdRandom.uniform(ptr);
        return q[t];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private final int[] order = new int[size()];
        private int t;

        public RandomIterator() {
            for (int i = 0; i < order.length; i++) {
                order[i] = i;
            }
            StdRandom.shuffle(order);
            t = 0;
        }

        @Override
        public boolean hasNext() {
            return t != size();
        }

        @Override
        public Item next() {
            if (t == size())
                throw new java.util.NoSuchElementException("no next");
            Item item = q[order[t]];
            t++;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++) {
            rq.enqueue(i);
        }
        System.out.println("Empty(): " + rq.isEmpty());
        System.out.println("size: " + rq.size());
        System.out.println("Sample: " + rq.sample());
        System.out.println("size: " + rq.size());

        Iterator<Integer> it = rq.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + "\t");
        }
        System.out.println();
        it = rq.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + "\t");
        }

        System.out.println("\nremove all");
        while (!rq.isEmpty()) {
            System.out.print(rq.dequeue() + "\t");
            System.out.print("\tSize: " + rq.size());
            System.out.println("\tCapacity: " + rq.capacity);
        }
    }
}
