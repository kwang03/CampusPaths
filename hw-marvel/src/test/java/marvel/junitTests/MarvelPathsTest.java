package marvel.junitTests;

import graph.DirectedLabeledGraph;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;
import marvel.*;
import org.junit.function.ThrowingRunnable;
import org.junit.rules.Timeout;

import java.io.IOException;

/**
 * This class contains a set of test cases that can be used to test the implementation of the
 * MarvelPaths class not tested by the script tests.
 *
 * <p>
 */
public class MarvelPathsTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    @Test (expected = IllegalArgumentException.class)
    public void buildExceptionTest(){
        MarvelPaths.buildGraph("badFile.csv");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStartTest() throws IOException {
        DirectedLabeledGraph graph = MarvelPaths.buildGraph("staffSuperheroes.csv");
        MarvelPaths.findPath(null, "Ernst-the-Bicycling-Wizard", graph);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullEndTest() throws IOException {
        DirectedLabeledGraph graph = MarvelPaths.buildGraph("staffSuperheroes.csv");
        MarvelPaths.findPath("Ernst-the-Bicycling-Wizard",null, graph);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullGraphTest() {
        MarvelPaths.findPath("Ernst-the-Bicycling-Wizard","Notkin-of-the-Superhuman-Beard", null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void startDoesntExistTest() throws IOException{
        DirectedLabeledGraph graph = MarvelPaths.buildGraph("staffSuperheroes.csv");
        MarvelPaths.findPath("Dont exist", "Ernst-the-Bicycling-Wizard", graph);
    }

    @Test
    public void endDoesntExistTest() {
        DirectedLabeledGraph graph = MarvelPaths.buildGraph("staffSuperheroes.csv");
        assertNull(MarvelPaths.findPath("Ernst-the-Bicycling-Wizard", "Dont exist", graph));
    }

    @Test
    public void pathDoesntExistTest(){
        DirectedLabeledGraph graph = MarvelPaths.buildGraph("twoCharsNoPath.csv");
        assertNull(MarvelPaths.findPath("Char1", "Char2", graph));
    }
}
