package com.github.romainbrenguier.story.graph;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public class Analysis {
    Set<Integer> reachableFrom(Graph graph, Set<Integer> start, Set<Integer> avoiding) {
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
}
