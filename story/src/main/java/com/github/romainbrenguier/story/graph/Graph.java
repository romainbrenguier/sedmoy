package com.github.romainbrenguier.story.graph;

import java.util.List;

public interface Graph {
    int size();

    /** It is assumed indexes go from 0 to size() - 1 */
    List<Integer> connectedFrom(int index);
}
