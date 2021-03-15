public class MazeNode {
    //***************************************************//
    //          NOTES – MazeNode Object                 
    // MazeNode is used to create a valid n-dimensional maze
    // MazeNode holds an integer positional value, a rank, and parent
    //
    // Position:
    // – Integer representing the spacial coordinate of the node
    // – [x, y, z] = [position % maze.size, (position/maze.size) % maze.size, (position/maze.size^2) % maze.size]
    //
    // Rank:
    // - Integer used when combining trees of nodes
    // - Lower rank tree becomes child of higher rank tree
    //
    // Parent:
    // - Points to the parent of the node tree
    // - Can be self-referential
    //***************************************************//

    private int         position;
    private int         rank;
    private MazeNode    parent;

    public MazeNode(int position){
        this.position = position;
        this.rank = 0;
        this.parent = this;
    }

    //***************************************************//
    //          NOTES – Class Methods                
    //***************************************************//

    public boolean isEquals(MazeNode other){
        return (this.position == other.position);
    }

    public MazeNode findSet(){
        return findSet(this);
    }

    public MazeNode findSet(MazeNode curr){
        if (curr == curr.parent){
            return curr;
        }

        curr.parent = findSet(curr.parent);
        return curr.parent;
    }

    public boolean union(MazeNode other){
        MazeNode parent1 = findSet(this);
        MazeNode parent2 = findSet(other);

        if (parent1.position != parent2.position){
            if (parent1.rank == parent2.rank){
                parent1.rank++;
                parent2.parent = parent1;
            }

            if (parent1.rank > parent2.rank){
                parent2.parent = parent1;
            }

            if (parent2.rank > parent1.rank){
                parent1.parent = parent2;
            }
            return true;
        }
    
        return false;
    }
}
