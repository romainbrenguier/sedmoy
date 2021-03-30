package com.github.romainbrenguier.sedmoy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.romainbrenguier.sedmoy.sort.LexicographicComparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import org.junit.jupiter.api.Test;

public class InteractiveModeTest {

  @Test
  public void testSort() {
    // Arrange
    InteractiveMode interactiveMode = new InteractiveMode(Arrays.asList(
        "foo", "bar", "baz"));
    Comparator<String> order = new LexicographicComparator("abforz");

    // Act
    interactiveMode.sort(order);

    // Assert
    assertEquals("bar", interactiveMode.inputData.get(0));
    assertEquals("baz", interactiveMode.inputData.get(1));
    assertEquals("foo", interactiveMode.inputData.get(2));
  }
}

