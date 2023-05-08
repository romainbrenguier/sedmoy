package com.github.romainbrenguier.story.graph;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GraphAnalysis {
    public Set<Integer> reachableFrom(Graph graph, Set<Integer> start, Set<Integer> avoiding) {
        final HashSet<Integer> result = new HashSet<>();
        final ArrayDeque<Integer> toVisit = new ArrayDeque<>(start);
        while (!toVisit.isEmpty()) {
            final int node = toVisit.pop();
            if (result.add(node)) {
                for (int target : graph.connectedFrom(node)) {
                    if (!avoiding.contains(target))
                        toVisit.add(target);
                }
            }
        }
        return result;
    }

    /**
     * End nodes such that there is a path from `start` of length `n` that avoids {@code avoids[i]}
     * at each step `i`
     * @param avoids for each index i a set of nodes that are forbidden
     */
    public Set<Integer> reachableAvoidingPaths(
            Graph graph, List<Set<Integer>> avoids, Set<Integer> start) {
        Set<Integer> currentPositions = new HashSet<>(start);
        for (Set<Integer> avoid : avoids) {
            currentPositions.removeAll(avoid);
            currentPositions = reachableFrom(graph, currentPositions, avoid);
        }
        return currentPositions;
    }
}
