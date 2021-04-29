package graph;


import java.util.Iterator;
import java.util.*;

/**
 * <b>DirectedLabeledGraph</b> represents a mutable directed labeled graph consisting of nodes and labeled edges between nodes.
 *
 * <p>Specification fields:
 * @spec.specfield nodes : Set(Nodes) //The nodes in the graph
 * @spec.specfield edges : Set(Edges) //The labeled edges in the graph</p>
 *
 * <p>Abstract Invariant:
 * Two of the same node cannot be in the graph and each edge with the same parent and child must have different labels</p>
 */
public class DirectedLabeledGraph {

    /**Creates a new DirectedLabeledGraph with no nodes or edges
     * @spec.effects Constructs a new DirectedLabeledGraph with no nodes or edges
     */
    public DirectedLabeledGraph(){
        throw new UnsupportedOperationException();
    }


    /**Adds node to this without any ingoing or outgoing edges
     *
     * @param node The node to be added to the graph
     * @return true if node was added to the graph and false if node was not added (already in graph)
     * @spec.requires node is not null and node is not already in graph
     * @spec.modifies this
     * @spec.effects Adds node to this without any edges in or out of it
     */
    public boolean addNode(Node node){
        throw new UnsupportedOperationException();
    }

    /**Adds a node with label "label" to this without any ingoing or outgoing edges
     *
     * @param label The label of the node to be added to the graph
     * @return true if node was added to the graph and false if node was not added (already in graph)
     * @spec.requires node is not null and label is not already a label of a node in the graph
     * @spec.modifies this
     * @spec.effects Adds node with label without any edges in or out of it
     */
    public boolean addNode(String label){
        throw new UnsupportedOperationException();
    }



    /**Adds an edge to this with label "label" between source and destination
     *
     * @param label The weight of the edge to be added
     * @param source The Node that the added edge comes out of
     * @param destination The Node that the added edges goes into
     * @return true if the edge was added and false if the edge was not added (edge between the two nodes with same label exists)
     * @spec.requires source and destination are not null, there is not already an edge with same label from source to destination
     * @spec.modifies this
     * @spec.effects Adds the given edge with the label "label" to this from the node source to the node destination
     *
     */
    public boolean addEdge(String label, Node source, Node destination){
        throw new UnsupportedOperationException();
    }

    /**Removes the edge in this between source and destination
     *
     * @param label the label of the edge to be removed
     * @param source The node that the edge to be removed comes out of
     * @param destination The node that the edge to be removed goes into
     * @return true if the edge between the nodes was removed and false if not (edge doesn't exist)
     * @spec.requires source and destination are not null, there exists an edge between source and destination
     * @spec.modifies this
     * @spec.effects Removes the edge between source and destination in this
     */
    public boolean removeEdge(String label, Node source, Node destination){
        throw new UnsupportedOperationException();
    }

    /**Removes node from this
     *
     * @param node The node in this to be removed
     * @return true if node was removed and false if node was not removed (node did not exist in graph)
     * @spec.requires node is not null, node exists in this
     * @spec.modifies this
     * @spec.effects Removes node from this
     */
    public boolean removeNode(Node node){
        throw new UnsupportedOperationException();
    }

    /**Removes node with label "label" from this
     *
     * @param label The label of the node to be removed
     * @return true if node was removed and false if node was not removed (node did not exist in graph)
     * @spec.requires label is not null, a node with label "label" exists in this
     * @spec.modifies this
     * @spec.effects Removes the node with label "label" from this
     */
    public boolean removeNode(String label){
        throw new UnsupportedOperationException();
    }


    /**Returns a list of all of the nodes where there exists an edge from "node" to them.
     *
     * @param node The node whom's children will be in the list
     * @return A list of all of the nodes where there exists an edge from "node" to them
     * @spec.requires node is not null, node exists in this
     *
     */
    public List<Node> listChildren(Node node){
        throw new UnsupportedOperationException();
    }

    /**Provides an iterator over all of the nodes in this
     *
     * @return An iterator over all of the nodes in this
     */
    public Iterator<Node> iterator(){
        throw new UnsupportedOperationException();
    }

    /**Returns the set of nodes in this graph
     *
     * @return the set of nodes that are in this
     */
    public Set<Node> getNodes(){
        throw new UnsupportedOperationException();
    }

    /**Returns the node with a specific label in this
     *
     * @param label The label of the node that should be returned
     * @return The node in this with the label "label"
     * @throws NoSuchElementException if a node with label "label" does not exist in this
     * @spec.requires label is not null
     */
    public Node getNodeByName(String label){
        throw new UnsupportedOperationException();
    }

    /**Returns a set of the labels of the edges between two nodes
     *
     * @param source The node that the edges in the returned set are coming out of
     * @param destination The node that the edges in the returned set go into
     * @return A set of the labels of the edges between source and destination (empty set if no edges between)
     * @spec.requires source and destination are not null
     */
    public Set<String> getEdgesBetween(Node source, Node destination){
        throw new UnsupportedOperationException();
    }


    /**Removes all nodes and edges from this
     *
     * @spec.modifies this
     * @spec.effects Removes all node and edges from this, resulting in a empty graph
     */
    public void clear(){
        throw new UnsupportedOperationException();
    }

    /**
     * <b>Node</b> represents an immutable single node in a DirectedLabeledGraph
     *
     * <p>Specification fields:
     * @spec.specfield label : String //The label or name of the node</p>
     *
     */
    public class Node{

        /**Constructs a node with label "label"
         *
         * @param label The label of the new constructed node
         * @spec.requires label is not null
         * @spec.effects creates a new Node with "label" as its label
         */
        public Node(String label){
            throw new UnsupportedOperationException();
        }
    }

}
