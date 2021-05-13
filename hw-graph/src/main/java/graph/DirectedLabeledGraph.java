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
public class DirectedLabeledGraph implements Iterable<DirectedLabeledGraph.Node>{

    /**
     *Holds the adjacency list representation of our graph mapping nodes to its edges
     */
    private HashMap<Node, Set<Edge>> adj;

    /**
     * Indicates if checkRep() will run in its entirety if called
     */
    private final boolean DEBUG = false;

    //Abstract Invariant: adj.keySet() is the set of nodes in this DirectedLabeledGraph.
    //                    for each node n in adj.keySet(), for each edge e in adj.get(n) an outgoing edge starts from n and uses e to get to the destination node,
    //                    The union of all of these edges make up the set of edges in this DirectedLabeledGraph

    //Representation Invariant: adj != null
    //                          for each node n in adj.keySet(), n != null
    //                          for each node n in adj.keySet(), for each edge e in adj.get(n), e != null and the destination node of e exists in the graph
    //
    //
    //                          (No duplicate nodes or edges between two nodes is implied through implementation using a hashset)

    /**Creates a new DirectedLabeledGraph with no nodes or edges
     * @spec.effects Constructs a new DirectedLabeledGraph with no nodes or edges
     */
    public DirectedLabeledGraph(){
        adj = new HashMap<>();
        checkRep();
    }


    /**Adds node to this without any ingoing or outgoing edges
     *
     * @param node The node to be added to the graph
     * @return true if node was added to the graph and false if node was not added (already in graph)
     * @spec.requires node is not null
     * @spec.modifies this
     * @spec.effects Adds node to this without any edges in or out of it or does not add to graph if node already in graph
     */
    public boolean addNode(Node node){
        checkRep();
        if(node == null){
            throw new IllegalArgumentException();
        } else if(adj.containsKey(node)){
            return false;
        }
        adj.put(node, new HashSet<>());
        checkRep();
        return true;
    }

    /**Adds a node with label "label" to this without any ingoing or outgoing edges
     *
     * @param label The label of the node to be added to the graph
     * @return true if node was added to the graph and false if node was not added (already in graph)
     * @spec.requires label is not null
     * @spec.modifies this
     * @spec.effects Adds node with label without any edges in or out of it or does not add the node if node with label already in graph
     */
    public boolean addNode(String label){
        checkRep();
        if(label == null){
            throw new IllegalArgumentException();
        }
        Node node = new Node(label);
        checkRep();
        return addNode(node);
    }



    /**Adds an edge to this with label "label" between source and destination
     *
     * @param label The weight of the edge to be added
     * @param source The Node that the added edge comes out of
     * @param destination The Node that the added edges goes into
     * @return true if the edge was added and false if the edge was not added (edge between the two nodes with same label exists)
     * @spec.requires source, destination and label are not null.
     * @spec.modifies this
     * @spec.effects Adds the given edge with the label "label" to this from the node source to the node destination or does not add the edge if it already exists.
     *               If source or destination does not exist in the graph, addEdge will add the missing node into the graph and then add the edge
     *
     */
    public boolean addEdge(String label, Node source, Node destination){
        checkRep();
        if(label == null || source == null || destination == null){
            throw new IllegalArgumentException();
        }
        Edge edge = new Edge(label, destination);
        if(!adj.containsKey(source)){
            addNode(source);
        }
        if(!adj.containsKey(destination)){
            addNode(destination);
        }
        Set<Edge> s = adj.get(source);
        boolean added = s.add(edge);
        checkRep();
        return added;
    }

    /**Removes the edge in this between source and destination
     *
     * @param label the label of the edge to be removed
     * @param source The node that the edge to be removed comes out of
     * @param destination The node that the edge to be removed goes into
     * @return true if the edge between the nodes was removed and false if not (edge doesn't exist)
     * @spec.requires source, destination and label are not null
     * @spec.modifies this
     * @spec.effects Removes the edge between source and destination in this or does nothing if the edge didn't exist
     */
    public boolean removeEdge(String label, Node source, Node destination){
        checkRep();
        if(label == null || source == null || destination == null){
            throw new IllegalArgumentException();
        }
        if(!adj.containsKey(source)){
            return false;
        }
        Edge edge = new Edge(label, destination);
        Set<Edge> s = adj.get(source);
        boolean removed = s.remove(edge);
        checkRep();
        return removed;
    }

