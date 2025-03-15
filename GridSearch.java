import java.io.FileInputStream;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Objects;

class Point
{
    public int x;
    public int y;

    Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }
}

class Node implements Comparable<Node>
{
    int cost;
    Point p;

    Node(Point p, int cost)
    {
        this.cost = cost;
        this.p = p;
    }

    @Override
    public int compareTo(Node other)
    {
        return Integer.compare(this.cost, other.cost);
    }

}


public class GridSearch
{   
    public int row;
    public int col;
    public int[] map;

    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};

    GridSearch(String filename, int row, int col)
    {   
        this.row = row;
        this.col = col;
        this.map = new int[row * col];
        int c = 0;
        try
        {
            FileInputStream file = new FileInputStream(filename);
            int b;
            while((b = file.read()) != -1)
            {
                if(b != '1' && b != '0')
                {
                    continue;
                }

                map[c++] = b - '0';
            }
            
            file.close();
        }
        catch(Exception e)
        {
            System.err.println("File IO Error");
            System.exit(1);
        }
        
    }

    void print_map()
    {
        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < col; j++)
            {
                System.out.print(this.map[i * col + j] + " ");
            }
            System.out.println();
        }
    }

   /**
    * Checks if a given point is valid within the grid/map.
    * A point is considered valid if it lies within the grid boundaries and is not blocked (i.e., its value in the map is 0).
    * By default 0 considered to be empty space and 1 is an obstacle
    * Adjust value of the obstacle and empty space by changing value at line 115 by return from zero to one
    * @param p The point to validate.
    * @return True if the point is valid (within bounds and not blocked), false otherwise.
    */
    public boolean isValid(Point p)
    {
        if(p.x < 0 || p.y < 0 || p.x >= col || p.y >= row) return false;
        return map[p.y * this.col + p.x] == 0;
    }

    /**
    * Computes the Manhattan distance (heuristic cost) between two points.
    * 
    * @param p1 The first point.
    * @param p2 The second point.
    * @return The Manhattan distance between the two points.
    */
    public int heuristics(Point p1, Point p2)
    {
        int mdist = Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
        return mdist;
    }


    /**
    * Implements the A* search algorithm to find the shortest path from a start point to a stop point in the grid/map.
    * The algorithm uses a priority queue to explore nodes with the lowest estimated total cost (f = g + h),
    * where g is the cost from the start to the current node, and h is the heuristic estimate from the current node to the goal.
    *
    * @param start The starting point of the search.
    * @param stop The goal point of the search.
    * @return The shortest path cost to the stop point, or -1 if no path exists.
    */
    public int aStar(Point start, Point stop)
    {
        PriorityQueue<Node> pq = new PriorityQueue<>(); 
        HashMap<Point, Integer> gCostMap = new HashMap<>();

        gCostMap.put(start, 0);
        pq.add(new Node(start, heuristics(start, stop)));

        while(!pq.isEmpty())
        {
            Node current = pq.poll();
            Point currPoint = current.p;    
            int gCost = gCostMap.get(currPoint);

            if(currPoint.equals(stop)) return gCost;

            for(int i = 0; i < 4; i++)
            {
                Point np = new Point(currPoint.x + dx[i], currPoint.y + dy[i]);

                if(isValid(np) == false) continue;
                
                int h = heuristics(np, stop);
                int g = gCost + 1;
                int f = g + h;
                
                if(!gCostMap.containsKey(np) || gCostMap.get(np) > g)
                {   
                    gCostMap.put(np, g);
                    pq.add(new Node(np, f));
                }

            }
            
        }

        return -1;
    }
    

    /**
    * Performs a Uniform Cost Search (UCS) to find the minimum cost path from a start point to a stop point.
    * The search explores the grid using a priority queue, prioritizing nodes with the lowest cost.
    * The cost of moving to any neighboring point is assumed to be 1.
    *
    * @param start The starting point of the search.
    * @param stop  The target point to reach.
    * @return The minimum cost to reach the stop point from the start point. 
    * Returns -1 if the stop point is unreachable.
    */
    public int ucs(Point start, Point stop)
    {
        PriorityQueue<Node> pq = new PriorityQueue<>(); 
        HashMap<Point, Integer> costMap = new HashMap<>();

        pq.add(new Node(start, 0));
        costMap.put(start, 0);

        while(!pq.isEmpty())
        {
            Node current = pq.poll();
            Point currPoint = current.p;
            int currCost = current.cost;
            
            if(currPoint.equals(stop)) return currCost;

            for(int i = 0; i < 4; i++)
            {
                Point np = new Point(currPoint.x + dx[i], currPoint.y + dy[i]);
                
                if(isValid(np) == false) continue;

                int newCost = currCost + 1;

                if(!costMap.containsKey(np) || costMap.get(np) > newCost)
                {
                    costMap.put(np, newCost);
                    pq.add(new Node(np, newCost));
                }

            }

            
        }
        
        return -1;
    }

}

