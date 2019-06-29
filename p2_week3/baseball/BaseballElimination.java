/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 06.02/2019
 *  Description: BaseballElimination.java
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseballElimination {
    // create a baseball division from given filename in format specified below
    private final int N;
    private final Map<String, Integer> name2id;
    private final int[] win;
    private final int[] lose;
    private final int[] left;
    private final int[][] matrix;
    private final String[] name;
    private boolean[] elimiated;
    private List<String>[] certification;
    private final int LARGE = 100000000;

    public BaseballElimination(String filename) {
        int maxWin = -1;
        int maxWinId = -1;

        In in = new In(filename);
        N = in.readInt();
        name2id = new HashMap<>();
        win = new int[N];
        lose = new int[N];
        left = new int[N];
        matrix = new int[N][N];
        name = new String[N];
        elimiated = new boolean[N];
        certification = (List<String>[]) new List[N];
        for (int i = 0; i < N; i++) {
            String tname = in.readString();
            name2id.put(tname, i);
            name[i] = tname;
            win[i] = in.readInt();
            if (win[i] > maxWin) {
                maxWin = win[i];
                maxWinId = i;
            }
            lose[i] = in.readInt();
            left[i] = in.readInt();
            for (int j = 0; j < N; j++) {
                matrix[i][j] = in.readInt();
            }
        }
        for (int i = 0; i < N; i++) {
            if (win[i] + left[i] < maxWin) {
                elimiated[i] = true;
                certification[i] = new ArrayList<>();
                certification[i].add(name[maxWinId]);
                continue;
            }
            eliminationCal(i);
        }
    }

    private void eliminationCal(int t) {
        int size = (N - 2) * (N - 1) / 2 + N + 1;
        // group 1: 0;
        // group 2: 1 -> (N - 2) * (N - 1) / 2
        // group 3: (N - 2) * (N - 1) / 2 + 1 -> (N - 2) * (N - 1) / 2 + (N - 1)
        // group 4: (N - 2) * (N - 1) / 2 + N
        FlowNetwork fn = new FlowNetwork(size);
        int[] team = new int[N - 1];
        int pos = 0;
        for (int i = 0; i < N; i++) {
            if (i != t)
                team[pos++] = i;
        }
        int idx = 1;
        int g3 = (N - 2) * (N - 1) / 2 + 1;
        for (int j = 0; j < N - 1; j++) {
            for (int k = j + 1; k < N - 1; k++) {
                fn.addEdge(new FlowEdge(0, idx, matrix[team[j]][team[k]]));
                fn.addEdge(new FlowEdge(idx, g3 + j, LARGE));
                fn.addEdge(new FlowEdge(idx, g3 + k, LARGE));
                idx++;
            }
        }
        for (int i = 0; i < N - 1; i++) {
            fn.addEdge(new FlowEdge(g3 + i, size - 1, win[t] + left[t] - win[team[i]]));
        }
        FordFulkerson ff = new FordFulkerson(fn, 0, size - 1);
        // System.out.println("flow: " + ff.value());
        List<String> list = new ArrayList<>();
        for (int i = 0; i < N - 1; i++) {
            if (ff.inCut(g3 + i))
                list.add(name[team[i]]);
        }
        certification[t] = list.size() == 0 ? null : list;
        elimiated[t] = list.size() != 0;
    }

    // number of teams
    public int numberOfTeams() {
        return N;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(name);
    }

    // number of wins for given team
    public int wins(String team) {
        if (!name2id.containsKey(team))
            throw new java.lang.IllegalArgumentException("non-existing key");

        return win[name2id.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!name2id.containsKey(team))
            throw new java.lang.IllegalArgumentException("non-existing key");

        return lose[name2id.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!name2id.containsKey(team))
            throw new java.lang.IllegalArgumentException("non-existing key");

        return left[name2id.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!name2id.containsKey(team1) || !name2id.containsKey(team2))
            throw new java.lang.IllegalArgumentException("non-existing key");

        return matrix[name2id.get(team1)][name2id.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!name2id.containsKey(team))
            throw new java.lang.IllegalArgumentException("non-existing key");

        return elimiated[name2id.get(team)];
    }

    // subset R of teams that eliminates given team; null if not eliminated

    public Iterable<String> certificateOfElimination(String team) {
        if (!name2id.containsKey(team))
            throw new java.lang.IllegalArgumentException("non-existing key");

        return certification[name2id.get(team)];
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
