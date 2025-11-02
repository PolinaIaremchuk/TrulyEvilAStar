public class Node implements Comparable<Node> {
    int x;
    int y;
    int g;
    int h;
    int f;
    Node parent;

    Node(int x, int y, int g, int h, Node parent) {
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.parent = parent;
    }

  /*
  for the min heap
  Integer.compare(3, 7) → -1  // means 3 comes before 7
  Integer.compare(5, 5) →  0  // same
  Integer.compare(10, 4) → 1  // means 10 comes after 4
   */
   @Override
    public int compareTo(Node other) {
        // Primary comparison by f-score (total cost)
        int fCompare = Integer.compare(this.f, other.f);
        if (fCompare != 0) {
            return fCompare;
        }
        // Secondary comparison by h-score (heuristic) for tie-breaking
        return Integer.compare(this.h, other.h);
    }
}