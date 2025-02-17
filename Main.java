import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        GridSearch gs = new GridSearch("maze1.txt", 81, 81);
        
        Scanner scan = new Scanner(System.in);

        int x1 = scan.nextInt(); int y1 = scan.nextInt();
        int x2 = scan.nextInt(); int y2 = scan.nextInt();

        scan.close();

        int resInformed = gs.aStar(new Point(x1, y1), new Point(x2, y2));
        
        if(resInformed != -1)
        {
            System.out.println("YES");
        }
        else
        {
            System.out.println("NO");
        }

        System.out.println("Cost:" + resInformed);
    }
}
