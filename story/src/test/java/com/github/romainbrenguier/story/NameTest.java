package com.github.romainbrenguier.story;

import java.util.Random;

import com.github.romainbrenguier.story.character.Name;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NameTest {
    @Test
    void testMake() {
        // Arrange
        Random r = new Random();

        // Act
        final Name name = Name.make(r, true, true);

        // Assert
        assertEquals(name.first, "Estelle");
        assertEquals(name.last, "Levesque");
    }

}

