package com.github.romainbrenguier.sedmoy.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.romainbrenguier.sedmoy.model.Row;
import org.junit.jupiter.api.Test;

public class RowTest {

  @Test
  public void testOfCsvLine() {
    // Arrange and Act
    List<String> actualOfCsvLineResult = Collections.singletonList("Line");

    // Assert
    assertEquals("Line", Row.column(actualOfCsvLineResult, 0));
    assertEquals("", Row.column(actualOfCsvLineResult, 1));
  }

  @Test
  public void testOfCsvLine2() {
    // Arrange and Act
    List<String> actualOfCsvLineResult = Arrays.asList("foo", "bar");

    // Assert
    assertEquals("foo", Row.column(actualOfCsvLineResult, 0));
    assertEquals("bar", Row.column(actualOfCsvLineResult, 1));
    assertEquals("", Row.column(actualOfCsvLineResult, 2));
  }
}

