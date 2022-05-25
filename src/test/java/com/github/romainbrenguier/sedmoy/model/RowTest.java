package com.github.romainbrenguier.sedmoy.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class RowTest {

  @Test
  public void testOfCsvLine() {
    // Arrange and Act
    Row actualOfCsvLineResult = new Row(Collections.singletonList("Line"));

    // Assert
    assertEquals("Line", actualOfCsvLineResult.column(0));
    assertEquals("", actualOfCsvLineResult.column(1));
  }

  @Test
  public void testOfCsvLine2() {
    // Arrange and Act
    Row actualOfCsvLineResult = new Row(Arrays.asList("foo", "bar"));

    // Assert
    assertEquals("foo", actualOfCsvLineResult.column(0));
    assertEquals("bar", actualOfCsvLineResult.column(1));
    assertEquals("", actualOfCsvLineResult.column(2));
  }
}

