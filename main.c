#include <stdio.h>
#include "ucs.c"


int main()
{
    FILE* file = fopen("sample.txt", "r");
    Map map = create_map(4, 4);

    char temp;
    int offset = 0;
    while((temp = fgetc(file)) != EOF && offset < map.col * map.row)
    {
        if(temp == '\n') continue;
        map.arr[offset++] = temp - '0';
    }

    fclose(file);

    int res = DFS(map, (Point2D){0, 0}, (Point2D){3, 3});

    if(res == 1)
    {
        printf("YES\n");
    }
    else
    {
        printf("NO\n");
    }

    free_map(map);
    return 0;

}