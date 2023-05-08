package com.github.romainbrenguier.story.solver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.romainbrenguier.story.character.Character;
import com.github.romainbrenguier.story.scene.Action;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.custom_hash.TObjectByteCustomHashMap;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;

class MovesTest {
    /**
     * Method under test: {@link Moves#stateSequenceFromMove(Map, Iterable)}
     */
    @Test
    void testStateSequenceFromMove() {
        // Arrange
        Moves moves = new Moves();
        HashMap<Character, Integer> initial = new HashMap<>();
        Random r = new Random(0);
        final Character character1 = Character.random(r);
        final Character character2 = Character.random(r);
        initial.put(character1, 1);
        initial.put(character2, 2);

        LinkedHashSet<Action.Move> moveSet = new LinkedHashSet<>();
        Action.Move e = new Action.Move();
        e.character = character1;
        e.toRoom = 3;
        moveSet.add(e);
        Action.Move e1 = new Action.Move();
        e1.character = character2;
        e1.toRoom = 3;
        moveSet.add(e1);

        // Act
        List<Moves.State> actualStateSequenceFromMoveResult = moves.stateSequenceFromMove(initial, moveSet);

        // Assert
        assertEquals(3, actualStateSequenceFromMoveResult.size());
        assertEquals(1, actualStateSequenceFromMoveResult.get(0).map.get(character1));
        assertEquals(3, actualStateSequenceFromMoveResult.get(1).map.get(character1));
        assertEquals(3, actualStateSequenceFromMoveResult.get(2).map.get(character1));
        assertEquals(2, actualStateSequenceFromMoveResult.get(0).map.get(character2));
        assertEquals(2, actualStateSequenceFromMoveResult.get(1).map.get(character2));
        assertEquals(3, actualStateSequenceFromMoveResult.get(2).map.get(character2));
    }

}

