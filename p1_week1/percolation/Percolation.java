/* *****************************************************************************
 *  Name: Guodong Xie
 *  Date: 5/18/2019
 *  Description: Percolation.java
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // create n-by-n grid, with all sites blocked
    private final WeightedQuickUnionUF uf1;
    private final WeightedQuickUnionUF uf2;

    private boolean[][] grid;
    private final int n;
    private int openSite;

    public Percolation(int n) {
        if (n <= 0)
            throw new java.lang.IllegalArgumentException("Index Error!");
        this.uf1 = new WeightedQuickUnionUF((n + 1) * n);
        this.uf2 = new WeightedQuickUnionUF((n + 1) * n);

        this.grid = new boolean[n + 1][n + 1];
        this.n = n;
        this.openSite = 0;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new java.lang.IllegalArgumentException("Index Error!");
        }
        if (isOpen(row, col))
            return;
        grid[row][col] = true;
        openSite++;
        int k0 = ij2k(row, col);
        int k1 = ij2k(row - 1, col);
        int k2 = ij2k(row + 1, col);
        int k3 = ij2k(row, col - 1);
        int k4 = ij2k(row, col + 1);
        if ((row - 1 > 0 && isOpen(row - 1, col))) {
            uf1.union(k0, k1);
            uf2.union(k0, k1);
        }
        if (row + 1 <= n && isOpen(row + 1, col)) {
            uf1.union(k0, k2);
            uf2.union(k0, k2);
        }
        if (col - 1 > 0 && isOpen(row, col - 1)) {
            uf1.union(k0, k3);
            uf2.union(k0, k3);
        }
        if (col + 1 <= n && isOpen(row, col + 1)) {
            uf1.union(k0, k4);
            uf2.union(k0, k4);
        }
        if (row == 1) {
            uf1.union(k0, 0);
            uf2.union(k0, 0);
        }
        if (row == n)
            uf2.union(k0, 1);
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new java.lang.IllegalArgumentException("Index Error!");
        }
        return grid[row][col];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new java.lang.IllegalArgumentException("Index Error!");
        }
        int k = ij2k(row, col);
        return uf1.connected(k, 0);
    }

    public int numberOfOpenSites() {
        return openSite;
    }

    public boolean percolates() {
        return uf2.connected(0, 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        // nothing to do here
    }

    private int ij2k(int i, int j) {
        return i * n + j - 1;
    }
}
