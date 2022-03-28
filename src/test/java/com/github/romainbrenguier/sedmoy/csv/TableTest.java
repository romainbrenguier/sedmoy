package com.github.romainbrenguier.sedmoy.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class TableTest {
  Table makeSimpleTable() {
    return new TableParser().parseLines(Arrays.asList("a,b,c", "d,e,foo,bar", "g,h,i"));
  }

  @Test
  void testHeight() {
    // Act and Assert
    assertEquals(3, makeSimpleTable().height());
  }

  @Test
  void testWidth() {
    // Act and Assert
    assertEquals(4, makeSimpleTable().width());
  }

  @Test
  void testCell() {
    // Arrange
    Table table = makeSimpleTable();

    // Act and Assert
    assertEquals("a", table.cell(0, 0));
    assertEquals("bar", table.cell(3, 1));
    assertEquals("", table.cell(5, 2));
  }

  @Test
  void testDownFrom() {
    // Act
    Table actualDownFromResult = makeSimpleTable().downFrom(2, 1);

    // Assert
    assertEquals(2, actualDownFromResult.width());
    assertEquals(2, actualDownFromResult.height());
    assertEquals("foo", actualDownFromResult.cell(0, 0));
    assertEquals("bar", actualDownFromResult.cell(1, 0));
    assertEquals("i", actualDownFromResult.cell(0, 1));
    assertEquals("", actualDownFromResult.cell(1, 1));
  }
}

