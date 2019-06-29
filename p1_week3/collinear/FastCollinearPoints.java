/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 5/23/2019
 *  Description: FastCollinearPoints.java
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    // finds all line segments containing 4 or more points

    private int n;
    private LineSegment[] ls;

    public FastCollinearPoints(Point[] pointsIn) {
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
        Arrays.sort(points);
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i - 1]) == 0)
                throw new java.lang.IllegalArgumentException("error");
        }
        int capacity = 1;
        ls = new LineSegment[1];
        n = 0;
        int np = points.length;
        for (int i = 0; i < np - 3; i++) {
            final Point ref = points[i];
            Point[] pt = new Point[np];
            for (int j = 0; j < np; j++)
                pt[j] = points[j];
            Arrays.sort(pt, (a, b) -> Double.compare(ref.slopeTo(a), ref.slopeTo(b)));
            int start = 0;
            int end = 0;
            double sp = ref.slopeTo(pt[start]);
            while (end <= pt.length) {
                if (end != pt.length && Double.compare(ref.slopeTo(pt[end]), sp) == 0)
                    end++;
                else {
                    // System.out.println("start: " + start + " end: " + end);
                    if (end - start >= 3) {
                        Point[] p = new Point[end - start + 1];
                        for (int k = start; k < end; k++)
                            p[k - start] = pt[k];
                        p[end - start] = ref;
                        Arrays.sort(p);
                        if (p[0].compareTo(ref) == 0) {
                            ls[n] = new LineSegment(p[0], p[end - start]);
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
                    start = end;
                    end++;
                    if (start < pt.length)
                        sp = ref.slopeTo(pt[start]);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
