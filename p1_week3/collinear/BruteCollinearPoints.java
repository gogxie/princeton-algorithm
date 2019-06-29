/* *****************************************************************************
 *  Name: Guodong Xie
 *  Date: 05/23/2019
 *  Description: BruteCollinaerPoints.java
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    // finds all line segments containing 4 points

    private int n;
    private LineSegment[] ls;

    public BruteCollinearPoints(Point[] pointsIn) {
        // Throw a java.lang.IllegalArgumentException if the argument to the constructor is null,
        // if any point in the array is null
        // or if the argument to the constructor contains a repeated point
        if (pointsIn == null) throw new java.lang.IllegalArgumentException("error");
        for (Point p : pointsIn)
            if (p == null)
                throw new java.lang.IllegalArgumentException("error");
        Point[] points = new Point[pointsIn.length];
        for (int i = 0; i < points.length; i++)
            points[i] = pointsIn[i];
        Arrays.sort(points);
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i - 1]) == 0)
                throw new java.lang.IllegalArgumentException("error");
        }
        int capacity = 1;
        ls = new LineSegment[1];
        n = 0;
        int np = points.length;
        Point[] p = new Point[4];
        for (int i = 0; i < np; i++) {
            p[0] = points[i];
            for (int j = i + 1; j < np; j++) {
                p[1] = points[j];
                for (int k = j + 1; k < np; k++) {
                    p[2] = points[k];
                    for (int l = k + 1; l < np; l++) {
                        p[3] = points[l];
                        double s1 = p[0].slopeTo(p[1]);
                        double s2 = p[0].slopeTo(p[2]);
                        double s3 = p[0].slopeTo(p[3]);
                        if (Double.compare(s1, s2) == 0 && Double.compare(s1, s3) == 0) {
                            Arrays.sort(p);
                            ls[n] = new LineSegment(p[0], p[3]);
                            n++;
                            if (n == capacity) {
                                LineSegment[] lst = new LineSegment[capacity * 2];
                                for (int ii = 0; ii < n; ii++) {
                                    lst[ii] = ls[ii];
                                }
                                ls = lst;
                                capacity *= 2;
                            }
                        }
                    }

                }
            }
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return n;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lst = new LineSegment[n];
        for (int i = 0; i < n; i++) {
            lst[i] = ls[i];
        }
        return lst;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
