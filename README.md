## N-Dimensional Maze

Implemented Kruskal's algorithm, utilized path compression and union by rank, and used depth first search to create and solve an n-dimensional maze. Applied advanced knowledge of disjoint data structures and minimum spanning tree algorithms.

## Maze Implementation

In order to make a complete and valid maze, creates a boolean array and MazeNode object for each cell in the array. The boolean array represents walls and the direction (i.e. [+x, -x, +y, -y, ...]), which will be true if wall exists and false if wall does not. The MazeNode object is used in the disjoint data structure operations-- such as unioning two node trees together, finding the root of a node tree, and comparing two nodes to see if they equal each other. These two elements are necessary to store and manipulate the cells in data.

An important clarification to give on the representation of cells is that it is one large array of boolean wall arrays, as opposed to making a matrix to represent a maze. In other words, as opposed to using a coordinate system to label cells (i.e. (x=0, y=2, z=3)), each cell has is labeled by one integer. Getting from one position to another is determined by the direction you are moving in, and one which axis. For instance, to move in the right, your destination will be: [ destination = start_pos + 1 ]; the negative x will be [ destination = start_pos - 1 ]; the positive y [ destination = start_pos + size]; the negative y [ destination = start_pos - size]. Here listed is the generalization for moving from one position to another:

+X : destination = start_pos + size^0
-X : destination = start_pos - size^0
+Y : destination = start_pos + size^1
-Y : destination = start_pos - size^1
+Z : destination = start_pos + size^2
-Z : destination = start_pos - size^2
+T : destination = start_pos + size^3
-T : destination = start_pos - size^3

## Kruskal's Algorithm

(Contained within "createMaze()" in the Maze class)

Kruskal's algorithm is an algorithm that finds a mininum spanning tree in a graph. For this implementation, nodes in the graph are represented by MazeNode objects, and edges are adjacent nodes that share no wall between them. Related to the "cost" of a mininum spanning tree, all edges between nodes can be assumed to have a cost of 1.

With each cell having a representitive MazeNode object, the algorithm first starts by making each MazeNode object have a parent pointer to itself. Then, with each iteration of the create method, randomly chosen adjacent nodes are unioned together to create larger, disjoint trees. The process continues until all nodes share the same root-- meaning any node can be reached from any node. This implementation is used to make the maze interesting with the randomly created structure, and difficult with no isolated areas.

## DFS Solver

(Contained within "solveMaze()" and "dfs()" in the Maze class)

To solve the maze, the algorithm uses a depth first search to find the path from one designated starting position to an end position. Prints path in terminal.

## Printing Maze

(Contained within "printMaze()" in the Maze class)

Pratically, you would only want to print the maze if it was below two dimensions, as it would be not very simple to represent even a 3D maze in text format. Furthermore, the maze can be printed in the terminal (either 1 dimensional or 2 dimensional) with arrows indicating with cells can be moved between.