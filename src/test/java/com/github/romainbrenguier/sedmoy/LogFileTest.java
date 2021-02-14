package com.github.romainbrenguier.sedmoy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class LogFileTest {

  @Test
  public void testDiffString() {
    // Arrange
    LogFile logFile = new LogFile();
    LocalTime previous = LocalTime.of(1, 1);

    // Act and Assert
    assertEquals("        0ms", logFile.diffString(previous, LocalTime.of(1, 1)));
  }

  @Test
  public void testDiffString2() {
    // Arrange
    LogFile logFile = new LogFile();

    // Act and Assert
    assertEquals("NA", logFile.diffString(null, LocalTime.of(1, 1)));
  }

  @Test
  public void testDiffString3() {
    // Arrange
    LogFile logFile = new LogFile();

    // Act and Assert
    assertEquals("NA", logFile.diffString(LocalTime.of(1, 1), null));
  }


}

