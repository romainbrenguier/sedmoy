package com.github.romainbrenguier.story;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Disabled;

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

