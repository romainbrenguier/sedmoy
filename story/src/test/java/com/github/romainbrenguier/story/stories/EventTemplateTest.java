package com.github.romainbrenguier.story.stories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.romainbrenguier.story.stories.EventTemplate;

import java.util.List;

import org.junit.jupiter.api.Test;

class EventTemplateTest {
    /**
     * Method under test: {@link EventTemplate#templates()}
     */
    @Test
    void testTemplates() {
        // Arrange and Act
        List<EventTemplate> actualTemplatesResult = EventTemplate.templates();

        // Assert
        assertEquals(5, actualTemplatesResult.size());
    }
}

