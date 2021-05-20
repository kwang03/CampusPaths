package pathfinder.junitTests;

import graph.DirectedLabeledGraph;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import pathfinder.CampusPaths;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;


/**
 * This class contains a set of test cases that can be used to test the implementation of the
 * CampusPaths class not tested in the script tests.
 *
 * <p>
 */
public class CampusPathsTest {
    @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    @Test (expected = IllegalArgumentException.class)
    public void nullStartTest(){
        DirectedLabeledGraph<String, Double> graph = new DirectedLabeledGraph<>();
        graph.addNode("one");
        CampusPaths.findPath(null, "one", graph);
    }

    @Test (expected = IllegalArgumentException.class)
    public void nullEndTest(){
        DirectedLabeledGraph<String, Double> graph = new DirectedLabeledGraph<>();
        graph.addNode("one");
        CampusPaths.findPath("one", null, graph);
    }

    @Test (expected = IllegalArgumentException.class)
    public void nullGraphTest(){
        CampusPaths.findPath("one","one", null);
    }

    @Test
    public void noPathTest(){
        DirectedLabeledGraph<String, Double> graph = new DirectedLabeledGraph<>();
        DirectedLabeledGraph.Node<String> one = new DirectedLabeledGraph.Node<>("one");
        DirectedLabeledGraph.Node<String> two = new DirectedLabeledGraph.Node<>("two");
        DirectedLabeledGraph.Node<String> three = new DirectedLabeledGraph.Node<>("three");
        graph.addNode(one);
        graph.addNode(two);
        graph.addNode(three);
        //Test no path with graph with no edges
        assertNull(CampusPaths.findPath("one","two",graph));

        //Test no path with graph with one edges
        graph.addEdge(1.0,one,two);
        assertNull(CampusPaths.findPath("two","one",graph));

        //Test no path with two edges
        graph.addEdge(1.0, two, three);
        assertNull(CampusPaths.findPath("two", "one", graph));
    }


}
