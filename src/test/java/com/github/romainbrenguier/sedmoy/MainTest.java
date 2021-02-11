package com.github.romainbrenguier.sedmoy;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class MainTest {
  @Test
  public void sort() throws IOException {
    // Arrange
    Path example = Paths
        .get(System.getProperty("user.home"), "Downloads", "mcrw10000.csv");
    MainConfig config = new MainConfig().withPath(example).withLimit(1000);

    // Act
    CsvData data = Main.sort(config);

    // Assert
    assertFalse(data.getLines().isEmpty());
  }

  @Test
  public void run() {
    // Arrange
    Path example = Paths
        .get(System.getProperty("user.home"), "Downloads", "mcrw10000.csv");
    MainConfig config = new MainConfig().withPath(example)
//        .writeMobi()
        .withLimit(1000);

    // Act
    Main.run(config);
  }
}