    /**Removes node from this
     *
     * @param node The node in this to be removed
     * @return true if node was removed and false if node was not removed (node did not exist in graph)
     * @spec.requires node is not null
     * @spec.modifies this
     * @spec.effects Removes node from this and removes all occurrences of node from this or does nothing if the node did not exist in the graph
     */
    public boolean removeNode(Node node){
        checkRep();
        if(node == null){
            throw new IllegalArgumentException();
        }
        boolean removed = adj.remove(node) != null;
        if(removed){
            fixGraph(node);
        }
        checkRep();
        return removed;
    }

    /**Removes all occurrences of a removed node from the graph
     *
     * @param node The node that should be removed
     * @spec.modifies this
     * @spec.effects Removes all edges where the destination is node in this
     */
    private void fixGraph(Node node){
        for(Node source : adj.keySet()){
            Set<Edge> edges = adj.get(source);
            edges.removeIf(edge -> edge.getDestination().equals(node));
        }
    }

    /**Removes node with label "label" from this
     *
     * @param label The label of the node to be removed
     * @return true if node was removed and false if node was not removed (node did not exist in graph)
     * @spec.requires label is not null
     * @spec.modifies this
     * @spec.effects Removes the node with label "label" from this and removes all occurrences of the node with "label" or does nothing if a node with "label" does not exist
     */
    public boolean removeNode(String label){
        checkRep();
        if(label == null){
            throw new IllegalArgumentException();
        }
        Node node = new Node(label);
        checkRep();
        return removeNode(node);
    }


    /**Returns a list of all of the nodes where there exists an edge from "node" to them.
     *
     * @param node The node whom's children will be in the list
     * @return A list of all of the nodes where there exists an edge from "node" to them
     * @spec.requires node is not null, node exists in this
     *
     */
    public List<Node> listChildren(Node node){
        if(node == null || !adj.containsKey(node)){
            throw new IllegalArgumentException();
        }
        Set<Edge> edges = adj.get(node);
        List<Node> children = new ArrayList<>();
        for(Edge edge : edges){
            children.add(edge.getDestination());
        }
        return children;
    }

    /**Provides an iterator over all of the nodes in this
     *
     * @return An iterator over all of the nodes in this
     */
    public Iterator<Node> iterator(){
        Set<Node> nodes = new HashSet<>(adj.keySet());
        return nodes.iterator();
    }

    /**Returns the set of nodes in this graph
     *
     * @return the set of nodes that are in this
     */
    public Set<Node> getNodes(){
        return new HashSet<>(adj.keySet());
    }

    /**Returns the node with a specific label in this
     *
     * @param label The label of the node that should be returned
     * @return The node in this with the label "label" or null if node does not exist in this
     * @spec.requires label is not null
     */
    public Node getNodeByName(String label){
        if(label == null){
            throw new IllegalArgumentException();
        }
        for(Node node : adj.keySet()){
            if(node.getLabel().equals(label)){
                return node;
            }
        }
        return null;
    }

    /**Returns a set of the labels of the edges between two nodes
     *
     * @param source The node that the edges in the returned set are coming out of
     * @param destination The node that the edges in the returned set go into
     * @return A set of the labels of the edges between source and destination (empty set if no edges between)
     * @spec.requires source and destination are not null, source and destination both exist in the graph
     */
    public Set<String> getEdgesBetween(Node source, Node destination){
        if(source == null || destination == null || !adj.containsKey(source) || !adj.containsKey(destination)){
            throw new IllegalArgumentException();
        }
        Set<String> between = new HashSet<>();
        Set<Edge> edges = adj.get(source);
        for(Edge edge : edges){
            if(edge.getDestination().equals(destination)){
                between.add(edge.getLabel());
            }
        }
        return between;
    }

    /**Returns a set of the edges to children nodes of node
     *
     * @param node The node who's edges will be returned
     * @return A set of the edges to the children of node (empty set if no children)
     * @spec.requires node is not null and node is in the graph
     */
    public Set<Edge> getEdges(Node node){
        if(node == null || !adj.containsKey(node)){
            throw new IllegalArgumentException();
        }
        Set<Edge> edges = adj.get(node);
        return new HashSet<>(edges);
    }

    /**Removes all nodes and edges from this
     *
     * @spec.modifies this
     * @spec.effects Removes all node and edges from this, resulting in a empty graph
     */
    public void clear(){
        checkRep();
        adj.clear();
        checkRep();
    }

