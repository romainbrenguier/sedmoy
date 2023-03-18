package com.github.romainbrenguier.story;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class SceneState {
    private final SceneSetup setup;

    /** Indexes corresponds to room indexes, or null if not present*/
    private final Map<Character, Integer> positions = new LinkedHashMap<>();

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

    public void applyAction(Action action) {
        if (action instanceof Action.Move) {
            final Action.Move move = (Action.Move) action;
            move(move.character, move.toRoom);
        } else if (action instanceof Action.Arrive) {
            final Action.Arrive arrive = (Action.Arrive) action;
            positions.put(arrive.character, arrive.inRoom);
        }
    }
}
