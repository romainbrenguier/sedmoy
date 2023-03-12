package com.github.romainbrenguier.story.places;

import java.util.Random;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MansionTest {

    /**
     * Method under test: {@link Mansion#make(Random)}
     */
    @Test
    void testMake() {
        Random r = new Random(42);
        final Place place = new Mansion().make(r);
        assertNotNull(place);
    }
}

