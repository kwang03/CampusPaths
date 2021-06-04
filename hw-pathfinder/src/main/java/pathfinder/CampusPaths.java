package pathfinder;

import graph.DirectedLabeledGraph;
import pathfinder.datastructures.Path;
import pathfinder.parser.CampusPath;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * CampusPaths provides a single utility method that performs Dijkstra's algorithm on a DirectedLabeledGraph to find the least cost path between two nodes
 */
public class CampusPaths{

    //CampusPaths is not an ADT, it only provides one static method. Therefore, there is no representation invariant or abstraction function

    /** Finds the least cost path between two nodes in a DirectedLabeledGraph
     *
     * @param graph The DirectedLabeledGraph that a path will be found in
     * @param start The node in the graph that the path should start from
     * @param end The node in the graph that the path should end at
     * @param <E> Type parameter of the nodes in the graph
     * @spec.requires graph does not contain any negative edge weights. Nodes with labels start and end are in graph
     * @throws IllegalArgumentException if graph, start, or end are null
     * @return A Path of Points that represents the least cost path between start and end. If no path is found in the graph, null is returned
     */
    public static <E> Path<E> findPath(E start, E end, DirectedLabeledGraph<E,Double> graph){
        if(graph == null || start == null || end == null){
            throw new IllegalArgumentException();
        }
        PriorityQueue<Path<E>> active = new PriorityQueue<>(); //Queue of paths to still be examined
        Set<DirectedLabeledGraph.Node<E>> finished = new HashSet<>(); //Set of nodes we have found shortest path to

        active.add(new Path<>(start));

        while(!active.isEmpty()){
            Path<E> minPath = active.remove(); //Shortest path currently in active queue
            E minDest = minPath.getEnd(); //The node that the path goes to

            if (minDest.equals(end)){ //We found the node we were looking for!
                return minPath;
            }

            if(finished.contains(new DirectedLabeledGraph.Node<>(minDest))){ //Already found a shorter path to the node
                continue;
            }

            Set<DirectedLabeledGraph.Edge<E,Double>> edges = graph.getEdges(new DirectedLabeledGraph.Node<>(minDest));
            for(DirectedLabeledGraph.Edge<E,Double> e : edges){ //Go through all of the edges that the node has
                if(!finished.contains(e.getDestination())){
                    Path<E> newPath = minPath.extend(e.getDestination().getLabel(), e.getLabel()); //Extend the current path with the additional edge
                    active.add(newPath); //Add the new path to the queue of paths
                }
            }
            finished.add(new DirectedLabeledGraph.Node<>(minDest)); //Add the node we just looked at to the finished set
        }
        return null; //No path found return null
    }
}
