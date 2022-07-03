/***
 * Used to describe a node in a graph. 
 * These nodes have 
 *      disjoint data strucutre functionalities, and
 *      searching algorithm compatible constructor variables.
 * <p>
 * Constructor variables include
 *      the nodes position in the graph,
 *      the rank & parent of the disjoint set the node belongs to,
 *      and a 'if visited' variable.
 * <p>
 * Disjoint data structure functions, like {@code findSet()} and {@code union()}
 * use "path compression" and "union by rank", respectively.
 * <p>
 * These nodes also have {@code isVisited()}, a method used for searching algorithms,
 * which find the shortest path from one node to another.
 * 
 * @author  Kameron Melvin {kmelvin22@amherst.edu} 
 * @since   1.0
 * @version 1.0
 */
public class Node {
    
    // Constructor variables for creating maze functions
    private int pos;
    private int rank;
    private Node parent;

    // Constructor variables for solving maze functions
    private boolean visited;

    /***
     * Node object for graph.
     * <p>
     * Sets node position, disjoint set variables, and searching algorithm variables.
     * 
     * @param node_pos
     */
    public Node(int node_pos){
        this.pos    = node_pos;
        this.rank   = 0;
        this.parent = this;

        this.visited = false;
    }

    /***
     * Finds the {@code parent} of {@code this}'s disjoint set.
     *  
     * @return {@code Node} – disjoint set parent 
     */
    public Node findSet(){

        return findSet(this);
    }

    /***
     * Finds the {@code parent} of {@code inputNode}'s disjoint set.
     * <p>
     * Path compression :: will set {@code this.parent} to {@code parent} of disjoint set.
     * 
     * @param   inputNode   – node (of disjoint set)
     *  
     * @return  {@code Node} disjoint set parent 
     */
    public Node findSet(Node inputNode){
        if (inputNode.getParent() == inputNode)
            return inputNode;

        inputNode.parent = findSet(inputNode.parent);

        return inputNode.parent;
    }

    /***
     * Unions disjoint sets of {@code nodeA} and {@code nodeB}
     * <p>
     * Union by rank :: highest ranked {@code parent} of becomes {@code parent} of disjoint set.
     * 
     * @param   nodeA   - node (of disjoint set)
     * @param   nodeB   - node (of disjoint set)
     * 
     * @return  {@code boolean} true if sets can be unioned.
     */
    public boolean union(Node nodeA, Node nodeB){
        Node parentA = nodeA.findSet();
        Node parentB = nodeB.findSet();

        if (parentA.getPos() == parentB.getPos())
            return false;

        if (parentA.getRank() == parentB.getRank())
            parentA.rank++;

        if (parentA.getRank() > parentB.getRank())
            parentB.parent = parentA;

        else
            parentA.parent = parentB;
        
        return true;
    }

    /***
     * {@code this.visited} becomes true.
     * <p>
     * Used for solving maze purposes.
     * 
     * @return {@code boolean} true if visited
     */
    public void visit(){
        
        visited = true;
    }

    /***
     * Used for solving maze purposes.
     * 
     * @return {@code boolean} true if visited
     */
    public boolean isVisited(){

        return visited;
    }

    /***
     * String representation of the {@link Node} object using {@link Object#toString()}. 
     * 
     * @return Node {@code pos=val}, {@code rank=val}, and {@code parent=val}.
     */
    public String toString() {
        return "{pos=" + pos + ", rank=" + rank + ", parent=" + parent.pos + "}";
    }

    /**
     * @return int return the pos
     */
    public int getPos() {
        return pos;
    }
    
    /**
     * @return int return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param parent the parent to get
     */
    public Node getParent() {
        return parent;
    }
}
