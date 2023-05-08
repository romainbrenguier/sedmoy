package com.github.romainbrenguier.story.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class AnalysisTest {
    /**
     * Method under test: {@link GraphAnalysis#reachableFrom(Graph, Set, Set)}
     */
    @Test
    void testReachableFrom() {
        // Arrange
        GraphAnalysis analysis = new GraphAnalysis();
        ListGraph graph = new ListGraph();
        graph.connect(0, 1);
        graph.connect(1, 2);
        graph.connect(3, 2);
        Set<Integer> start = Collections.singleton(0);

        // Act and Assert
        assertEquals(3,
                analysis.reachableFrom(graph, start, Collections.emptySet())
                        .size());
    }

    /**
     * Method under test: {@link GraphAnalysis#reachableAvoidingPaths(Graph, List, Set)}
     */
    @Test
    void testReachableAvoidingPaths() {
        // Arrange
        GraphAnalysis analysis = new GraphAnalysis();

        ListGraph listGraph = new ListGraph();
        listGraph.connect(0, 1);
        listGraph.connect(1, 2);
        listGraph.connect(0, 2);
        listGraph.connect(3, 2);

        ArrayList<Set<Integer>> avoids = new ArrayList<>();
        avoids.add(Collections.singleton(1));
        avoids.add(Collections.singleton(2));
        avoids.add(Collections.singleton(0));

        Set<Integer> start = Collections.singleton(0);

        // Act
        Set<Integer> actualReachableAvoidingPathsResult =
                analysis.reachableAvoidingPaths(listGraph, avoids,
                start);

        // Assert
        assertEquals(2, actualReachableAvoidingPathsResult.size());
        assertTrue(actualReachableAvoidingPathsResult.contains(1));
        assertTrue(actualReachableAvoidingPathsResult.contains(2));
    }

}

