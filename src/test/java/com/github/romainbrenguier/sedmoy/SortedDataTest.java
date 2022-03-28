package com.github.romainbrenguier.sedmoy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.romainbrenguier.sedmoy.csv.Table;
import com.github.romainbrenguier.sedmoy.csv.Row;
import com.github.romainbrenguier.sedmoy.csv.TableParser;
import com.github.romainbrenguier.sedmoy.sort.SortedData;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SortedDataTest {

  @Test
  public void testConstructor() {
    // Arrange, Act and Assert
    List<Row> discarded = (new SortedData()).getDiscarded();
    assertTrue(discarded instanceof java.util.ArrayList);
    assertTrue(discarded.isEmpty());
  }

  @Test
  public void testAdd() {
    // Arrange
    SortedData ofCsvResult = SortedData.ofCsv(new Table(new ArrayList<>()));

    // Act
    ofCsvResult.add(new Row(Arrays.asList("foo", "foo", "foo")));
  }

  @Test
  public void testOfCsv() throws IOException {
    // Arrange
    Path example = Paths
        .get(System.getProperty("user.home"), "Downloads", "mcrw10000.csv");
    Table input = new TableParser()
        .parseLines(Files.readAllLines(example))
        .limit(1000);

    // Act
    SortedData data = SortedData.ofCsv(input);

    // Assert
    assertTrue(data.nbEntries() > 0);
  }

  @Test
  public void testNbEntries() {
    // Arrange, Act and Assert
    assertEquals(0, SortedData.ofCsv(new Table(new ArrayList<>())).nbEntries());
  }
}

