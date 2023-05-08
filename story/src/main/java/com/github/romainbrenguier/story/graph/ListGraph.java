package com.github.romainbrenguier.story.graph;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.List;

/** Graph implementation using list multimap */
public class ListGraph implements Graph {
    int maxIndex = 0;
    private final ListMultimap<Integer, Integer> connections =
            ArrayListMultimap.create();

    void connect(int roomIndex1, int roomIndex2) {
        connections.put(roomIndex1, roomIndex2);
        maxIndex = Math.max(maxIndex, Math.max(roomIndex1, roomIndex2));
    }

    @Override
    public int size() {
        return maxIndex + 1;
    }

    @Override
    public List<Integer> connectedFrom(int index) {
        return connections.get(index);
    }
}
