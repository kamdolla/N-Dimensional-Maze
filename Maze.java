import java.util.*;
import java.lang.Math;

public class Maze {
    //***************************************************//
    //          NOTES – Maze Object            
    // Maze will be a representation of a n-dimensional maze
    // Maze will hold an array of dimension, size, array of cells, and map of maze nodes
    //
    // Dimension:
    // – Holds the defined dimension of the maze
    //
    // Size:
    // - Holds the defined size of one row in the square n-dimensional maze
    //
    // Cells:
    // – One cell for each space in the maze
    // – Each cell contains a boolean array representing walls
    // – Walls defined as being on or off
    // – Cells.length    = (maze.size)^(maze.dimension)
    // - Cells[0].length = 2*(maze.dimension)
    //
    // MazeNodes:
    // - Integer position of cell --> MazeNode for disjoint set functions
    // - After createMaze(), all MazeNodes should have same parent
    //***************************************************//

    private boolean[][]  cells;
    private int          dimension;
    private int          size;

    private Map<Integer, MazeNode> mazeNodes = new HashMap<>();

    public Maze(int dimension, int size){
        System.out.println("INITIALIZING: Maze being initialized...");
        this.dimension  = dimension;
        this.size       = size;
        this.cells      = new boolean[(int) Math.pow(size, dimension)][2*dimension];
        System.out.println("INITIALIZING: Successfully initialized");
        createMaze();
    }

    //***************************************************//
    //          NOTES – Class Methods           
    // CREATE MAZE: for each cell: 
    // - creates MazeNode representing cell and its set data
    // - unions to some other in-bounds, disjoint cell
    // - breaks walls between union cells  
    //
    // CHECK MAZE: for each MazeNode:
    // - checks if all parents are the same
    //
    // SOLVE MAZE: 
    // - finds path to target node and returns it to output file
    // - uses dfs and backtracking
    //
    // PRINT MAZE:
    // - prints the maze
    // - reasonably, only should be printing up to 2 dimensions
    //***************************************************//

    public void createMaze(){
        System.out.println("CREATING: Maze being created...");
        //Setup – set all wall values to true
        Stack<Integer> unused   = new Stack<>();

        for(int i = 0; i < cells.length; i++){
            for (int j = 0; j < cells[0].length; j++){
                cells[i][j] = true;
            }

            unused.add(i);
        }

        Collections.shuffle(unused);
        //Stack<Integer> clone = (Stack<Integer>) unused.clone();

        //Connect Sets – randomly connect cells and break walls

        //Kruskal's until all nodes have been used once
        while(!unused.isEmpty()){
            //Set current node and position and add to mazeNodes
            int currPos         = unused.pop();
            MazeNode currNode   = (mazeNodes.containsKey(currPos)) ?
                                    mazeNodes.get(currPos) : new MazeNode(currPos);

            if (!mazeNodes.containsKey(currPos)) mazeNodes.put(currPos, currNode);

            //Create list of valid directions to move in (+x, -x, +y, ...)
            //Find the next node and union it to the current node
            for (Integer dir : validDirections(currPos)){
                //Use the direction to find the next node and position
                int nextPos         = currPos+dir;
                MazeNode nextNode   = (mazeNodes.containsKey(nextPos)) ?
                                        mazeNodes.get(nextPos) : new MazeNode(nextPos);

                if (!mazeNodes.containsKey(nextPos)) mazeNodes.put(nextPos, nextNode);

                //If the nodes can be unioned, union and break walls between them >> break
                if(currNode.union(nextNode)){
                    breakWalls(currPos, nextPos, dir);
                    //break;
                }
            }
        }

        //Can repeat the unioning process by uncommenting lines 77 and 103
        //Repeating will result in different, more connected maze
        /*
        //First go around creates disjointed trees, second go around connects all trees together
        while(!clone.isEmpty()){
            //Set current node and position and add to mazeNodes
            int currPos         = clone.pop();
            MazeNode currNode   = (mazeNodes.containsKey(currPos)) ?
                                    mazeNodes.get(currPos) : new MazeNode(currPos);

            if (!mazeNodes.containsKey(currPos)) mazeNodes.put(currPos, currNode);

            //Create list of valid directions to move in (+x, -x, +y, ...)
            //Find the next node and union it to the current node
            for (Integer dir : validDirections(currPos)){
                //Use the direction to find the next node and position
                int nextPos         = currPos+dir;
                MazeNode nextNode   = (mazeNodes.containsKey(nextPos)) ?
                                        mazeNodes.get(nextPos) : new MazeNode(nextPos);

                if (!mazeNodes.containsKey(nextPos)) mazeNodes.put(nextPos, nextNode);

                //If the nodes can be unioned, union and break walls between them >> break
                if(currNode.union(nextNode)){
                    breakWalls(currPos, nextPos, dir);
                    break;
                }
            }
        }
        */
        System.out.println("CREATING: Successfully created");
    }

