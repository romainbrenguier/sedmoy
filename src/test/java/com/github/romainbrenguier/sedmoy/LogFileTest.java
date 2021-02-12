package com.github.romainbrenguier.sedmoy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class LogFileTest {

  @Test
  public void testConstructor() {
    // Arrange
    ArrayList<String> stringList = new ArrayList<String>();
    stringList.add("12:34:56.789 foo");
    stringList.add("12:35:43.210 bar baz");

    // Act
    LogFile actualLogFile = new LogFile(stringList, " ");

    // Assert
    assertEquals("NA      foo\n46421ms bar baz\n", actualLogFile.toString());
  }

  @Test
  public void testTimedLineToString() {
    // Arrange
    LogFile.TimedLine file = new LogFile.TimedLine(
        LocalTime.of(1, 1), "Not all who wander are lost");
    // Act
    String actual = file.toString();

    // Assert
    assertEquals("01:01:00 Not all who wander are lost", actual);
  }

  @Test
  public void testTimedLineOfString() {
    // Arrange and Act
    LogFile.TimedLine actualOfStringResult =
        LogFile.TimedLine.ofString("12:34:56.789 foo", " ");

    // Assert
    assertEquals("foo", actualOfStringResult.content);
    assertEquals("12:34:56.789", actualOfStringResult.time.toString());
  }
}

