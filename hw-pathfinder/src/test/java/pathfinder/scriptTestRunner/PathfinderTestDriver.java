/*
 * Copyright (C) 2021 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2021 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */


package pathfinder.scriptTestRunner;

import graph.DirectedLabeledGraph;

import marvel.*;
import pathfinder.CampusPaths;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    // ***************************
    // ***  JUnit Test Driver  ***
    // ***************************

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/
    private final Map<String, DirectedLabeledGraph<String,Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @spec.requires r != null && w != null
     * @spec.effects Creates a new GraphTestDriver which reads command from
     * {@code r} and writes results to {@code w}
     **/
    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @throws IOException if the input or output sources encounter an IOException
     * @spec.effects Executes the commands read from the input and writes results to the output
     **/
    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "FindPath":
                    findPath(arguments);
                    break;
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {

        graphs.put(graphName, new DirectedLabeledGraph<String,Double>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {

        DirectedLabeledGraph<String,Double> graph = graphs.get(graphName);
        graph.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);
        double doubleEdgeLabel = Double.parseDouble(edgeLabel);

        addEdge(graphName, parentName, childName, doubleEdgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         Double edgeLabel) {

        DirectedLabeledGraph<String,Double> graph = graphs.get(graphName);
        DirectedLabeledGraph.Node<String> parent = new DirectedLabeledGraph.Node<>(parentName);
        DirectedLabeledGraph.Node<String> child = new DirectedLabeledGraph.Node<>(childName);
        graph.addEdge(edgeLabel,parent,child);
        output.println("added edge " + String.format("%.3f", edgeLabel) + " from " + parentName + " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {

        DirectedLabeledGraph<String,Double> graph = graphs.get(graphName);
        Set<DirectedLabeledGraph.Node<String>> nodes = graph.getNodes();

        List<DirectedLabeledGraph.Node<String>> nodeList = new ArrayList<>(nodes);

        nodeList.sort(new sortNodes());
        output.print(graphName + " contains:");
        for(DirectedLabeledGraph.Node<String> node : nodeList){
            output.print(" " + node.getLabel());
        }
        output.println();
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {

        DirectedLabeledGraph<String,Double> graph = graphs.get(graphName);
        Set<DirectedLabeledGraph.Edge<String,Double>> edges = graph.getEdges(new DirectedLabeledGraph.Node<String>(parentName));

        List<DirectedLabeledGraph.Edge<String,Double>> edgeList = new ArrayList<>(edges);

        edgeList.sort(new sortEdgeDestination().thenComparing(new sortEdgeNames()));

        output.print("the children of " + parentName + " in " + graphName + " are:");
        for(DirectedLabeledGraph.Edge<String,Double> edge : edgeList){
            output.print(" " + edge.getDestination().getLabel() + "(" + String.format("%.3f", edge.getLabel()) + ")");
        }
        output.println();
    }

    private void findPath(List<String> arguments){
        if(arguments.size() != 3) {
            throw new CommandException("Bad arguments to FindPath: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeA = arguments.get(1);
        String nodeB = arguments.get(2);
        findPath(graphName, nodeA, nodeB);
    }

    private void findPath(String graphName, String nodeA, String nodeB){
        DirectedLabeledGraph<String,Double> graph = graphs.get(graphName);
        DirectedLabeledGraph.Node<String> node1 = new DirectedLabeledGraph.Node<>(nodeA);
        DirectedLabeledGraph.Node<String> node2 = new DirectedLabeledGraph.Node<>(nodeB);
        if(!graph.getNodes().contains(node1) || !graph.getNodes().contains(node2)){
            unknownChars(node1, node2, graph);
            return;
        }
        Path<String> path = CampusPaths.findPath(nodeA, nodeB, graph);
        output.println("path from " + nodeA + " to " + nodeB + ":");
        if(path == null){
            output.println("no path found");
            return;
        }
        for(Path<String>.Segment segment: path){
            String startNode = segment.getStart();
            String destNode = segment.getEnd();
            double weight = segment.getCost();
            output.println(startNode + " to " + destNode + " with weight " + String.format("%.3f", weight));
        }
        output.println("total cost: " + String.format("%.3f",path.getCost()));
    }

    private void unknownChars(DirectedLabeledGraph.Node<String> node1, DirectedLabeledGraph.Node<String> node2, DirectedLabeledGraph<String,Double> graph){
        if(!graph.getNodes().contains(node1)){
            output.println("unknown: " + node1.getLabel());
        }
        if(!graph.getNodes().contains(node2)){
            output.println("unknown: " + node2.getLabel());
        }
    }

    private static class sortNodes implements Comparator<DirectedLabeledGraph.Node<String>>{
        public int compare(DirectedLabeledGraph.Node<String> a, DirectedLabeledGraph.Node<String> b){
            return a.getLabel().compareTo(b.getLabel());
        }
    }

    private static class sortEdgeNames implements Comparator<DirectedLabeledGraph.Edge<String,Double>>{
        public int compare(DirectedLabeledGraph.Edge<String,Double> a, DirectedLabeledGraph.Edge<String,Double> b){
            return a.getLabel().compareTo(b.getLabel());
        }
    }

    private static class sortEdgeDestination implements Comparator<DirectedLabeledGraph.Edge<String,Double>>{
        public int compare(DirectedLabeledGraph.Edge<String,Double> a, DirectedLabeledGraph.Edge<String,Double> b){
            return a.getDestination().getLabel().compareTo(b.getDestination().getLabel());
        }
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }


}
