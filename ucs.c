#include <stdlib.h>
#include <string.h>

typedef struct
{
    int row, col;
    int* arr;
} Map;


typedef struct 
{
    int x, y;
} Point2D;


Map create_map(int row, int col)
{
    Map res;
    res.row = row;
    res.col = col;
    res.arr = calloc(row * col, sizeof(int));
    return res; 
}

void free_map(Map mp)
{
    free(mp.arr);
}

int cmp(Point2D a, Point2D b)
{
    return a.x == b.x && a.y == b.y;
}

int outbnd(Map map, Point2D point)
{
    return point.x < 0 || point.y < 0 || point.x >= map.col || point.y >= map.row;
}

int cost(Point2D p1, Point2D p2)
{
    return abs(p1.x - p2.x) + abs(p1.y - p2.y);
}

static void dfs(Map* map, char* bitmap, Point2D current, Point2D stop, int* found)
{
    if(*found == 1 || outbnd(*map, current)) return;

    int linear_pos = current.y * map->col + current.x;

    if(bitmap[linear_pos / 8] & (1 << (linear_pos % 8))) return;

    if(map->arr[linear_pos] == 1)
    {
        bitmap[linear_pos / 8] |= 1 << (linear_pos % 8);
        return;
    }
     
    
    if(cmp(current, stop))
    {
        *found = 1;
        return;
    }

    bitmap[linear_pos / 8] |= 1 << (linear_pos % 8);

    dfs(map, bitmap, (Point2D){current.x + 1, current.y}, stop, found);
    dfs(map, bitmap, (Point2D){current.x - 1, current.y}, stop, found);
    dfs(map, bitmap, (Point2D){current.x, current.y - 1}, stop, found);
    dfs(map, bitmap, (Point2D){current.x, current.y + 1}, stop, found);

}


int DFS(Map map, Point2D start, Point2D stop)
{
    int size = map.col * map.row;
    
    char* bitmap = calloc((size / 8) + 1, 1);
    int res = 0;

    dfs(&map, bitmap, start, stop, &res);
    
    free(bitmap);

    return res;
}

int ucs(Map map, char* bitmap, int* distance, Point2D current, Point2D stop)
{
    

}

int UCS(Map map, Point2D start, Point2D stop)
{
    int size = map.col * map.row;
    char* bitmap = calloc((size / 8) + 1, 1);
    int* distance = calloc(size, sizeof(int));
    
    memset(distance, 0x3f, sizeof(int) * size);
    
    ucs(map, bitmap, distance, start, stop);

}

int astar(Map map, Point2D start, Point2D stop)
{
    
}