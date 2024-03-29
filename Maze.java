import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

/***
 * Graphical representation of a maze–– where walls denote edges, and nodes denote vertices.
 * 
 * @author Kameron Melvin {kmelvin22@amherst.edu}
 * @since   1.0
 * @version 1.0
 */
class Maze {
    
    private int numNodes;
    private int numNodeWalls;

    private boolean[] walls;

    private int dimension;
    private int size;

    private HashMap<Integer, Node> nodes;

    /***
     * Creates maze object.
     * <p>
     * Sets {@code dimenison}, {@code size}.
     * <p>
     * Determines the {@code numNodes}, {@code numNodeWalls}.
     * 
     * @param inputDimension    – dimension of maze
     * @param inputSize         – size of maze
     * 
     * @see Node
     */
    public Maze(Integer inputDimension, Integer inputSize){
        
        this.dimension  = inputDimension.intValue();
        this.size       = inputSize.intValue();

        numNodes        = (int) Math.pow(size, dimension);
        numNodeWalls    = dimension * 2;
    }

    public int getNumNodes(){
        return numNodes;
    }

    public int getNumNodeWalls(){
        return numNodeWalls;
    }

    /***
     * Initialization method for maze object.
     * <p>
     * Intializes {@code walls[]} to be all true.
     * <p>
     * Generates mapping from {@code node.pos} to {@code node} and initializes nodes for each position.
     */
    public void initialize(){
        walls = new boolean[numNodes*numNodeWalls];
        Arrays.fill(walls, true);

        nodes = new HashMap<Integer, Node>();

        for (int i = 0; i < numNodes; i++){
            nodes.put(i*numNodeWalls, new Node(i*numNodeWalls));
        }
    }

    /***
     * Generates a maze using constructor variables using an implementation of "Kruskal's Algorithm".
     * <p>
     * Randomly selects a {@code nodeA}, then randomly selects a direction to find {@code nodeB}, then unions if {@code nodeA} and {@code nodeB} can be unioned.
     * Repeat the process until all nodes have been selected.
     * <p>
     * Ensures that all nodes will be unioned with no cycles, as self unions are not permitted.
     * <p>
     * Utilizes path compression and union by rank to minimize runtime.
     * <p><ul>
     * <li> Union by rank := highest ranked {@code parent} of becomes {@code parent} of disjoint set.
     * <li> Path compression := will set {@code this.parent} to {@code parent} of disjoint set.
     * </ul><p>
     * If maze generation is invalid, then maze will be generated again.
     * 
     * @return String maze representation as {@code walls}
     */
    public String create(){

        initialize();

        List<Integer> nodeIndexList = new ArrayList<Integer>(nodes.keySet());
        Collections.shuffle(nodeIndexList);

        Iterator<Integer> nodeIndexIterator = nodeIndexList.iterator();

        while(nodeIndexIterator.hasNext()){

            Node currNode = nodes.get(nodeIndexIterator.next());

            List<Integer> nextPositions = findNextPositions(currNode.getPos());
            Collections.shuffle(nextPositions);

            for (Integer nextNodePos : nextPositions) {

                Node nextNode = nodes.get(nextNodePos);

                if (currNode.union(nextNode)){
                    openWall(currNode.getPos(), nextNode.getPos());
                    break;
                }
            }
        }

        if (!check())
            return create();

        return Arrays.toString(walls);
    }

    /***
     * Ensures that the maze generation is valid.
     * <p>
     * A maze is considered valid if:
     *      there exists a spanning tree between all nodes, and
     *      there exists no cycles between nodes.
     * <p>
     * Because of union by rank and path compression methods,
     *      if all nodes point towards the same parent
     *      then the maze must be valid.
     * <p>
     * Maze will be generated again if maze is invalid.
     * 
     * @return true if maze is valid
     */
    public boolean check(){

        Node parent = null;

        for(Node node : nodes.values()){
            if (parent == null){
                parent = node.findSet();
                continue;
            }

            if (!parent.equals(node.findSet()))
                return false;
        }

        return true;
    }

