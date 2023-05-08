package com.github.romainbrenguier.story.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class AnalysisTest {
    /**
     * Method under test: {@link Analysis#reachableFrom(Graph, Set, Set)}
     */
    @Test
    void testReachableFrom() {
        // Arrange
        Analysis analysis = new Analysis();
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

}

