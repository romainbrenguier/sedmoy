package com.github.romainbrenguier.story.solver;

import com.github.romainbrenguier.story.character.Character;
import com.github.romainbrenguier.story.graph.GraphAnalysis;
import com.github.romainbrenguier.story.graph.Graph;
import com.github.romainbrenguier.story.scene.Action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * Transform a sequence of moves into a sequence of states (position)
     */
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

    /**
     * Set of states reachable at time i, without beeing seen by witness whose position at time i
     * is given in the list of states
     */
    Set<Integer> reachableUnseen(
            Graph graph, List<State> witnesses, Character character, Integer startPosition) {
        final ArrayList<Set<Integer>> result = new ArrayList<>();
        final List<Set<Integer>> toAvoid = witnesses.stream()
                .map(state -> positionToAvoid(character, state)).collect(Collectors.toList());
        return new GraphAnalysis().reachableAvoidingPaths(graph, toAvoid,
                Collections.singleton(startPosition));
    }

    private static Set<Integer> positionToAvoid(Character character, State state) {
        return state.map.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(character))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
}
