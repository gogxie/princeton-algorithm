/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 05/25/2019
 *  Description: PointSet.java
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }
    // add the point to the set (if it is not already in the set)

    public void insert(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("error!");

        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("error!");

        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points)
            p.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.IllegalArgumentException("error!");

        List<Point2D> list = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p))
                list.add(p);
        }
        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("error!");
        Point2D res = null;
        double dis = Double.MAX_VALUE;
        for (Point2D pt : points) {
            double d = pt.distanceSquaredTo(p);
            if (d < dis) {
                dis = d;
                res = pt;
            }
        }
        return res;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // nothing here
    }
}
