package com.github.romainbrenguier.sedmoy.fitting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class IncrementalTest {

  @Test
  public void testConstructor() {
    // Arrange and Act
    Incremental actualIncremental = new Incremental("S");

    // Assert
    assertEquals(1, actualIncremental.baseIndex);
    assertEquals(21, actualIncremental.max);
  }

  @Test
  public void testIndex() {
    // Arrange, Act and Assert
    assertEquals(1, (new Incremental("S")).index().intValue());
  }

  @Test
  public void testNext() {
    // Arrange, Act and Assert
    assertEquals(21, ((Incremental) (new Incremental("S")).next()).max);
    assertEquals(2, ((Incremental) (new Incremental("S")).next()).baseIndex);
  }
}

