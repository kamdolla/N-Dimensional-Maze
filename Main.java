import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Maze maze = new Maze(4,5);

        boolean[] m1 = maze.create();
        System.out.println(Arrays.toString(m1));

        int[] m2 = maze.solve(0, 200);
        System.out.println(Arrays.toString(m2));

    }
}