    /**
     * Standard hashCode function.
     *
     * @return an int that all objects equal to this will also return
     */
    @Override
    public int hashCode() {
        return adj.hashCode();
    }

    /**
     * Standard equality operation.
     *
     * @param obj the object to be compared for equality
     * @return true if and only if 'obj' is an instance of a DirectedLabeledGraph and 'this' and 'obj' represent
     * the same graph.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DirectedLabeledGraph) {
            DirectedLabeledGraph graph = (DirectedLabeledGraph) obj;
            return adj.equals(graph.adj);
        } else {
            return false;
        }
    }

    /**
     * Throws an exception if the representation invariant is violated
     */
    private void checkRep(){
        assert adj != null;
        if(DEBUG) {
            for (Node node : adj.keySet()) {
                assert node != null : "null node";
                for (Edge edge : adj.get(node)) {
                    assert edge != null : "null edge";
                    assert adj.containsKey(edge.getDestination()) : "edge with destination not in graph";
                }
            }
        }
    }

    /**
     * <b>Node</b> represents an immutable single node in a DirectedLabeledGraph
     *
     * <p>Specification fields:
     * @spec.specfield label : String //The label or name of the node</p>
     *
     */
    public static class Node{

        //Abstract Invariant: label is the label of this node in a graph

        //Representation Invariant: label != null

        /**
         * Holds the string label of this node
         */
        private final String label;

        /**Constructs a node with label "label"
         *
         * @param label The label of the new constructed node
         * @spec.requires label is not null
         * @spec.effects creates a new Node with "label" as its label
         */
        public Node(String label){
            this.label = label;
            checkRep();
        }

        /**returns the label of this node
         *
         * @return the string label of this node
         */
        public String getLabel(){
            return label;
        }

        /**
         * Standard hashCode function.
         *
         * @return an int that all objects equal to this will also return
         */
        @Override
        public int hashCode() {
            return label.hashCode();
        }

        /**
         * Standard equality operation.
         *
         * @param obj the object to be compared for equality
         * @return true if and only if 'obj' is an instance of a Node and 'this' and 'obj' represent
         * the same node.
         */
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Node) {
                return label.equals(((Node) obj).label);
            } else {
                return false;
            }
        }

        /**
         * Throws an exception if the representation invariant is violated
         */
        private void checkRep(){
            assert label != null : "label is null";
        }
    }

    /**
     * <b>Edge</b> represents an immutable single edge in a DirectedLabeledGraph with a label and a destination node
     *
     * <p>Specification fields:
     *      @spec.specfield label : String //The label or name of the edge
     *      @spec.specfield destination: Node //The destination node of this edge</p>
     */
    public static class Edge{

        //Abstract Invariant: label is the label of this edge in a graph and destination is the node that this edge points to

        //Representation Invariant: label != null and destination != null

        /**
         * The label of the edge
         */
        private final String label;
        /**
         * The destination node of the edge
         */
        private final Node destination;

        /**Creates an edge with a label and destination node
         *
         * @param label The string label of the edge
         * @param destination The node that the edge points to
         * @spec.requires label and destination are not null
         * @spec.effects creates a new edge with label "label" and "destination" as its destination node
         */
        public Edge(String label, Node destination){
            this.label = label;
            this.destination = destination;
            checkRep();
        }

        /**returns the label of this
         *
         * @return the label of this edge
         */
        public String getLabel(){
            return label;
        }

        /**returns the destination node of this
         *
         * @return the destination node
         */
        public Node getDestination(){
            return destination;
        }

        /**
         * Standard hashCode function.
         *
         * @return an int that all objects equal to this will also return
         */
        @Override
        public int hashCode(){
            return 31 * label.hashCode() + destination.hashCode();
        }

        /**
         * Standard equality operation.
         *
         * @param obj the object to be compared for equality
         * @return true if and only if 'obj' is an instance of a Edge and 'this' and 'obj' represent
         * the same edge.
         */
        @Override
        public boolean equals(Object obj){
            if(obj instanceof Edge) {
                return label.equals(((Edge) obj).getLabel()) && destination.equals(((Edge) obj).getDestination());
            } else {
                return false;
            }
        }

        /**
         * Throws an exception if the representation invariant is violated
         */
        private void checkRep(){
            assert label != null : "label is null";
            assert destination != null : "destination node is null";
        }
    }

}
