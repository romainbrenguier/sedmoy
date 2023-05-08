package com.github.romainbrenguier.story.solver;

import com.github.romainbrenguier.story.character.Character;
import com.github.romainbrenguier.story.scene.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Moves {
    static class State {
        Map<Character, Integer> map = new HashMap<>();

        State copy() {
            return copy(map);
        }

        static State copy(Map<Character, Integer> map) {
            final State state = new State();
            state.map.putAll(map);
            return state;
        }

        void set(Character c, Integer pos) {
            map.put(c, pos);
        }
    }

    /** Transform a sequence of moves into a sequence of states (position) */
    List<State> stateSequenceFromMove(
            Map<Character, Integer> initial, Iterable<Action.Move> moves) {
        final ArrayList<State> result = new ArrayList<>();
        final State initialState = State.copy(initial);
        result.add(initialState);
        State lastState = initialState;
        for (Action.Move move : moves) {
            final State newState = lastState.copy();
            newState.set(move.character, move.toRoom);
            result.add(newState);
            lastState = newState;
        }
        return result;
    }
}