    /***
     * Uses searching algorithm to find path from {@code 0} (the first position) to {@code walls.length-nodeNumWalls} (the last position), 
     * and return list of positions as its path.
     * <p>
     * Uses the A* searching algorithm using an eucledian distance heuristic,
     * which is guaranteed to find the minimal path cost.
     * <p>
     * Need to find cleaner way of organizing path costs, rather than having a dedicated comparator
     * and tuple object to accomplish sorting.
     * 
     * @return String of mininum cost path from {@code startPos} to {@code goalPos}
     */
    public String solve(){
        return solve(0, walls.length-numNodeWalls);
    }

    /***
     * Uses searching algorithm to find path from {@code startPos} to {@code goalPos}, 
     * and return list of positions as its path.
     * <p>
     * Uses the A* searching algorithm using an eucledian distance heuristic,
     * which is guaranteed to find the minimal path cost.
     * 
     * @param startPos  - first position
     * @param goalPos   – second position
     * 
     * @return String mininum cost path from {@code startPos} to {@code goalPos}
     */
    public String solve(int startPos, int goalPos){

        HashMap<Integer, Integer> pathPrev  = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> pathCost  = new HashMap<Integer, Integer>();
        PriorityQueue<Integer> pathQueue = new PriorityQueue<Integer>((pos1, pos2) -> ((pathCost.get(pos1) + findHeuristic(pos1, goalPos)) - (pathCost.get(pos2) + findHeuristic(pos2, goalPos))));

        pathPrev.put(startPos, null);
        pathCost.put(startPos, 0);
        pathQueue.add(startPos);

        while (!pathQueue.isEmpty()) {

            int currentPos = pathQueue.poll();

            if (currentPos == goalPos)
                return findPath(pathPrev, currentPos);

            for (Integer nextPos : findOpenPositions(currentPos)){

                int nextPosCost = pathCost.get(currentPos) + 1;

                if (!pathCost.containsKey(nextPos) || nextPosCost < pathCost.get(currentPos)){

                    pathPrev.put(nextPos, currentPos);
                    pathCost.put(nextPos, nextPosCost);
                    pathQueue.add(nextPos);
                }
            }
        }

        return null;
    }

    /***
     * Given {@code pos1} and {@code pos2}, sets appropriate booleans in {@code walls} to true.
     * <p>
     * Determines direction vector, and wall positions from given positions.
     * 
     * @param pos1  – first position
     * @param pos2  – second position
     */
    public void openWall(int pos1, int pos2){

        int dir         = pos2 - pos1;

        int direction   = (dir < 0) ? -1 : 1;
        int magnitude   = (dir < 0) ? -dir : dir;

        int wallIndex   = ((direction < 0) ? 1 : 0) + 2 * ((int) (Math.log(magnitude/numNodeWalls) / Math.log(size)));

        int wallsPos1   = pos1 + wallIndex;
        int wallsPos2   = (wallsPos1 % 2 == 0) ? pos2 + wallIndex+1: pos2 + wallIndex-1;

        walls[wallsPos1] = false;
        walls[wallsPos2]= false;
    }

    /***
     * Finds path from {@code currentPos} and head of {@code pathPrev} map.
     * <p>
     * The head of the map is determined if {@code pathPrev.get(pos) == null}.
     * <p>
     * Backtracks the previous path map until the head is found.
     * 
     * @param pathPrev      – position to previous position mapping
     * @param currentPos    – first position
     * 
     * @return ordered position list indicating path
     */
    public String findPath(HashMap<Integer, Integer> pathPrev, int currentPos){

        List<Integer> path = new ArrayList<Integer>();

        while (pathPrev.get(currentPos) != null){

            path.add(currentPos);

            currentPos = pathPrev.get(currentPos);
        }

        path.add(currentPos);

        Collections.reverse(path);

        return path.toString();
    }

    /***
     * Given initial position, determine all valid next positions.
     * <p>
     * A position is invalid if:
     * <p><ul>
     * <li> its index does not map to a {@link Node}, 
     * <li> its index is {@code > 0} 
     * <li> its index is {@code < walls.length} 
     * <li> one of its columns do not match
     * </ul><p>
     * Translate graph coordinates to positions. 
     * 
     * @param currentPos    – initial position
     * 
     * @return list of valid next positions
     * 
     * @see Maze#checkDirection(int, int, int)
     */
    public List<Integer> findNextPositions(int currentPos){
        
        List<Integer> nextPositions = new ArrayList<Integer>();

        for(Integer direction : findDirections(currentPos))
            nextPositions.add(currentPos + direction);

        return nextPositions;
    }

