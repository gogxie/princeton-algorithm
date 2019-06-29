/* *****************************************************************************
 *  Name: Guodong Xie
 *  Date: 5/18/2019
 *  Description: PercolationStats.java
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;


public class PercolationStats {
    // perform trials independent experiments on an n-by-n grid
    private static final double CONFIDENCE_95 = 1.96;
    private final double mean;
    private final double std;
    private final double confHi;
    private final double confLow;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException("Index Error!");
        }
        int[] order = new int[n * n];
        for (int i = 0; i < n * n; i++)
            order[i] = i;
        double[] r = new double[trials];
        for (int i = 0; i < trials; i++) {
            StdRandom.shuffle(order);
            Percolation p = new Percolation(n);
            for (int j = 0; j < order.length && (!p.percolates()); j++) {
                int row = order[j] / n + 1;
                int col = order[j] % n + 1;
                p.open(row, col);
            }
            r[i] = p.numberOfOpenSites() * 1.0 / (n * n);
        }
        this.mean = StdStats.mean(r);
        this.std = StdStats.stddev(r);
        this.confLow = mean - CONFIDENCE_95 * std / Math.sqrt(trials);
        this.confHi = mean + CONFIDENCE_95 * std / Math.sqrt(trials);

    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return std;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return confLow;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confHi;
    }

    // test client (described below)
    public static void main(String[] args) {
        Stopwatch sw = new Stopwatch();
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]),
                                                   Integer.parseInt(args[1]));
        System.out.printf("mean                    = %s\n", String.valueOf(ps.mean()));
        System.out.printf("stddev                  = %s\n", String.valueOf(ps.stddev()));
        System.out.printf("95%% confidence interval = [%s, %s]\n",
                          String.valueOf(ps.confidenceLo()),
                          String.valueOf(ps.confidenceHi()));
        System.out.printf("elapsedTime: %s\n", sw.elapsedTime());
    }
}
