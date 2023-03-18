package com.github.romainbrenguier.story;

import com.github.romainbrenguier.story.places.Room;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SceneTest {

    /**
     * Method under test: {@link Scene#make(Random)}
     */
    @Test
    void testMake() {
        // Arrange
        Random r = new Random(42L);

        // Act
        Scene actualMakeResult = Scene.make(r);

        // Assert
        List<TimedAction> timedActionList = actualMakeResult.actions;
        String report = actualMakeResult.report();
        assertEquals(10, timedActionList.size());
        assertNotNull(report);
    }
}

