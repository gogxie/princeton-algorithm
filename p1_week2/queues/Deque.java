/* *****************************************************************************
 *  Name: Guodong Xie
 *  Date: 5/20/2019
 *  Description: Deque
 **************************************************************************** */

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    // construct an empty deque

    private Node<Item> head;
    private Node<Item> tail;
    private int n;

    public Deque() {
        head = null;
        tail = null;
        n = 0;
    }

    private class Node<T> {
        private final T item;
        private Node<T> next;
        private Node<T> pre;

        public Node(T item) {
            this.item = item;
            this.next = null;
            this.pre = null;
        }
    }

    // is the deque empty?
    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException("null item!");
        n++;
        if (head == null) {
            head = new Node<Item>(item);
            tail = head;
        }
        else {
            Node<Item> oldhead = head;
            head = new Node<Item>(item);
            head.next = oldhead;
            oldhead.pre = head;
        }
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException("null item!");
        n++;
        if (head == null) {
            head = new Node<Item>(item);
            tail = head;
        }
        else {
            Node<Item> oldtail = tail;
            tail = new Node<Item>(item);
            oldtail.next = tail;
            tail.pre = oldtail;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (head == null)
            throw new java.util.NoSuchElementException("null to remove!");
        n--;
        Item item = head.item;
        if (head.next == null) {
            head = null;
            tail = null;
        }
        else {
            head = head.next;
            head.pre = null;
        }
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (size() == 0)
            throw new java.util.NoSuchElementException("null to remove!");
        n--;
        Item item = tail.item;
        if (head.next == null) {
            head = null;
            tail = null;
        }
        else {
            tail = tail.pre;
            tail.next = null;
        }
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Item> {
        private Node<Item> cur = head;

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public Item next() {
            if (cur == null)
                throw new java.util.NoSuchElementException("no next");
            Item t = cur.item;
            cur = cur.next;
            return t;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<Integer> dq = new Deque<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0)
                dq.addFirst(i);
            else
                dq.addLast(i);
        }
        System.out.println("Empty()?:" + dq.isEmpty());

        Iterator<Integer> it = dq.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + "\t");
        }
        System.out.print("\nsize:" + dq.size());

        System.out.println("\nremove first");
        dq.removeFirst();
        it = dq.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + "\t");
        }
        System.out.println("\nremove last");
        dq.removeLast();
        it = dq.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + "\t");
        }
        System.out.println("\nremove all");
        while (!dq.isEmpty()) {
            System.out.print(dq.removeFirst() + "\t");
            System.out.print(dq.removeLast() + "\t");

        }
    }
}
