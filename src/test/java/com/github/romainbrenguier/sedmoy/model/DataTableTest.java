package com.github.romainbrenguier.sedmoy.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class DataTableTest {
  DataTable makeSimpleTable() {
    return new CsvParser().parseLines(Arrays.asList("a,b,c", "d,e,foo,bar", "g,h,i"));
  }

  @Test
  public void testHeight() {
    // Act and Assert
    assertEquals(3, makeSimpleTable().height());
  }

  @Test
  public void testWidth() {
    // Act and Assert
    assertEquals(4, makeSimpleTable().width());
  }

  @Test
  public void testCell() {
    // Arrange
    DataTable table = makeSimpleTable();

    // Act and Assert
    assertEquals("a", table.cellAsString(0, 0));
    assertEquals("bar", table.cellAsString(3, 1));
    assertEquals("", table.cellAsString(5, 2));
  }

  @Test
  public void testDownFrom() {
    // Act
    DataTable actualDownFromResult = makeSimpleTable().downFrom(2, 1);

    // Assert
    assertEquals(2, actualDownFromResult.width());
    assertEquals(2, actualDownFromResult.height());
    assertEquals("foo", actualDownFromResult.cellAsString(0, 0));
    assertEquals("bar", actualDownFromResult.cellAsString(1, 0));
    assertEquals("i", actualDownFromResult.cellAsString(0, 1));
    assertEquals("", actualDownFromResult.cellAsString(1, 1));
  }

  @Test
  public void testUpFrom() {
    // Act
    DataTable actualDownFromResult = makeSimpleTable().upFrom(2, 1);

    // Assert
    assertEquals(3, actualDownFromResult.width());
    assertEquals(2, actualDownFromResult.height());
    assertEquals("a", actualDownFromResult.cellAsString(0, 0));
    assertEquals("b", actualDownFromResult.cellAsString(1, 0));
    assertEquals("c", actualDownFromResult.cellAsString(2, 0));
    assertEquals("d", actualDownFromResult.cellAsString(0, 1));
    assertEquals("e", actualDownFromResult.cellAsString(1, 1));
    assertEquals("foo", actualDownFromResult.cellAsString(2, 1));
  }
}

