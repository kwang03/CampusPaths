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
     * @spec.requires graph, start, and end are not null. graph does not contain any negative edge weights. Nodes with labels start and end are in graph
     * @return A Path of DirectedLabeledGraph Nodes that represents the least cost path between start and end. If no path is found in the graph, null is returned
     */
    public static <E> Path<DirectedLabeledGraph.Node<E>> findPath(E start, E end, DirectedLabeledGraph<E,Double> graph){
        if(graph == null || start == null || end == null){
            throw new IllegalArgumentException();
        }
        DirectedLabeledGraph.Node<E> startNode = new DirectedLabeledGraph.Node<>(start);
        DirectedLabeledGraph.Node<E> endNode = new DirectedLabeledGraph.Node<>(end);
        PriorityQueue<Path<DirectedLabeledGraph.Node<E>>> active = new PriorityQueue<>();
        Set<DirectedLabeledGraph.Node<E>> finished = new HashSet<>();

        active.add(new Path<>(startNode));

        while(!active.isEmpty()){
            Path<DirectedLabeledGraph.Node<E>> minPath = active.remove();
            DirectedLabeledGraph.Node<E> minDest = minPath.getEnd();

            if (minDest.equals(endNode)){
                return minPath;
            }

            if(finished.contains(minDest)){
                continue;
            }

            Set<DirectedLabeledGraph.Edge<E,Double>> edges = graph.getEdges(minDest);
            for(DirectedLabeledGraph.Edge<E,Double> e : edges){
                if(!finished.contains(e.getDestination())){
                    Path<DirectedLabeledGraph.Node<E>> newPath = minPath.extend(e.getDestination(), e.getLabel());
                    active.add(newPath);
                }
            }
            finished.add(minDest);
        }
        return null;
    }
}
