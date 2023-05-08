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
    private final Map<Character, CharacterState> characterStates = new LinkedHashMap<>();

    public final List<Character> killed = new ArrayList<>();

    @Nullable
    Integer roomWhereShoutHeard;

    public SceneState(SceneSetup setup) {
        this.setup = setup;
    }

    public void move(Character c, @Nullable Integer roomIndex) {
        characterStates.get(c).position = roomIndex;
    }

    @Nullable
    public Integer getPositionIndex(Character c) {
        final CharacterState state = characterStates.get(c);
        return state != null ? state.position : null;
    }

    public List<Character> charactersInRoom(Integer roomIndex) {
        return characterStates.entrySet().stream()
                .filter(entry -> roomIndex.equals(entry.getValue().position))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Nullable
    public CharacterState getState(Character character) {
        return characterStates.get(character);
    }

    public void applyAction(Action action) {
        if (action instanceof Action.Move) {
            final Action.Move move = (Action.Move) action;
            characterStates.get(move.character).positionKnownFromOthers =
                    charactersInRoom(move.fromRoom).stream().anyMatch(c -> !c.equals(move.character));
            move(move.character, move.toRoom);
        } else if (action instanceof Action.Talk) {
            final Action.Talk talk = (Action.Talk) action;
            talk.talking.forEach(c -> characterStates.get(c).positionKnownFromOthers = true);
        } else if (action instanceof Action.Arrive) {
            final Action.Arrive arrive = (Action.Arrive) action;
            final CharacterState newState = new CharacterState();
            newState.position = arrive.inRoom;
            characterStates.put(arrive.character, newState);
        } else if (action instanceof Action.Kill) {
            final Action.Kill kill = (Action.Kill) action;
            killed.add(kill.target);
            characterStates.get(kill.by).fleeing = true;
        } else if (action instanceof Action.Shout) {
            roomWhereShoutHeard = characterStates.get(((Action.Shout) action).by).position;
        }
    }
}
