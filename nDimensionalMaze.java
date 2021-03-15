public class nDimensionalMaze {

    public static boolean DEBUG = true;
    public static void main(String[] args) throws Exception {
        
        int     dimension;
        int     size;

        //set dimensions and size
        dimension   = 2;
        size        = 10;

        Maze maze = new Maze(dimension, size);

        maze.checkMaze();  
        maze.solveMaze(0);
        if (DEBUG && dimension < 3) maze.printMaze();

    }

}