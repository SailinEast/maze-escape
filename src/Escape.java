import java.util.*;

/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells();
        int cols = w.cols();
        int exit = n - 1;
        int[][] open = Graphs.open(w, broken);

        int deadEnds = 0;
        for (int u = 0; u < n; u++) {
            if (open[u].length == 1) {
                deadEnds++;
            }
        }

        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);

        int visitedCount = 0;
        int[] dirs = {-cols, 1, cols, -1}; // up, right, down, left

        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (visited[u]) continue;
            visited[u] = true;
            visitedCount++;

            if (u == exit) break;

            for (int d = 3; d >= 0; d--) {
                int v = u + dirs[d];
                if (isNeighbourOpen(open[u], v) && !visited[v]) {
                    parent[v] = u;
                    stack.push(v);
                }
            }
        }

        List<Integer> list = new ArrayList<>();
        for (int cur = exit; cur != -1; cur = parent[cur]) {
            list.add(cur);
        }
        Collections.reverse(list);

        int[] path = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            path[i] = list.get(i);
        }

        return new Result(path, visitedCount, deadEnds);
    }

    private static boolean isNeighbourOpen(int[] openNeighbours, int target) {
        for (int x : openNeighbours) {
            if (x == target) return true;
        }
        return false;
    }
}
