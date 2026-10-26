import java.util.*;

/**
 * Missions 4 and 5. The slides: a directed graph. Cells are 0 .. n-1, slide i goes from s.from()[i] to s.to()[i]
 * and takes s.time()[i]. There are no slides from a cell to itself, and the exit can always be reached from the start.
 *
 * Ready for you: Graphs.slidesFrom(s)[u] is the list of the slides that leave cell u, and Graphs.slidesInto(s)[v] is the list
 * of the slides that enter cell v (reversed graph). The lists hold slide numbers i; the cell at the other end is s.to()[i]
 * or s.from()[i].
 */
public class Zones {

    /**
     * Mission 4. Kosaraju. Returns zoneOf: zoneOf[cell] = number of the zone, numbers 0 .. zones-1.
     * Number the zones in the order in which the second pass finds them. That order is a topological order
     * of the zone graph: every slide between two zones goes from a smaller number to a larger number.
     *
     * Plan: 1) DFS on the slides, write the cells in the order in which they are finished (use a stack, not recursion);
     * 2) go through the cells from the last finished to the first; if a cell has no zone yet, DFS from it on the reversed
     * slides (slidesInto) and give every cell it reaches the next zone number.
     * Is DAG in Main is 1 when the number of zones equals the number of cells (no cycle), so you do not need a separate check.
     */
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();
        int[][] from = Graphs.slidesFrom(s);
        int[][] into = Graphs.slidesInto(s);

        // DFS on forward slides to get finishing order
        // state: 0 = unvisited, 1 = visiting (on stack), 2 = finished
        int[] state = new int[n];
        List<Integer> finishedOrder = new ArrayList<>(n);
        Deque<Integer> stack = new ArrayDeque<>();

        for (int start = 0; start < n; start++) {
            if (state[start] != 0) continue;
            stack.push(start);

            while (!stack.isEmpty()) {
                int u = stack.pop();
                if (state[u] == 2) continue;

                if (state[u] == 1) {
                    // All neighbours explored
                    state[u] = 2;
                    finishedOrder.add(u);
                } else {
                    // First time visiting u:
                    //     push u back, then push its unvisited neighbours
                    state[u] = 1;
                    stack.push(u);
                    for (int slide : from[u]) {
                        int v = s.to()[slide];
                        if (state[v] == 0) {
                            stack.push(v);
                        }
                    }
                }
            }
        }

        // DFS on reversed slides
        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);
        int zoneCount = 0;

        for (int i = n - 1; i >= 0; i--) {
            int start = finishedOrder.get(i);
            if (zoneOf[start] != -1) continue;

            zoneOf[start] = zoneCount;
            stack.push(start);

            while (!stack.isEmpty()) {
                int u = stack.pop();
                for (int slide : into[u]) {
                    int v = s.from()[slide];
                    if (zoneOf[v] == -1) {
                        zoneOf[v] = zoneCount;
                        stack.push(v);
                    }
                }
            }
            zoneCount++;
        }
        return zoneOf;
    }

    /**
     * Mission 5. The graph of zones: the edge X to Y costs the cheapest slide from X to Y, walking inside a zone is free.
     * Go through the zones in the order of their numbers (no priority queue). Returns two numbers:
     * [0] flow stages: the number of zones on the longest chain of zones;
     * [1] the cost of the cheapest route from the zone of cell 0 to the zone of the last cell.
     */
    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        // TODO
        return new long[]{0, 0};
    }
}
