package com.github.romainbrenguier.story;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;

class StatusTest {
    /**
     * Method under test: {@link Status#random(Random)}
     */
    @Test
    void testRandom() {
        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     Random.haveNextNextGaussian
        //     Random.nextNextGaussian
        //     Random.seed

        // Arrange
        Random random = new Random();

        // Act
        Status.random(random);
    }

    /**
     * Method under test: {@link Status#random(Random)}
     */
    @Test
    void testRandom2() {
        // Arrange
        Random random = new Random(42L);

        // Act and Assert
        assertEquals(Status.Scholar, Status.random(random));
    }

    /**
     * Method under test: {@link Status#random(Random)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRandom3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at com.github.romainbrenguier.story.Status.random(Status.java:24)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange and Act
        Status.random(null);
    }

    /**
     * Method under test: {@link Status#random(Random)}
     */
    @Test
    void testRandom4() {
        // Arrange
        Random random = new Random(1L);

        // Act and Assert
        assertEquals(Status.Aristocrat, Status.random(random));
    }

    /**
     * Method under test: {@link Status#random(Random)}
     */
    @Test
    void testRandom5() {
        // Arrange
        Random random = new Random(-1L);

        // Act and Assert
        assertEquals(Status.Prostitute, Status.random(random));
    }

    /**
     * Method under test: {@link Status#random(Random)}
     */
    @Test
    void testRandom6() {
        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     Random.haveNextNextGaussian
        //     Random.nextNextGaussian
        //     Random.seed
        //     ThreadLocalRandom.initialized

        // Arrange and Act
        Status.random(ThreadLocalRandom.current());
    }

    /**
     * Method under test: {@link Status#random(Random)}
     */
    @Test
    void testRandom7() {
        // TODO: Complete this test.
        //   Diffblue AI was unable to find a test

        // Arrange
        SecureRandom random = new SecureRandom();

        // Act
        Status.random(random);
    }
}

