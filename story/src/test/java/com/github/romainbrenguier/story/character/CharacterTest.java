package com.github.romainbrenguier.story.character;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import com.github.romainbrenguier.story.character.Character;

import com.github.romainbrenguier.story.character.Status;
import org.junit.jupiter.api.Test;

class CharacterTest {
    @Test
    void testRandom2() {
        // Arrange
        Random r = new Random(42L);

        // Act
        Character actualRandomResult = Character.random(r);

        // Assert
        assertEquals(51, actualRandomResult.age);
        assertEquals(Status.Peasant, actualRandomResult.status);
        assertFalse(actualRandomResult.isWoman);
        assertEquals(2, actualRandomResult.personality.traits.size());
    }
}

