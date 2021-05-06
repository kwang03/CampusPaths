package graph.junitTests;

import graph.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;


/**
 * This class contains a set of test cases that can be used to test the implementation of the
 * DirectedLabeledGraph class.
 *
 * <p>
 */
public class GraphTest {
    @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested
    //Some nodes to use for testing
    private DirectedLabeledGraph.Node one = new DirectedLabeledGraph.Node("one");
    private DirectedLabeledGraph.Node two = new DirectedLabeledGraph.Node("two");
    private DirectedLabeledGraph.Node three = new DirectedLabeledGraph.Node("three");
    //@BeforeClass

    //Some methods to get basic graph examples without edges
    private  DirectedLabeledGraph empty(){
        return new DirectedLabeledGraph();
    }

    private DirectedLabeledGraph oneNode(){
        DirectedLabeledGraph oneNode = new DirectedLabeledGraph();
        oneNode.addNode(one);
        return oneNode;
    }

    private DirectedLabeledGraph twoNodes(){
        DirectedLabeledGraph twoNodes = new DirectedLabeledGraph();
        twoNodes.addNode(one);
        twoNodes.addNode(two);
        return twoNodes;
    }

    //Some methods to get basic graph examples with edges
    private DirectedLabeledGraph getOneEdgeTwoNodes(){
        DirectedLabeledGraph oneEdgeTwoNodes = new DirectedLabeledGraph();
        oneEdgeTwoNodes.addNode(one);
        oneEdgeTwoNodes.addNode(two);
        oneEdgeTwoNodes.addEdge("e1", one, two);
        return oneEdgeTwoNodes;
    }

    private DirectedLabeledGraph getTwoEdgesThreeNodes(){
        DirectedLabeledGraph twoEdgesThreeNodes = new DirectedLabeledGraph();
        twoEdgesThreeNodes.addNode(one);
        twoEdgesThreeNodes.addNode(two);
        twoEdgesThreeNodes.addNode(three);
        twoEdgesThreeNodes.addEdge("e1",one,two);
        twoEdgesThreeNodes.addEdge("e2",two,three);
        return twoEdgesThreeNodes;
    }

    //Helper functions to get special graph cases
    private DirectedLabeledGraph oneEdgeThreeNodes(){
        DirectedLabeledGraph oneEdgeThreeNodes = new DirectedLabeledGraph();
        oneEdgeThreeNodes.addNode(one);
        oneEdgeThreeNodes.addNode(two);
        oneEdgeThreeNodes.addNode(three);
        oneEdgeThreeNodes.addEdge("e1",one,two);
        return oneEdgeThreeNodes;
    }

    private DirectedLabeledGraph threeNodes(){
        DirectedLabeledGraph threeNodes = new DirectedLabeledGraph();
        threeNodes.addNode(one);
        threeNodes.addNode(two);
        threeNodes.addNode(three);
        return threeNodes;
    }

    private DirectedLabeledGraph twoEdgesTwoNodes(){
        DirectedLabeledGraph twoEdgesTwoNodes = new DirectedLabeledGraph();
        twoEdgesTwoNodes.addNode(one);
        twoEdgesTwoNodes.addNode(two);
        twoEdgesTwoNodes.addEdge("e1",one,two);
        twoEdgesTwoNodes.addEdge("e2", one,two);
        return twoEdgesTwoNodes;
    }

    private DirectedLabeledGraph selfLoop(){
        DirectedLabeledGraph selfLoop = new DirectedLabeledGraph();
        selfLoop.addNode(one);
        selfLoop.addEdge("e1", one, one);
        return selfLoop;
    }

