package marvel;
import graph.DirectedLabeledGraph;

import java.io.IOException;
import java.util.*;

/**
 * MarvelPaths contains utilities to build a network DirectedLabeledGraph of characters from a csv file and find the shortest path between two characters
 * in a network DirectedLabeledGraph. Additionally, it can be run from the command line to take input of two marvel characters and output the shortest
 * path between them
 */
public class MarvelPaths {
    //MarvelPaths only contains static methods and is never constructed therefore it is not an ADT and does not have a abstraction function or representation invariant


    /**
     * The name of the file containing data about characters and books they are in
     */
    private static final String fileName = "marvel.csv";

    /**Creates a DirectedLabeledGraph network graph from a csv file containing names and books the names are in separated by a comma
     *
     * @param fileName The name of the csv file that will be used to create a network graph from
     * @return A DirectedLabeledGraph representing the relationships between characters where the nodes are characters and the edges are books the characters are both in
     * @throws IllegalArgumentException if there is a problem with the input file
     */
    public static DirectedLabeledGraph buildGraph(String fileName) {
        DirectedLabeledGraph graph = new DirectedLabeledGraph();
        try {
            Map<String, List<String>> map = MarvelParser.parseData(fileName);
            for (String book : map.keySet()) {
                for (String name1 : map.get(book)) {
                    DirectedLabeledGraph.Node name1Node = new DirectedLabeledGraph.Node(name1);
                    graph.addNode(name1Node);
                    for (String name2 : map.get(book)) {
                        DirectedLabeledGraph.Node name2Node = new DirectedLabeledGraph.Node(name2);
                        graph.addNode(name2Node);
                        if (!name1.equals(name2)) {
                            graph.addEdge(book, name1Node, name2Node);
                        }
                    }
                }
            }
            return graph;
        } catch (IOException e){
            throw new IllegalArgumentException("Problem reading the input file");
        }
    }


    /**Finds the shortest path between two characters in a network graph
     *
     * @param start The character that the path should begin at
     * @param end The character that the path should arrive at
     * @param graph The network graph that a path must be found within
     * @return A list of DirectedLabeledGraph Edges that represent the shortest path between start character and end character or null if no path exists. Note the start node will
     *         not be in the returned list, the path is implied to start at "start"
     * @throws IllegalArgumentException if start, end or graph are null or if start is not a node in graph
     */
    public static List<DirectedLabeledGraph.Edge> findPath(String start, String end, DirectedLabeledGraph graph){
        if(start == null || end == null || graph == null){
            throw new IllegalArgumentException();
        }
        DirectedLabeledGraph.Node startNode = new DirectedLabeledGraph.Node(start);
        DirectedLabeledGraph.Node endNode = new DirectedLabeledGraph.Node(end);
        Queue<DirectedLabeledGraph.Node> nodeQueue = new LinkedList<>(); //Queue holding next nodes to explore
        Map<DirectedLabeledGraph.Node, List<DirectedLabeledGraph.Edge>> paths = new HashMap<>(); //Maps each node to the shortest path to get there

        nodeQueue.add(startNode);
        paths.put(startNode, new ArrayList<>());
        while(!nodeQueue.isEmpty()){
            DirectedLabeledGraph.Node currNode = nodeQueue.remove();
            if(currNode.equals(endNode)){ //We found the destination node
                return paths.get(currNode);
            }
            List<DirectedLabeledGraph.Edge> edgeList = new ArrayList<>(graph.getEdges(currNode));
            edgeList.sort(new sortEdgeDestination().thenComparing(new sortEdgeNames())); //Sort the list in lexicographical order
            for(DirectedLabeledGraph.Edge edge : edgeList){
                DirectedLabeledGraph.Node destNode = edge.getDestination();
                if(!paths.containsKey(destNode)){
                    List<DirectedLabeledGraph.Edge> currPath = paths.get(currNode);
                    List<DirectedLabeledGraph.Edge> newPath = new ArrayList<>(currPath);
                    newPath.add(edge); //Shortest path to parent node + edge from parent to child = shortest path to child
                    paths.put(destNode, newPath);
                    nodeQueue.add(destNode);
                }
            }
        }
        return null;
    }


    /**
     * A private helper class that implements the Comparator interface and sorts DirectedLabeledGraph Edges by the labels of the edges themselves
     */
    private static class sortEdgeNames implements Comparator<DirectedLabeledGraph.Edge>{
        public int compare(DirectedLabeledGraph.Edge a, DirectedLabeledGraph.Edge b){
            return a.getLabel().compareTo(b.getLabel());
        }
    }

    /**
     * A private helper class that implements the Comparator interface and sorts DirectedLabeledGraph Edges by the labels of the edge children
     */
    private static class sortEdgeDestination implements Comparator<DirectedLabeledGraph.Edge>{
        public int compare(DirectedLabeledGraph.Edge a, DirectedLabeledGraph.Edge b){
            return a.getDestination().getLabel().compareTo(b.getDestination().getLabel());
        }
    }

    /** Runs a command line interactive program with the user, prompting the user for two marvel character names and outputs
     *  the shortest path between these two characters in the marvel.csv file.
     *
     * @param args command-line argument that wont be used by the program
     */
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        DirectedLabeledGraph graph = buildGraph(fileName);
        String again = "y";
        System.out.println("Welcome to the CSE331 Marvel Paths Finder!");

        while(again.equalsIgnoreCase("y")) {
            System.out.println();
            System.out.print("Please enter the first character you want to find a path from: ");
            String char1 = input.next();
            System.out.println();
            System.out.print("Please enter the second character you want to find a path to: ");
            String char2 = input.next();
            System.out.println();

            DirectedLabeledGraph.Node node1 = new DirectedLabeledGraph.Node(char1);
            DirectedLabeledGraph.Node node2 = new DirectedLabeledGraph.Node(char2);
            if(!graph.getNodes().contains(node1) || !graph.getNodes().contains(node2)){
                unknownChars(node1, node2, graph);
                again = again();
                continue;
            }

            List<DirectedLabeledGraph.Edge> path = findPath(char1, char2, graph);
            if(path == null){
                System.out.println("no path found");
                again = again();
                continue;
            }
            System.out.println("Shortest path from " + char1 + " to " + char2 + " is of length " + path.size() +":");
            String previous = char1;
            for(DirectedLabeledGraph.Edge edge : path){
                String destChar = edge.getDestination().getLabel();
                String book = edge.getLabel();
                System.out.println(previous + " to " + destChar + " via " + book);
                previous = destChar;
            }
            again = again();
        }
        System.out.println("Mr.Stark I don't feel so good...");
    }


    /** Private helper method that asks the user if they would like to find another path
     *
     * @return the string the user types in response to if they would like to find another path
     */
    private static String again(){
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.print("Would you like to find another path? (y for yes, anything else for no): ");
        return input.next();
    }

    /** Private helper method that prints out passed nodes that are not in the graph
     *
     * @param node1 First node that might not be in the graph
     * @param node2 Second node that might not be in the graph
     * @param graph The graph in which we are looking for whether node1 or node2 are not in the graph
     */
    private static void unknownChars(DirectedLabeledGraph.Node node1, DirectedLabeledGraph.Node node2, DirectedLabeledGraph graph){
        if(!graph.getNodes().contains(node1)){
            System.out.println("Unknown Character: " + node1.getLabel());
        }
        if(!graph.getNodes().contains(node2)){
            System.out.println("Unknown Character: " + node2.getLabel());
        }
    }
}
