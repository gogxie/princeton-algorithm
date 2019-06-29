/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 05/24/2019
 *  Description: board.java
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.Iterator;

public class Board {
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    private final int n;
    private final int[][] bd;
    private final int hammingValue;
    private final int manhattanValue;
    private final int x0;
    private final int y0;
    private Board[] neighbor;

    public Board(int[][] blocks) {
        if (blocks.length < 2 || blocks.length != blocks[0].length)
            throw new java.lang.IllegalArgumentException("error");
        n = blocks.length;
        bd = new int[n][n];
        int xx0 = 0;
        int yx0 = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                bd[i][j] = blocks[i][j];
                if (bd[i][j] == 0) {
                    xx0 = i;
                    yx0 = j;
                }
            }
        }
        this.x0 = xx0;
        this.y0 = yx0;
        int hv = 0;
        int mv = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (bd[i][j] == 0)
                    continue;
                int ii = (bd[i][j] - 1) / n;
                int jj = (bd[i][j] - 1) % n;
                if (i == ii && j == jj)
                    continue;
                hv++;
                mv += (Math.abs(i - ii) + Math.abs(j - jj));
            }
        }
        hammingValue = hv;
        manhattanValue = mv;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        return hammingValue;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattanValue;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hammingValue == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] bd2 = new int[n][n];
        arrayCopy(bd, bd2);
        if (bd2[0][0] == 0 || bd2[0][1] == 0)
            swap(bd2, 1, 0, 1, 1);
        else
            swap(bd2, 0, 0, 0, 1);
        return new Board(bd2);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y)
            return true;

        if (y == null || y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;
        if (this.bd.length != that.bd.length || this.bd[0].length != that.bd[0].length)
            return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (bd[i][j] != that.bd[i][j])
                    return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int cnt = 4;
        if (x0 == 0 || x0 == n - 1) cnt--;
        if (y0 == 0 || y0 == n - 1) cnt--;
        neighbor = new Board[cnt];
        int[][] nei;
        int cur = 0;
        int[][] directions = new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] direction : directions) {
            int xx = x0 + direction[0];
            int yy = y0 + direction[1];
            if (xx >= 0 && xx < n && yy >= 0 && yy < n) {
                nei = new int[n][n];
                arrayCopy(bd, nei);
                swap(nei, x0, y0, xx, yy);
                neighbor[cur++] = new Board(nei);
            }
        }
        return () -> (new NeighborIterator());
    }

    private class NeighborIterator implements Iterator<Board> {

        private final int cnt;
        private int cur;

        public NeighborIterator() {
            cnt = neighbor.length;
            cur = 0;
        }

        @Override
        public boolean hasNext() {
            return cur != cnt;
        }

        @Override
        public Board next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException("error");
            Board t = neighbor[cur++];
            return t;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\n", n));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (bd[i][j] < 10) sb.append(" " + bd[i][j] + " ");
                else sb.append(bd[i][j] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void arrayCopy(int[][] aSource, int[][] aDestination) {
        for (int i = 0; i < aSource.length; i++) {
            System.arraycopy(aSource[i], 0, aDestination[i], 0, aSource[i].length);
        }
    }

    private void swap(int[][] b, int i, int j, int l, int m) {
        int temp = b[i][j];
        b[i][j] = b[l][m];
        b[l][m] = temp;
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        for (Board brd : initial.neighbors()) {
            System.out.println(brd);
            System.out.println(brd.hamming());
        }
        // System.out.println(initial.toString());
        // System.out.println(initial.hamming());
        // System.out.println(initial.manhattan());
        // System.out.println(initial.twin().toString());
    }

}
