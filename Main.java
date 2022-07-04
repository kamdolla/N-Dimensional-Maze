import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int dimension   = 2;
        int size        = 5;

        // Maze maze       = outputMaze(dimension, size);
        // int[] solution  = outputSolution(maze, dimension, size);

        outputSolution(outputMaze(dimension, size), dimension, size);
    }

    /**
     * Generates an output maze with given {@code dimension} and {@code size}.
     * <p>
     * Writes generated output maze onto a file {@code output.txt},
     * as well as the given parameters.
     * <p>
     * If the {@link BufferedWriter} cannot write to the file, the method will return {@code null}.
     * 
     * @param inputDimension    – dimension for output maze
     * @param inputSize         – size for output maze
     * 
     * @return n-dimensional maze object with generated maze
     */
    public static Maze outputMaze(int inputDimension, int inputSize){

        Maze maze = new Maze(inputDimension, inputSize);

        String m1   = Arrays.toString(maze.create());
        String d    = Integer.toString(inputDimension);
        String s    = Integer.toString(inputSize);

        try{
            FileWriter m1File = new FileWriter("output.txt");
            BufferedWriter outputWriter = new BufferedWriter(m1File);

            outputWriter.write(d);
            outputWriter.newLine();
            outputWriter.write(s);
            outputWriter.newLine();
            outputWriter.write(m1);

            outputWriter.close();
        }
        catch (IOException exception){
            exception.printStackTrace();
            return null;
        }

        return maze;
    }

    /**
     * Generates a solution for the given {@code maze}, with {@code dimension} and {@code size}.
     * <p>
     * Writes the generated solution onto a file {@code solution.txt},
     * as well as the given parameters.
     * <p>
     * If the {@link BufferedWriter} cannot write to the file, the method will return {@code null}.
     * 
     * @param maze      – input maze object with generated maze to solve
     * @param dimenison – input maze dimension
     * @param size      – input maze size
     * 
     * @return generated solution path from position {@code 0} to last node position.
     */
    public static int[] outputSolution(Maze maze, int dimenison, int size){

        int[] solution = maze.solve();

        String s1   = Arrays.toString(solution);
        String d    = Integer.toString(dimenison);
        String s    = Integer.toString(size);

        try{
            FileWriter s1File = new FileWriter("solution.txt");
            BufferedWriter outputWriter = new BufferedWriter(s1File);

            outputWriter.write(d);
            outputWriter.newLine();
            outputWriter.write(s);
            outputWriter.newLine();
            outputWriter.write(s1);

            outputWriter.close();
        }
        catch (IOException exception){
            exception.printStackTrace();
            return null;
        }

        return solution;
    }
}
