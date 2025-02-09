import java.io.FileInputStream;
import java.util.HashMap;
import java.util.PriorityQueue;

import java.util.Objects;

class Point {
    public int x;
    public int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
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
    public int compareTo(Node other) {
        return Integer.compare(this.cost, other.cost);
    }

}


public class Map
{   
    public int row;
    public int col;
    public int[] map;

    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};

    Map(String filename, int row, int col)
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
                if(b == '\n' || b == ' ')
                {
                    continue;
                }

                map[c++] = b - '0';
            }
            
            file.close();
        }
        catch(Exception e)
        {
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


    public boolean isValid(Point p)
    {
        if(p.x < 0 || p.y < 0 || p.x >= col || p.y >= row) return false;
        return map[p.y * col + p.x] == 0;
    }

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
            
            if(currCost > costMap.get(currPoint)) continue;

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

    public int heuristics(Point p1, Point p2)
    {
        int mdist = Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
        return mdist;
    }


    public int aStart(Point start, Point stop)
    {
        PriorityQueue<Node> pq = new PriorityQueue<>(); 
        HashMap<Point, Integer> costMap = new HashMap<>();

        costMap.put(start, 0);
        pq.add(new Node(start, heuristics(start, stop)));

        while(!pq.isEmpty())
        {
            Node current = pq.poll();
            Point currPoint = current.p;
            int currCost = current.cost;

            if(currPoint.equals(stop)) return currCost;

            if(currCost > costMap.get(currPoint)) continue;

            for(int i = 0; i < 4; i++)
            {
                Point np = new Point(currPoint.x + dx[i], currPoint.y + dy[i]);

                if(isValid(np) == false) continue;

                int newCost = heuristics(np, stop) + currCost + 1;
                
                if(!costMap.containsKey(np) || costMap.get(np) > newCost)
                {
                    costMap.put(np, newCost);
                    pq.add(new Node(np, newCost));
                }

            }
            
        }
        

        

        return -1;
    }

    public static void main(String[] args)
    {
        Map map = new Map("sample.txt", 4, 4);
        map.print_map();
        int res = map.aStart(new Point(0, 0), new Point(3,3));

        System.out.println(res);
    }
}