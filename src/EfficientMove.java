import java.util.*;
import java.util.Stack;
//E = 4V (since each cell has 4 edges)
//Total becomes O(4V + V log V) = O(V log V) (since V log V dominates)
//Space O(V) stores at most V nodes





public class EfficientMove {
    static final Map<Character, Integer> costMap;
    static boolean[][] visited;
    static int[][] costSoFar;
    static int rows;
    static int cols;

    static {
        costMap = new HashMap<>();
        costMap.put('S', 2);                 // Start
        costMap.put('P', 2);                 // Plain
        costMap.put('F', 3);                 // Forest
        costMap.put('W', 5);                 // Water
        costMap.put('M', Integer.MAX_VALUE); // Mountain (wall)
        costMap.put('G', 2);                 // Goal
    }


    //Print Function
    static void printPerformanceMetrics(Node goalNode, char[][] grid) {
        if (goalNode == null) {
            System.out.println("No path found to display metrics.");
            return;
        }

        // Calculate nodes explored/visited
        int nodesExplored = 0;
        for (boolean[] row : visited) {
            for (boolean cell : row) {
                if (cell) nodesExplored++;
            }
        }

        // Get path sequence
        List<Character> pathSequence = new ArrayList<>();
        List<Node> pathNodes = new ArrayList<>();
        Node current = goalNode;
        while (current != null) {
            pathNodes.add(current);
            current = current.parent;
        }
        Collections.reverse(pathNodes);
        for (Node node : pathNodes) {
            pathSequence.add(grid[node.x][node.y]);
        }

        // Calculate shortest path length (number of steps)
        int pathLength = pathNodes.size() - 1; // Subtract 1 to exclude start node

        System.out.println("\nA* Performance Metrics:");
        System.out.println("----------------------------------");
        System.out.printf("Nodes explored/visited: %8d\n", nodesExplored);
        System.out.printf("Shortest path length: %10d\n", pathLength);
        System.out.print("Shortest path: ");
        System.out.println(pathSequence.toString()
                .replace("[", "'").replace("]", "'")
                .replace(", ", "', '"));
        System.out.printf("Path total cost: %15d\n", goalNode.g);
        System.out.println("----------------------------------");
    }


    static int heuristic(int x1, int y1, int x2, int y2) {
        int newH = 2*(Math.abs(x1 - x2) + Math.abs(y1 - y2));
        return  newH;// Manhattan distance
    }

    public static void aStar(char[][] grid) {

        //for printing
        long startTime = System.currentTimeMillis();



        rows = grid.length;
        cols = grid[0].length;

        // Init arrays
        visited = new boolean[rows][cols];
        costSoFar = new int[rows][cols];
        for (int[] row : costSoFar) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        int startX = -1;
        int startY = -1;
        int goalX = -1;
        int goalY = -1;

        // Find start/goal coord
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'S' && startX == -1) {
                    startX = i;
                    startY = j;
                }
                if (grid[i][j] == 'G') {
                    goalX = i;
                    goalY = j;
                }
            }
        }

        // Handle no s,g
        if (startX == -1 || goalX == -1) {
            System.out.println("Start or Goal not found in grid.");
            return;
        }

        // Algorithm
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        Node startNode = new Node(startX, startY, 0, heuristic(startX, startY, goalX, goalY), null);
        frontier.add(startNode);
        costSoFar[startX][startY] = 0;

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // R, D, L, U

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();

            if (current.x == goalX && current.y == goalY) {
                long endTime = System.currentTimeMillis();
                System.out.println("\nPath found!");
                printPerformanceMetrics(current, grid);
                System.out.printf("Execution time: %15dms\n", (endTime - startTime));
                return;
            }

            if (visited[current.x][current.y]) continue;
            visited[current.x][current.y] = true;

            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1]; //preparatiojnn for calculate g

                // Boundary check
                if (nx < 0 || ny < 0 || nx >= rows || ny >= cols) continue;

                // Get cost directly from map
                int moveCost = costMap.get(grid[nx][ny]);
                if (moveCost == Integer.MAX_VALUE) continue; // Skip mountains

                int newCost = current.g + moveCost;
                if (newCost < costSoFar[nx][ny]) {
                    costSoFar[nx][ny] = newCost;
                    int h = heuristic(nx, ny, goalX, goalY);
                    Node neighbor = new Node(nx, ny, newCost, h, current);
                    frontier.add(neighbor);


                }
            }
        }
        //for print
        System.out.println("No path to goal found.");
        long endTime = System.currentTimeMillis();
        System.out.printf("Execution time: %15dms\n", (endTime - startTime));

    }

    public static void main(String[] args) {
        char[][] input = {
                {'S', 'P', 'P', 'F', 'G'},
                {'F', 'M', 'F', 'F', 'F'},
                {'P', 'P', 'S', 'P', 'P'}
        };


        char[][] input1 = {
                {'S', 'P', 'P', 'M', 'G'},
                {'F', 'M', 'F', 'M', 'M'},
                {'P', 'P', 'S', 'P', 'P'}
        };

        aStar(input);

        System.out.print("\n");
        System.out.print("\n");
        System.out.print("\n");

        aStar(input1);
    }


//    public static void main(String[] args){
//        Queue<Integer> stack = new LinkedList<>();
//
//        stack.offer(1);
//        stack.offer(2);
//        stack.offer(3);
//        System.out.println(stack.peek());
//        stack.poll();
//        System.out.println(stack);
//    }
}