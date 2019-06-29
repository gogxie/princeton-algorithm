/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 05/25/2019
 *  Description: PointSet.java
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private TreeNode root;
    private int n;


    private class TreeNode {
        private final Point2D pt;
        private TreeNode left;
        private TreeNode right;
        private final boolean flag;
        private final double xlow;
        private final double xhigh;
        private final double ylow;
        private final double yhigh;

        public TreeNode(Point2D pt, boolean flag, double xlow, double xhigh, double ylow,
                        double yhigh) {
            this.pt = pt;
            this.left = null;
            this.right = null;
            this.flag = flag;
            this.xlow = xlow;
            this.xhigh = xhigh;
            this.ylow = ylow;
            this.yhigh = yhigh;
        }
    }


    // construct an empty set of points
    public KdTree() {
        root = null;
        n = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return n;
    }
    // add the point to the set (if it is not already in the set)

    public void insert(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("error!");
        root = insert(p, root, true, 0, 1, 0, 1);
        n++;
    }

    private TreeNode insert(Point2D p, TreeNode r, boolean flag, double xlow, double xhigh,
                            double ylow, double yhigh) {
        if (r == null)
            return new TreeNode(p, flag, xlow, xhigh, ylow, yhigh);
        if (r.pt.equals(p)) {
            n--;
            return r;
        }
        if (r.flag) {
            if (p.x() < r.pt.x())
                r.left = insert(p, r.left, false, xlow, r.pt.x(), ylow, yhigh);
            else
                r.right = insert(p, r.right, false, r.pt.x(), xhigh, ylow, yhigh);
        }
        else {
            if (p.y() < r.pt.y())
                r.left = insert(p, r.left, true, xlow, xhigh, ylow, r.pt.y());
            else
                r.right = insert(p, r.right, true, xlow, xhigh, r.pt.y(), yhigh);
        }
        return r;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("error!");

        return contains(root, p);
    }

    private boolean contains(TreeNode r, Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("error!");

        if (r == null)
            return false;

        if (r.pt.equals(p))
            return true;

        if (r.flag) {
            if (p.x() < r.pt.x())
                return contains(r.left, p);
            else
                return contains(r.right, p);
        }
        else {
            if (p.y() < r.pt.y())
                return contains(r.left, p);
            else
                return contains(r.right, p);
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(TreeNode r) {
        if (r == null)
            return;
        if (r.flag) {
            StdDraw.setPenRadius(0.002);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(r.pt.x(), r.ylow, r.pt.x(), r.yhigh);
            StdDraw.setPenRadius(0.02);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(r.pt.x(), r.pt.y());
            draw(r.left);
            draw(r.right);
        }
        else {
            StdDraw.setPenRadius(0.002);
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(r.xlow, r.pt.y(), r.xhigh, r.pt.y());
            StdDraw.setPenRadius(0.02);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(r.pt.x(), r.pt.y());
            draw(r.left);
            draw(r.right);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.IllegalArgumentException("error!");

        List<Point2D> list = new ArrayList<>();
        range(rect, root, list);
        return list;
    }

    private void range(RectHV rect, TreeNode r, List<Point2D> list) {
        if (r == null)
            return;
        if (rect.contains(r.pt))
            list.add(r.pt);
        if (r.flag) {
            if (rect.xmin() < r.pt.x())
                range(rect, r.left, list);
            if (rect.xmax() >= r.pt.x())
                range(rect, r.right, list);
        }
        else {
            if (rect.ymin() < r.pt.y())
                range(rect, r.left, list);
            if (rect.ymax() >= r.pt.y())
                range(rect, r.right, list);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.IllegalArgumentException("error!");
        if (isEmpty())
            return null;
        Point2D res = nearest(root, p);
        return res;
    }

    private Point2D nearest(TreeNode r, Point2D p) {
        Point2D res = r.pt;
        if (r.flag) {
            if (p.x() < r.pt.x()) {
                if (r.left != null) {
                    Point2D temp = nearest(r.left, p);
                    if (p.distanceSquaredTo(temp) < p.distanceSquaredTo(res))
                        res = temp;
                }
                RectHV trec = new RectHV(r.pt.x(), r.ylow, r.pt.x(), r.yhigh);
                if (p.distanceSquaredTo(res) > trec.distanceSquaredTo(p) && r.right != null) {
                    Point2D temp = nearest(r.right, p);
                    if (p.distanceSquaredTo(temp) < p.distanceSquaredTo(res))
                        res = temp;
                }
            }
            else {
                if (r.right != null) {
                    Point2D temp = nearest(r.right, p);
                    if (p.distanceSquaredTo(temp) < p.distanceSquaredTo(res))
                        res = temp;
                }
                RectHV trec = new RectHV(r.pt.x(), r.ylow, r.pt.x(), r.yhigh);
                if (p.distanceSquaredTo(res) > trec.distanceSquaredTo(p) && r.left != null) {
                    Point2D temp = nearest(r.left, p);
                    if (p.distanceSquaredTo(temp) < p.distanceSquaredTo(res))
                        res = temp;
                }
            }
        }
        else {
            if (p.y() < r.pt.y()) {
                if (r.left != null) {
                    Point2D temp = nearest(r.left, p);
                    if (p.distanceSquaredTo(temp) < p.distanceSquaredTo(res))
                        res = temp;
                }
                RectHV trec = new RectHV(r.xlow, r.pt.y(), r.xhigh, r.pt.y());
                if (p.distanceSquaredTo(res) > trec.distanceSquaredTo(p) && r.right != null) {
                    Point2D temp = nearest(r.right, p);
                    if (p.distanceSquaredTo(temp) < p.distanceSquaredTo(res))
                        res = temp;
                }
            }
            else {
                if (r.right != null) {
                    Point2D temp = nearest(r.right, p);
                    if (p.distanceSquaredTo(temp) < p.distanceSquaredTo(res))
                        res = temp;
                }
                RectHV trec = new RectHV(r.xlow, r.pt.y(), r.xhigh, r.pt.y());
                if (p.distanceSquaredTo(res) > trec.distanceSquaredTo(p) && r.left != null) {
                    Point2D temp = nearest(r.left, p);
                    if (p.distanceSquaredTo(temp) < p.distanceSquaredTo(res))
                        res = temp;
                }
            }
        }
        return res;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // nothing here
    }
}
