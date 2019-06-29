/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 05/24/2019
 *  Description: Solver.java
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Solver {
    // find a solution to the initial board (using the A* algorithm)
    private class SearchNode {
        // We define a search node of the game to be a board,
        // the number of moves made to reach the board
        // and the predecessor search node
        private final Board curBoard;
        private final int move;
        private final SearchNode pre;

        public SearchNode(Board curBoard, int move, SearchNode pre) {
            this.curBoard = curBoard;
            this.pre = pre;
            this.move = move;
        }
    }

    private final boolean slovebale;
    private final int moves;
    private final Board[] results;

    public Solver(Board initial) {
        if (initial == null)
            throw new java.lang.IllegalArgumentException("Error");
        MinPQ<SearchNode> pq1 = new MinPQ<>(
                (a, b) -> (a.curBoard.manhattan() - b.curBoard.manhattan() + a.move - b.move));
        MinPQ<SearchNode> pq2 = new MinPQ<>(
                (a, b) -> (a.curBoard.manhattan() - b.curBoard.manhattan() + a.move - b.move));
        pq1.insert(new SearchNode(initial, 0, null));
        pq2.insert(new SearchNode(initial.twin(), 0, null));
        while (true) {
            SearchNode tempNode;
            tempNode = pq1.delMin();
            // System.out.println("O::" + tempNode.curBoard.hamming());
            // System.out.println("O::" + tempNode.curBoard.manhattan());
            // System.out.println(tempNode.curBoard.toString());
            if (tempNode.curBoard.isGoal()) {
                slovebale = true;
                moves = tempNode.move;
                results = new Board[moves + 1];
                int m = moves;
                while (tempNode != null) {
                    results[m--] = tempNode.curBoard;
                    tempNode = tempNode.pre;
                }
                break;
            }
            for (Board brd : tempNode.curBoard.neighbors()) {
                // SearchNode ts = tempNode.pre;
                // boolean flag = true;
                // while (ts != null) {
                //     if (brd.equals(ts.curBoard)) {
                //         flag = false;
                //         break;
                //     }
                //     ts = ts.pre;
                // }
                // if (flag)
                //     pq1.insert(new SearchNode(brd, tempNode.move + 1, tempNode));
                if (tempNode.pre == null || !brd.equals(tempNode.pre.curBoard))
                    pq1.insert(new SearchNode(brd, tempNode.move + 1, tempNode));
            }

            tempNode = pq2.delMin();
            // System.out.print("T::");
            // System.out.println(tempNode.curBoard.toString());
            if (tempNode.curBoard.isGoal()) {
                slovebale = false;
                results = null;
                moves = -1;
                break;
            }
            for (Board brd : tempNode.curBoard.neighbors()) {
                // SearchNode ts = tempNode.pre;
                // boolean flag = true;
                // while (ts != null) {
                //     if (brd.equals(ts.curBoard)) {
                //         flag = false;
                //         break;
                //     }
                //     ts = ts.pre;
                // }
                // if (flag)
                //     pq2.insert(new SearchNode(brd, tempNode.move + 1, tempNode));
                if (tempNode.pre == null || !brd.equals(tempNode.pre.curBoard))
                    pq2.insert(new SearchNode(brd, tempNode.move + 1, tempNode));
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return slovebale;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (slovebale)
            return () -> (new SolutionIterator());
        else
            return null;
    }

    private class SolutionIterator implements Iterator<Board> {

        private final int n;
        private int ptr;

        public SolutionIterator() {
            n = results.length;
            ptr = 0;
        }

        @Override
        public boolean hasNext() {
            return ptr != n;
        }

        @Override
        public Board next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException("error");
            Board t = results[ptr];
            ptr++;
            return t;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
            StdOut.println("Minimum number of moves = " + solver.moves());
        }
    }
}