    /***
     * Determines {@code nextPositions} and which have open walls.
     * <p>
     * See {@link Maze#findNextPositions(int)} to see how next positions are determined.
     * 
     * @param currentPos – first position
     * 
     * @return list of next positions with open walls
     * 
     * @see Maze#checkOpenWall(int, int)
     */
    public List<Integer> findOpenPositions(int currentPos){

        List<Integer> nextPositions = findNextPositions(currentPos);

        nextPositions.removeIf(nextPos -> checkOpenWall(currentPos, nextPos));

        return nextPositions;
    }

    /***
     * Computes list of valid directions from a given position.
     * <p>
     * A {@code direction} is valid if it {@code currentPos + direction} maps to valid position.
     * 
     * @param currentPos    – first position
     * 
     * @return list of valid directions from input position.
     */
    public List<Integer> findDirections(int currentPos){

        List<Integer> directions = new ArrayList<Integer>();

        for(int i = 0; i < numNodeWalls; i++) {

            int direction   = (i % 2 == 0) ? 1 : -1;
            int magnitude   = numNodeWalls * (int) Math.pow(size, (i/2));

            if (checkDirection(currentPos, (direction*magnitude), (i/2)))
                directions.add(direction*magnitude);
        }

        return directions;
    }

    /***
     * Calculates the heuristic of {@code pos} and {@code goal}.
     * <p>
     * Uses the eucledian distance formula as the heuristic function.
     * 
     * @param pos1  – first position
     * @param pos2  – second position
     * 
     * @return heuristic value
     */
    public int findHeuristic(int pos1, int pos2){

        int heuristic = 0;

        int[] coord1 = findCoordinates(pos1);
        int[] coord2 = findCoordinates(pos2);

        for(int i = 0; i < dimension; i++)
            heuristic += (int) Math.pow(coord1[i] - coord2[i], 2);

        return heuristic;
    }

    /***
     * Translates maze position to coordinate representation.
     * 
     * @param pos   – position
     * 
     * @return coordinate representation of {@code pos}
     */
    public int[] findCoordinates(int pos){

        int[] coordinates = new int[dimension];

        for (int i = 0; i < dimension; i++)
            coordinates[i] = (pos/(numNodeWalls*((int) Math.pow(size, i)))) % size;

        return coordinates;
    }

    /***
     * Given a {@code pos1} and a {@code dir}, determine if the new position is valid.
     * <p>
     * The {@code dir} value is inputted as a directional magnitude.
     * <p>
     * The input {@code pow} is used to determine out of bounds movement.
     * 
     * @param pos1  – initial position
     * @param dir   – direction to new position
     * @param pow   – power for coordinate conversion
     * 
     * @return true if the next position is valid
     */
    public boolean checkDirection(int pos1, int dir, int pow){

        if (!nodes.containsKey(pos1) || !nodes.containsKey(pos1+dir))
            return false;

        if (pos1+dir < 0)
            return false;

        if (pos1+dir > walls.length)
            return false;

        int col = numNodeWalls * (int) Math.pow(size, pow+1);

        if (pos1 / col != (pos1 + dir) / col)
            return false;

        return true;
    }

    /***
     * Determines if there exists an open wall/path between {@code pos1} and {@code pos2}.
     * <p>
     * If the wall from {@code pos1 -> pos2} is open, then the wall from {@code pos2 -> pos1}
     * must be open as well.
     * 
     * @param pos1  - first position
     * @param pos2  - second position
     * 
     * @return true if walls between two positions exist
     */
    public boolean checkOpenWall(int pos1, int pos2){

        int dir         = pos2 - pos1;

        int direction   = (dir < 0) ? -1 : 1;
        int magnitude   = (dir < 0) ? -dir : dir;

        int wallIndex   = ((direction < 0) ? 1 : 0) + 2 * ((int) (Math.log(magnitude/numNodeWalls) / Math.log(size)));

        int wallsPos1   = pos1 + wallIndex;
        int wallsPos2   = (wallsPos1 % 2 == 0) ? pos2 + wallIndex+1: pos2 + wallIndex-1;

        return !(!walls[wallsPos1] && !walls[wallsPos2]);
    }
}