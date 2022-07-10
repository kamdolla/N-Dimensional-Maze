# N Dimensional Maze Generator & Solver

Generates a boolean array representing a n-dimensional, equilateral maze.

Implements Kruskal's algorithm to generate a random, unique, and challenging maze. Applies advanced disjoint data structure techniques like union by rank and path compression to minimize creation runtime.

Utilizes Dijkstra's algorithm to find a mininum spanning tree of the graph, validating that each node of the maze can be reached from each other node.

Computes a solution path uses A* searching algorithm with a Eucledian distance heuristic–– additionally ensuring the solution path is the shortest path to the solution node.

This project is intended to be expanded on, incorporating an API interface to request a maze and using Unreal Engine 5 to simulate maze in 4-dimensional space.

## Usage

Required to generate mazes, the **Maze.java** and **Node.java** files will be necesarry. The *Maze.create()* method to generate the maze and *Maze.solve()* to solve the generated maze.

The **Node.java** class is required for graph/maze functionalities and utilizes disjoint data structure methods, like *Node.findSet()* and *Node.union()*.

### API Interface

Use the **index.js** to use the API on the localhost (port is 8080). Remeber to initialize and import the following 'java' and 'express' modules.

> $ npm init
> $ npm install java
> $ npm install express

Use a Insomnia or Postman application to interact with the API, if you please. Requests should come from JSON format:

```yaml
{
    "dimension" : 2,
    "size" : 5
}
```

## Codebase

As list above, the codebase consist of a maze class, node class, and sample run file.

### Maze.java

Used to generate maze, solve maze, and validate that the maze in entirely traversable.

Represents the maze as a boolean array. Each node in the graph is dedicated some amount of indicies, determined by dimension and size, and each of its indicies indicates whether a wall in that direction is open or not in the maze–– meaning a path is traversable between those two nodes.

### Node.java

Used to describe a node in a graph. These nodes have disjoint data strucutre functionalities, and searching algorithm compatible constructor variables. 

Constructor variables include the nodes position in the graph, the rank & parent of the disjoint set the node belongs to, and a 'if visited' variable. 

Disjoint data structure functions, like *Node.findSet()* and *Node.union()* use "path compression" and "union by rank", respectively. These nodes also have {@code isVisited()}, a method used for searching algorithms, which find the shortest path from one node to another.