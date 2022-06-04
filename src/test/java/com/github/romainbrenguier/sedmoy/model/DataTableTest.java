package com.github.romainbrenguier.sedmoy.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class DataTableTest {
  DataTable makeSimpleTable() {
    return new CsvParser().parseLines(Arrays.asList("a,b,c", "d,e,foo,bar", "g,h,i"));
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
    DataTable table = makeSimpleTable();

    // Act and Assert
    assertEquals("a", table.cell(0, 0));
    assertEquals("bar", table.cell(3, 1));
    assertEquals("", table.cell(5, 2));
  }

  @Test
  void testDownFrom() {
    // Act
    DataTable actualDownFromResult = makeSimpleTable().downFrom(2, 1);

    // Assert
    assertEquals(2, actualDownFromResult.width());
    assertEquals(2, actualDownFromResult.height());
    assertEquals("foo", actualDownFromResult.cell(0, 0));
    assertEquals("bar", actualDownFromResult.cell(1, 0));
    assertEquals("i", actualDownFromResult.cell(0, 1));
    assertEquals("", actualDownFromResult.cell(1, 1));
  }

  @Test
  void testUpFrom() {
    // Act
    DataTable actualDownFromResult = makeSimpleTable().upFrom(2, 1);

    // Assert
    assertEquals(3, actualDownFromResult.width());
    assertEquals(2, actualDownFromResult.height());
    assertEquals("a", actualDownFromResult.cell(0, 0));
    assertEquals("b", actualDownFromResult.cell(1, 0));
    assertEquals("c", actualDownFromResult.cell(2, 0));
    assertEquals("d", actualDownFromResult.cell(0, 1));
    assertEquals("e", actualDownFromResult.cell(1, 1));
    assertEquals("foo", actualDownFromResult.cell(2, 1));
  }
}