    //Equals and Hashcode Tests
    @Test
    public void equalsTest(){
        //Empty graph test
        DirectedLabeledGraph g = new DirectedLabeledGraph();
        assertTrue(g.equals(g));
        assertTrue(g.equals(empty()));
        assertTrue(empty().equals(g));
        assertFalse(g.equals(null));

        //One node test
        g.addNode(one);
        assertTrue(g.equals(g));
        assertTrue(g.equals(oneNode()));
        assertTrue(oneNode().equals(g));
        assertFalse(g.equals(null));

        //Two node test
        g.addNode(two);
        assertTrue(g.equals(g));
        assertTrue(g.equals(twoNodes()));
        assertTrue(twoNodes().equals(g));
        assertFalse(g.equals(null));

        //Two node one edge test
        g.addEdge("e1", one, two);
        assertTrue(g.equals(g));
        assertTrue(g.equals(getOneEdgeTwoNodes()));
        assertTrue(getOneEdgeTwoNodes().equals(g));
        assertFalse(g.equals(null));

        //Two edge three node test
        g.addNode(three);
        g.addEdge("e2", two, three);
        assertTrue(g.equals(g));
        assertTrue(g.equals(getTwoEdgesThreeNodes()));
        assertTrue(getTwoEdgesThreeNodes().equals(g));
        assertFalse(g.equals(null));
    }

    @Test
    public void hashCodeTest(){
        //Empty graph tests
        DirectedLabeledGraph g = new DirectedLabeledGraph();
        assertTrue(g.hashCode() == g.hashCode());
        assertTrue(g.hashCode() == empty().hashCode());

        //One node test
        g.addNode(one);
        assertTrue(g.hashCode() == g.hashCode());
        assertTrue(g.hashCode() == oneNode().hashCode());

        //two node test
        g.addNode(two);
        assertTrue(g.hashCode() == g.hashCode());
        assertTrue(g.hashCode() == twoNodes().hashCode());

        //Two node one edge test
        g.addEdge("e1",one,two);
        assertTrue(g.hashCode() == g.hashCode());
        assertTrue(g.hashCode() == getOneEdgeTwoNodes().hashCode());

        //two edge three node test
        g.addNode(three);
        g.addEdge("e2",two,three);
        assertTrue(g.hashCode() == g.hashCode());
        assertTrue(g.hashCode() == getTwoEdgesThreeNodes().hashCode());
    }

    @Test
    public void equalsNodeTest(){
        //"One" node test
        DirectedLabeledGraph.Node node = new DirectedLabeledGraph.Node("one");
        assertTrue(node.equals(one));
        assertTrue(node.equals(node));
        assertFalse(node.equals(null));

        //"Two" node test
        node = new DirectedLabeledGraph.Node("two");
        assertTrue(node.equals(two));
        assertTrue(node.equals(node));
        assertFalse(node.equals(null));
    }

    @Test
    public void hashCodeNodeTest(){
        //"One" node test
        DirectedLabeledGraph.Node node = new DirectedLabeledGraph.Node("one");
        assertTrue(node.hashCode() == node.hashCode());
        assertTrue(node.hashCode() == one.hashCode());

        //"Two" node test
        node = new DirectedLabeledGraph.Node("two");
        assertTrue(node.hashCode() == node.hashCode());
        assertTrue(node.hashCode() == two.hashCode());
    }

    @Test
    public void equalsEdgeTest(){
        //Edge test
        DirectedLabeledGraph.Edge e1 = new DirectedLabeledGraph.Edge("e1", one);
        DirectedLabeledGraph.Edge e2 = new DirectedLabeledGraph.Edge("e1", one);
        DirectedLabeledGraph.Edge e3 = new DirectedLabeledGraph.Edge("e2",one);
        DirectedLabeledGraph.Edge e4 = new DirectedLabeledGraph.Edge("e1",two);
        //Equality test
        assertTrue(e1.equals(e2));
        assertTrue(e2.equals(e1));
        assertTrue(e1.equals(e1));
        assertFalse(e1.equals(null));

        //Inequality test
        assertFalse(e3.equals(e1));
        assertFalse(e1.equals(e3));

        assertFalse(e4.equals(e1));
        assertFalse(e1.equals(e4));
    }

    @Test
    public void hashCodeEdgeTest(){
        //"One" node test
        DirectedLabeledGraph.Edge e1 = new DirectedLabeledGraph.Edge("e1", one);
        DirectedLabeledGraph.Edge e2 = new DirectedLabeledGraph.Edge("e1", one);
        assertTrue(e1.hashCode() == e1.hashCode());
        assertTrue(e1.hashCode() == e2.hashCode());
    }

