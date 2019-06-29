/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 06/05/2019
 *  Description: BoggleSover.java
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Set;
import java.util.TreeSet;

public class BoggleSolver {
    private Node root;

    private class Node {
        private boolean flag;
        private Node[] next;

        public Node() {
            flag = false;
            next = new Node[26];
        }
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
            throw new java.lang.IllegalArgumentException("null");
        root = new Node();
        for (String st : dictionary) {
            addString(st, root);
        }
    }

    private void addString(String st, Node r) {
        Node cur = r;
        for (int i = 0; i < st.length(); i++) {
            int idx = st.charAt(i) - 'A';
            if (cur.next[idx] == null) {
                cur.next[idx] = new Node();
            }
            cur = cur.next[idx];
        }
        cur.flag = true;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null)
            throw new java.lang.IllegalArgumentException("null");
        int M = board.rows();
        int N = board.cols();
        Set<String> allWords = new TreeSet<>();
        StringBuilder sb = new StringBuilder();
        boolean[][] visited = new boolean[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                transverse(board, allWords, sb, visited, root, i, j);
            }
        }
        return allWords;
    }

    private void transverse(BoggleBoard bd, Set<String> allWords, StringBuilder sb,
                            boolean[][] visited, Node r, int i, int j) {
        if (!visited[i][j]) {
            int M = bd.rows();
            int N = bd.cols();
            visited[i][j] = true;
            char ch = bd.getLetter(i, j);
            if (ch == 'Q') {
                if (r.next['Q' - 'A'] == null
                        || r.next['Q' - 'A'].next['U' - 'A'] == null) {
                    visited[i][j] = false;
                }
                else {
                    r = r.next['Q' - 'A'].next['U' - 'A'];
                    sb.append("QU");

                    if (r.flag && sb.length() >= 3)
                        allWords.add(sb.toString());

                    int[][] dirs = new int[][] {
                            { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 },
                            { 1, 0 }, { 1, 1 }
                    };
                    for (int[] dir : dirs) {
                        int x = i + dir[0];
                        int y = j + dir[1];
                        if (x >= 0 && x < M && y >= 0 && y < N)
                            transverse(bd, allWords, sb, visited, r, x, y);
                    }
                    sb.setLength(sb.length() - 2);
                    visited[i][j] = false;
                }
            }
            else {
                if (r.next[ch - 'A'] == null) {
                    visited[i][j] = false;
                }
                else {
                    r = r.next[ch - 'A'];
                    sb.append(ch);
                    if (r.flag && sb.length() >= 3)
                        allWords.add(sb.toString());

                    int[][] dirs = new int[][] {
                            { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 },
                            { 1, 0 }, { 1, 1 }
                    };
                    for (int[] dir : dirs) {
                        int x = i + dir[0];
                        int y = j + dir[1];
                        if (x >= 0 && x < M && y >= 0 && y < N)
                            transverse(bd, allWords, sb, visited, r, x, y);
                    }
                    sb.setLength(sb.length() - 1);
                    visited[i][j] = false;
                }
            }
        }

    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null)
            throw new java.lang.IllegalArgumentException("null");
        if (scoreCal(root, word, 0)) {
            int len = word.length();
            if (len <= 2) return 0;
            if (len <= 4) return 1;
            if (len <= 5) return 2;
            if (len <= 6) return 3;
            if (len <= 7) return 5;
            return 11;
        }
        else {
            return 0;
        }
    }

    private boolean scoreCal(Node r, String word, int pos) {
        if (pos == word.length()) {
            return r.flag;
        }
        if (r.next[word.charAt(pos) - 'A'] == null)
            return false;
        return scoreCal(r.next[word.charAt(pos) - 'A'], word, pos + 1);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
