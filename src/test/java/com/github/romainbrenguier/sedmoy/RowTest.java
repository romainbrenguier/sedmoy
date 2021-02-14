package com.github.romainbrenguier.sedmoy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class RowTest {

  @Test
  public void testOfCsvLine() {
    // Arrange and Act
    Row actualOfCsvLineResult = Row.ofCsvLine(new String[]{"Line"});

    // Assert
    assertEquals(0, actualOfCsvLineResult.data.length);
    assertEquals("Line", actualOfCsvLineResult.key);
    assertNull(actualOfCsvLineResult.index);
  }

  @Test
  public void testOfCsvLine2() {
    // Arrange and Act
    Row actualOfCsvLineResult = Row.ofCsvLine(new String[]{"Line", "Line"});

    // Assert
    assertEquals(1, actualOfCsvLineResult.data.length);
    assertEquals("Line", actualOfCsvLineResult.key);
    assertNull(actualOfCsvLineResult.index);
  }

  @Test
  public void testWithIndex() {
    // Arrange, Act and Assert
    assertEquals(1,
        Row.ofCsvLine(new String[]{"foo", "foo", "foo"}).withIndex(1).index.intValue());
  }

  @Test
  public void testLengthOfFirstWord() {
    // Arrange, Act and Assert
    assertEquals(3, Row.ofCsvLine(new String[]{"foo", "foo", "foo"}).lengthOfFirstWord());
    assertEquals(1, Row.lengthOfFirstWord("S"));
  }

  @Test
  public void testLengthOfFirstWord2() {
    // Arrange
    Row ofCsvLineResult = Row.ofCsvLine(new String[]{"foo", "foo", "foo"});
    ofCsvLineResult.withIndex(1);

    // Act and Assert
    assertEquals(3, ofCsvLineResult.lengthOfFirstWord());
  }

  @Test
  public void testToCsvLine() {
    // Arrange, Act and Assert
    assertEquals(2, Row.ofCsvLine(new String[]{"foo", "foo", "foo"}).toCsvLine(1).length);
    assertEquals(1, Row.ofCsvLine(new String[]{null}).toCsvLine(1).length);
    assertEquals(1, Row.ofCsvLine(new String[]{"foo", "foo", "foo"}).toCsvLine(0).length);
    assertEquals(3, Row.ofCsvLine(new String[]{"foo", "foo", "foo"}).toCsvLine(2).length);
  }
}