    //Remove Node Tests
    @Test
    public void testRemoveNodeNodeArg(){
        //Remove from single node graph
        DirectedLabeledGraph g = oneNode();
        g.removeNode(one);
        assertEquals(g, empty());
        //Test return is false if node not removed
        assertFalse(g.removeNode(three));
        assertEquals(empty(), g);

        //Remove from two node graph
        g = twoNodes();
        g.removeNode(two);
        assertEquals(g, oneNode());
        //Test return is false if node not removed
        assertFalse(g.removeNode(three));
        assertEquals(oneNode(), g);

        //Remove from two nodes one edge
        g = getOneEdgeTwoNodes();
        g.removeNode(two);
        assertEquals(g, oneNode());
        //Test return is false if node not removed
        assertFalse(g.removeNode(three));
        assertEquals(oneNode(), g);

        //Remove from three nodes two edges
        g = getTwoEdgesThreeNodes();
        g.removeNode(three);
        assertEquals(g, getOneEdgeTwoNodes());
        //Test return is false if node not removed
        assertFalse(g.removeNode(three));
        assertEquals(getOneEdgeTwoNodes(), g);
    }

    @Test
    public void testRemoveNodeStringArg(){
        //Remove from single node graph
        DirectedLabeledGraph g = oneNode();
        g.removeNode("one");
        assertEquals(g, empty());
        //Test return is false if node not removed
        assertFalse(g.removeNode("fake"));
        assertEquals(empty(), g);

        //Remove from two node graph
        g = twoNodes();
        g.removeNode("two");
        assertEquals(g, oneNode());
        //Test return is false if node not removed
        assertFalse(g.removeNode("fake"));
        assertEquals(oneNode(), g);

        //Remove from two nodes one edge
        g = getOneEdgeTwoNodes();
        g.removeNode("two");
        assertEquals(g, oneNode());
        //Test return is false if node not removed
        assertFalse(g.removeNode("fake"));
        assertEquals(oneNode(), g);

        //Remove from three nodes two edges
        g = getTwoEdgesThreeNodes();
        g.removeNode("three");
        assertEquals(g, getOneEdgeTwoNodes());
        //Test return is false if node not removed
        assertFalse(g.removeNode("fake"));
        assertEquals(getOneEdgeTwoNodes(), g);


    }

    //Remove Edge Tests
    @Test
    public void testRemoveEdge(){
        //Remove edge from two node one edge
        DirectedLabeledGraph g = getOneEdgeTwoNodes();
        g.removeEdge("e1", one, two);
        assertEquals(g, twoNodes());
        //Test return false if edge doesnt exist
        assertFalse(g.removeEdge("fake", one, two));
        assertEquals(g, twoNodes());

        //Remove edges from three nodes two edges
        g = getTwoEdgesThreeNodes();
        g.removeEdge("e2", two, three);
        assertEquals(g, oneEdgeThreeNodes());
        //Test return false if edge doesnt exist
        assertFalse(g.removeEdge("fake", one, two));
        assertEquals(g, oneEdgeThreeNodes());
        //Remove next edge
        g.removeEdge("e1", one, two);
        assertEquals(g, threeNodes());
        //Test return false if edge doesnt exist
        assertFalse(g.removeEdge("fake", one, two));
        assertEquals(g, threeNodes());

        //Test self loop
        g = selfLoop();
        g.removeEdge("e1",one,one);
        assertEquals(g, oneNode());
        assertFalse(g.removeEdge("fake", one, one));
        assertEquals(oneNode(),g);
    }

    //Clear test
    @Test
    public void clearTest(){
        DirectedLabeledGraph g = oneNode();
        g.clear();
        assertEquals(g, empty());

        g = twoNodes();
        g.clear();
        assertEquals(g, empty());

        g = twoNodes();
        g.clear();
        assertEquals(g, empty());

        g = getOneEdgeTwoNodes();
        g.clear();
        assertEquals(g,empty());

        g = getTwoEdgesThreeNodes();
        g.clear();
        assertEquals(g,empty());

        g = empty();
        g.clear();
        assertEquals(g, empty());
    }

