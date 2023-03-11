package com.github.romainbrenguier.story;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;

class StatusTest {

    @Test
    void testRandom2() {
        assertEquals(Status.Scholar, Status.random(new Random(42L)));
        assertEquals(Status.Aristocrat, Status.random(new Random(1L)));
        assertEquals(Status.Prostitute, Status.random(new Random(-1L)));
    }
}

