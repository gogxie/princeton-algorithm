/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 05/31/2019
 *  Description: SeamCarver.java
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.util.Arrays;

public class SeamCarver {

    // create a seam carver object based on the given picture
    private Picture picture;
    private double[][] energyMatrix;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new java.lang.IllegalArgumentException("null");
        this.picture = new Picture(picture);
        energyMatrix = new double[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energyMatrix[i][j] = energyCal(i, j);
            }
        }
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new java.lang.IllegalArgumentException("index");
        return energyMatrix[x][y];
    }

    // energy of pixel at column x and row y
    private double energyCal(int x, int y) {
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return 1000.0;

        int left = picture.getRGB(x - 1, y);
        int right = picture.getRGB(x + 1, y);
        int above = picture.getRGB(x, y - 1);
        int below = picture.getRGB(x, y + 1);

        double res = 0;
        res += Math.pow(((right >> 16) & 0xFF) - ((left >> 16) & 0xFF), 2);
        res += Math.pow(((above >> 16) & 0xFF) - ((below >> 16) & 0xFF), 2);
        res += Math.pow(((right >> 8) & 0xFF) - ((left >> 8) & 0xFF), 2);
        res += Math.pow(((above >> 8) & 0xFF) - ((below >> 8) & 0xFF), 2);
        res += Math.pow((right & 0xFF) - (left & 0xFF), 2);
        res += Math.pow((above & 0xFF) - (below & 0xFF), 2);
        return Math.sqrt(res);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int m = picture.width();
        int n = picture.height();

        double[][] distance = new double[m][n];
        int[][] from = new int[m][n];

        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                if (x == 0) {
                    distance[x][y] = energyMatrix[x][y];
                    from[x][y] = -1;
                }
                else {
                    int start = Math.max(y - 1, 0);
                    int end = Math.min(y + 1, n - 1);
                    int p = start;
                    double d = distance[x - 1][start];
                    for (int k = start + 1; k <= end; k++) {
                        if (distance[x - 1][k] < d) {
                            d = distance[x - 1][k];
                            p = k;
                        }
                    }
                    distance[x][y] = energyMatrix[x][y] + d;
                    from[x][y] = p;
                }
            }
        }
        int pos = 0;
        double d = distance[m - 1][0];

        for (int y = 1; y < n; y++) {
            if (distance[m - 1][y] < d) {
                d = distance[m - 1][y];
                pos = y;
            }
        }
        int[] res = new int[m];
        for (int x = m - 1; x >= 0; x--) {
            res[x] = pos;
            pos = from[x][pos];
        }
        return res;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int m = picture.width();
        int n = picture.height();

        double[][] distance = new double[m][n];
        int[][] from = new int[m][n];

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                if (y == 0) {
                    distance[x][y] = energyMatrix[x][y];
                    from[x][y] = -1;
                }
                else {
                    int start = Math.max(x - 1, 0);
                    int end = Math.min(x + 1, m - 1);
                    int p = start;
                    double d = distance[start][y - 1];
                    for (int k = start + 1; k <= end; k++) {
                        if (distance[k][y - 1] < d) {
                            d = distance[k][y - 1];
                            p = k;
                        }
                    }
                    distance[x][y] = energyMatrix[x][y] + d;
                    from[x][y] = p;
                }
            }
        }
        int pos = 0;
        double d = distance[0][n - 1];

        for (int x = 1; x < m; x++) {
            if (distance[x][n - 1] < d) {
                d = distance[x][n - 1];
                pos = x;
            }
        }
        int[] res = new int[n];
        for (int y = n - 1; y >= 0; y--) {
            res[y] = pos;
            pos = from[pos][y];
        }
        return res;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        int m = picture.width();
        int n = picture.height();
        if (seam == null)
            throw new java.lang.IllegalArgumentException("null");
        if (n <= 1)
            throw new java.lang.IllegalArgumentException("small");
        if (seam.length != m)
            throw new java.lang.IllegalArgumentException("mismatch");

        if (seam[0] < 0 || seam[0] >= n)
            throw new java.lang.IllegalArgumentException("mismatch");

        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= n)
                throw new java.lang.IllegalArgumentException("mismatch");
            if (Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new java.lang.IllegalArgumentException("mismatch");
        }

        Picture tp = new Picture(m, n - 1);
        double[][] energyTemp = new double[m][n - 1];
        for (int x = 0; x < m; x++) {
            int flag = 0;
            for (int y = 0; y < n - 1; y++) {
                if (seam[x] == y) {
                    flag = 1;
                }
                tp.setRGB(x, y, picture.getRGB(x, y + flag));
                energyTemp[x][y] = energyMatrix[x][y + flag];
            }
        }
        picture = tp;
        energyMatrix = energyTemp;
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n - 1; y++) {
                if (seam[x] == y) {
                    energyMatrix[x][y] = energyCal(x, y);
                    if (x - 1 >= 0)
                        energyMatrix[x - 1][y] = energyCal(x - 1, y);
                    if (x + 1 < width())
                        energyMatrix[x + 1][y] = energyCal(x + 1, y);
                    if (y - 1 >= 0)
                        energyMatrix[x][y - 1] = energyCal(x, y - 1);
                }
            }
            if (seam[x] == n - 1) {
                energyMatrix[x][n - 2] = energyCal(x, n - 2);
            }
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        int m = picture.width();
        int n = picture.height();
        if (seam == null)
            throw new java.lang.IllegalArgumentException("null");
        if (m <= 1)
            throw new java.lang.IllegalArgumentException("small");
        if (seam.length != n)
            throw new java.lang.IllegalArgumentException("mismatch");

        if (seam[0] < 0 || seam[0] >= m)
            throw new java.lang.IllegalArgumentException("mismatch");

        for (int i = 1; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= m)
                throw new java.lang.IllegalArgumentException("mismatch");
            if (Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new java.lang.IllegalArgumentException("mismatch");
        }

        Picture tp = new Picture(m - 1, n);
        double[][] energyTemp = new double[m - 1][n];
        for (int y = 0; y < n; y++) {
            int flag = 0;
            for (int x = 0; x < m - 1; x++) {
                if (seam[y] == x) {
                    flag = 1;
                }
                tp.setRGB(x, y, picture.getRGB(x + flag, y));
                energyTemp[x][y] = energyMatrix[x + flag][y];
            }
        }
        picture = tp;
        energyMatrix = energyTemp;
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m - 1; x++) {
                if (seam[y] == x) {
                    energyMatrix[x][y] = energyCal(x, y);
                    if (y - 1 >= 0)
                        energyMatrix[x][y - 1] = energyCal(x, y - 1);
                    if (y + 1 < height())
                        energyMatrix[x][y + 1] = energyCal(x, y + 1);
                    if (x - 1 >= 0)
                        energyMatrix[x - 1][y] = energyCal(x - 1, y);
                }
            }
            if (seam[y] == m - 1)
                energyMatrix[m - 2][y] = energyCal(m - 2, y);
        }
    }

    //  unit testing (required)
    public static void main(String[] args) {
        // nothing here.
        SeamCarver sc = new SeamCarver(new Picture("3x4.png"));
        for (int i = 0; i < sc.energyMatrix.length; i++)
            System.out.println(Arrays.toString(sc.energyMatrix[i]));
    }

}