    //Get node by name test
    @Test
    public void getNodeByNameTest(){
        //Test one node graph
        DirectedLabeledGraph g = oneNode();
        assertEquals(g.getNodeByName("one"), one);
        assertNull(g.getNodeByName("fake"));
        //Test two node graph
        g = twoNodes();
        assertEquals(g.getNodeByName("one"),one);
        assertEquals(g.getNodeByName("two"),two);
        assertNull(g.getNodeByName("fake"));
        //Test graph with one edge two nodes
        g = getOneEdgeTwoNodes();
        assertEquals(g.getNodeByName("one"),one);
        assertEquals(g.getNodeByName("two"),two);
        assertNull(g.getNodeByName("fake"));
        //Test graph with two edges three nodes
        g = getTwoEdgesThreeNodes();
        assertEquals(g.getNodeByName("one"),one);
        assertEquals(g.getNodeByName("two"),two);
        assertEquals(g.getNodeByName("three"),three);
        assertNull(g.getNodeByName("fake"));
    }


    //Get edges between test
    @Test
    public void getEdgesBetweenTest(){
        //Test no edges in between
        DirectedLabeledGraph g = twoNodes();
        HashSet<String> s = new HashSet<>();
        assertEquals(g.getEdgesBetween(one, two), s);

        //Test one edge in between
        g = getOneEdgeTwoNodes();
        s.add("e1");
        assertEquals(g.getEdgesBetween(one,two), s);
        //Test self loop
        g = selfLoop();
        assertEquals(g.getEdgesBetween(one, one), s);

        //Test two edges in between
        g = twoEdgesTwoNodes();
        s.add("e2");
        assertEquals(g.getEdgesBetween(one, two), s);
    }

    //Get Edges test
    @Test
    public void getEdgesTest(){
        //No edges test
        DirectedLabeledGraph g = oneNode();
        HashSet<DirectedLabeledGraph.Edge> s = new HashSet<>();
        assertEquals(g.getEdges(one), s);

        //One edge test
        g = getOneEdgeTwoNodes();
        s.add(new DirectedLabeledGraph.Edge("e1", two));
        assertEquals(s, g.getEdges(one));

        //Two edge test
        g = twoEdgesTwoNodes();
        s.add(new DirectedLabeledGraph.Edge("e2",two));
        assertEquals(s, g.getEdges(one));
    }


    //Iterator Test
    @Test
    public void iteratorTest(){
        DirectedLabeledGraph g = empty();
        HashSet<DirectedLabeledGraph.Node> expected = new HashSet<>();
        HashSet<DirectedLabeledGraph.Node> actual = new HashSet<>();
        for (DirectedLabeledGraph.Node node : g) {
            expected.add(node);
        }
        assertEquals(actual,expected);

        g = oneNode();
        expected = new HashSet<>();
        expected.add(one);
        actual = new HashSet<>();
        for (DirectedLabeledGraph.Node node : g) {
            actual.add(node);
        }
        assertEquals(actual,expected);

        g = twoNodes();
        expected = new HashSet<>();
        expected.add(one);
        expected.add(two);
        actual = new HashSet<>();
        for (DirectedLabeledGraph.Node node : g) {
            actual.add(node);
        }
        assertEquals(actual,expected);

        g = getOneEdgeTwoNodes();
        expected = new HashSet<>();
        expected.add(one);
        expected.add(two);
        actual = new HashSet<>();
        for (DirectedLabeledGraph.Node node : g) {
            actual.add(node);
        }
        assertEquals(actual,expected);

        g = getTwoEdgesThreeNodes();
        expected = new HashSet<>();
        expected.add(one);
        expected.add(two);
        expected.add(three);
        actual = new HashSet<>();
        for (DirectedLabeledGraph.Node node : g) {
            actual.add(node);
        }
        assertEquals(actual,expected);
    }
}
