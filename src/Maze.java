import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        int n = w.cells();
        int m = w.count();
        DisjointSet dsu = new DisjointSet(n);
        int[] broken = new int[n - 1];
        int count = 0;

        for (int k = 0; k < m; k++) {
            if (dsu.union(w.a()[k], w.b()[k])) {
                broken[count++] = k;
            }
        }
        return broken;
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        int n = w.cells();
        int m = w.count();
        int[] cost = w.cost();

        Integer[] order = new Integer[m];
        for (int k = 0; k < m; k++) {
            order[k] = k;
        }
        Arrays.sort(order, Comparator.comparingInt(i -> cost[i]));

        DisjointSet dsu = new DisjointSet(n);
        int[] broken = new int[n - 1];
        int count = 0;

        for (int k : order) {
            if (dsu.union(w.a()[k], w.b()[k])) {
                broken[count++] = k;
                if (count == n - 1) break;
            }
        }
        return broken;
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells();
        int m = w.count();

        int[] deg = new int[n];
        for (int k = 0; k < m; k++) {
            deg[w.a()[k]]++;
            deg[w.b()[k]]++;
        }
        int[][] adj = new int[n][];
        for (int u = 0; u < n; u++) {
            adj[u] = new int[deg[u]];
        }
        int[] fill = new int[n];
        for (int k = 0; k < m; k++) {
            int a = w.a()[k], b = w.b()[k];
            adj[a][fill[a]++] = k;
            adj[b][fill[b]++] = k;
        }

        boolean[] visited = new boolean[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[1]));
        pq.add(new int[]{0, 0});

        long totalCost = 0;
        int visitedCount = 0;

        while (!pq.isEmpty() && visitedCount < n) {
            int[] cur = pq.poll();
            int u = cur[0];
            int c = cur[1];

            if (visited[u]) continue;
            visited[u] = true;
            totalCost += c;
            visitedCount++;

            for (int k : adj[u]) {
                int v = (w.a()[k] == u) ? w.b()[k] : w.a()[k];
                if (!visited[v]) {
                    pq.add(new int[]{v, w.cost()[k]});
                }
            }
        }
        return totalCost;
    }
}
