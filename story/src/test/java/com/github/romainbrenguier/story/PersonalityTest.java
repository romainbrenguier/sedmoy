package com.github.romainbrenguier.story;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;

class PersonalityTest {

    @Test
    void testRandom2() {
        final Personality personality = Personality.random(new Random(42L));
        assertEquals(2, personality.traits.size());
    }

}

