package com.github.romainbrenguier.story;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SceneState {
    private final SceneSetup setup;

    /**
     * Indexes corresponds to room indexes, or null if not present
     */
    private final Map<Character, Integer> positions = new LinkedHashMap<>();

    public final List<Character> killed = new ArrayList<>();

    @Nullable
    Integer roomWhereShoutHeard;

    public SceneState(SceneSetup setup) {
        this.setup = setup;
    }

    public void move(Character c, @Nullable Integer roomIndex) {
        positions.replace(c, roomIndex);
    }

    @Nullable
    public Integer getPositionIndex(Character c) {
        return positions.get(c);
    }

    public List<Character> charactersInRoom(Integer roomIndex) {
        return positions.entrySet().stream()
                .filter(entry -> entry.getValue().equals(roomIndex))
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }

    public void applyAction(Action action) {
        if (action instanceof Action.Move) {
            final Action.Move move = (Action.Move) action;
            move(move.character, move.toRoom);
        } else if (action instanceof Action.Arrive) {
            final Action.Arrive arrive = (Action.Arrive) action;
            positions.put(arrive.character, arrive.inRoom);
        } else if (action instanceof Action.Kill) {
            killed.add(((Action.Kill) action).target);
        } else if (action instanceof Action.Shout) {
            roomWhereShoutHeard = positions.get(((Action.Shout) action).by);
        }
    }
}