    public boolean checkMaze(){
        System.out.println("VALIDITY: Maze being validated...");
        //Every parent of each MazeNode should be the same
        MazeNode trueParent = this.mazeNodes.get(0).findSet();

        //Go through and compare each MazeNode parent to trueParent
        for (int i = 0; i < this.cells.length; i++){
            //If curr parent is not equal to trueParent, then maze is not valid
            if (!this.mazeNodes.get(i).findSet().isEquals(trueParent)){
                System.out.println("VALIDITY: Maze is INVALID");
                return false;
            }
        }
        
        //After visiting all nodes and checking each parent, every node is reachable >> valid
        System.out.println("VALIDITY: Maze is VALID");
        return true;
    }

    public void solveMaze(int start){
        solveMaze(start, this.cells.length-1);
    }

    public void solveMaze(int start, int end){
        //If start position is end position, path found
        System.out.println("SOLVING: Currently finding path...");
        if (start == end) {
            System.out.println("SOLVING: Found path");
            System.out.println("Path: " + start);
        }
        else{
            //Create a empty map of position integers to used booleans to be filled in dfs
            Map<Integer, Boolean> used = new HashMap<>();

            //dfs from start position, ends on end position
            System.out.println("SOLVING: Starting DFS on start position...");
            Stack<Integer> pathlist = dfs(used, start, end);

            //Write out path
            System.out.println("SOLVING: Found path");
            System.out.print("Path: " + pathlist.pop());
            while(!pathlist.isEmpty()){
                System.out.print(" -> " + pathlist.pop());
            }
            System.out.println();
        }
    }

    public Stack<Integer> dfs(Map<Integer, Boolean> visited, int curr, int end){
        //Path to be returned
        Stack<Integer> path = new Stack<>();

        //Marks current position as visited
        visited.putIfAbsent(curr, true);

        //If the destination is reached, add to the path and return it
        if (curr == end){
            path.push(curr);
            return path;
        }

        //DFS in each valid direction until destination is reached
        for (Integer dir : validDirections(curr)){
            //If wall exists and path is not already visited...
            if (!this.cells[curr][CONVERT_wall(dir)] && !visited.containsKey(curr+dir)){
                //Continue DFS on new position
                path = dfs(visited, curr+dir, end);

                //If path contains the destination, add the current position to path and return it
                if (path != null){
                    path.push(curr);
                    return path;
                }
            }
        }

        //If there are no more paths to go down on this depth, return null
        return null;
    }

    public void printMaze(){
        System.out.println("PRINTING: Maze being printed...");
        //Only compatabile for dimensions less than 2
        //Create map for arrow directions
        Map<Integer, String> map = new HashMap<>();
        map.put(0,"-▶");
        map.put(1,"⬅");
        map.put(2,"⬇");
        map.put(3,"⬆");

        //Print each cell's walls with arrow key directions for readibility
        for(int i = 0; i < this.cells.length; i++){
            if (i % this.size == 0) System.out.println();

            System.out.print("[ ");
            String s = "";

            for(int j = 0; j < this.cells[0].length; j++){
                if (this.cells[i][j] == false) s+= map.get(j) + " ";
            }

            s += "] ";
            System.out.printf("%-13s", s);
        }
        System.out.println();
    }

    //***************************************************//
    //          NOTES – Additional Methods           
    // BREAK WALLS: 
    // - breaks the walls from one node in the direction of another node
    //
    // VALID DIRECTIONS: 
    // – returns a list of in bound directions to move in from given position
    // - randomizes list for random unioning in Kruskal's
    //***************************************************//

    public void breakWalls(int fromPos, int toPos, int dir){   
        //Find which type of wall (x, y, z, ...) to break
        int wallPos = CONVERT_wall(dir);
        
        //If dir is negative, break the -wall for the outgoing direction
        if (dir < 0){
            this.cells[fromPos][wallPos]  = false;
            this.cells[toPos][wallPos-1]  = false;
        }
        
        //If dir is positive, break the +wall for the outgoing direction
        else{
            this.cells[fromPos][wallPos]  = false;
            this.cells[toPos][wallPos+1]  = false;
        }
    }

    public List<Integer> validDirections(int pos){
        //Make list of valid directions to move in
        List<Integer> ans = new ArrayList<>();
        for(int i = 0; i < this.dimension; i++){
            //dir1 = +pos coordinate direction; dir2 = –pos coordinate direction
            int dir1    = (int) Math.pow(this.size, i);
            int dir2    = -dir1;

            //validate directions, add direction + position
            //invalid if over/under cells size, and if wraps around (side bounds)
            if ( ( (dir1+pos) % Math.pow(size,i+1) ) > (pos % Math.pow(size,i) ) ){
                if (dir1+pos < this.cells.length){
                    ans.add(dir1);
                }
            }

            if ( (pos % Math.pow(size,i+1)) > ( (dir2+pos) % Math.pow(size,i) ) ){
                if (dir2+pos >= 0){
                    ans.add(dir2);
                }
            }
        }

        //Randomize the list
        Collections.shuffle(ans);

        return ans;
    }

    //***************************************************//
    //          NOTES – Conversion Methods           
    //***************************************************//

    public int CONVERT_wall(int dir){
        //Changes direction into wall position (ex. 5 -> -y)
        return (dir < 0)    ?   (int) (2*(Math.log(-dir)/Math.log(this.size)))+1 :
                                (int) (2*(Math.log(dir)/Math.log(this.size)));
    }

}