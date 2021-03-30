package com.github.romainbrenguier.sedmoy.sort;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import org.junit.jupiter.api.Test;

class LexicographicComparatorTest {

  @Test
  void testCompare() {
    Comparator<String> order = new LexicographicComparator("abforz");
    assertTrue(order.compare("foo", "bar") > 0);
    assertTrue(order.compare("bar", "baz") < 0);
    assertEquals(0, order.compare("bar", "bar"));
  }
}