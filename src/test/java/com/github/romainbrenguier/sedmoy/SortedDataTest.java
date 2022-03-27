package com.github.romainbrenguier.sedmoy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.romainbrenguier.sedmoy.csv.Table;
import com.github.romainbrenguier.sedmoy.csv.Row;
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
    SortedData ofCsvResult = SortedData.ofCsv(Table.parseLines(new ArrayList<>()));

    // Act
    ofCsvResult.add(new Row(Arrays.asList("foo", "foo", "foo")));
  }

  @Test
  public void testOfCsv() throws IOException {
    // Arrange
    Path example = Paths
        .get(System.getProperty("user.home"), "Downloads", "mcrw10000.csv");
    Table input = Table
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
    assertEquals(0, SortedData.ofCsv(Table.parseLines(new ArrayList<>())).nbEntries());
  }

  @Test
  public void testToCsv() {
    // Arrange, Act and Assert
    assertEquals("",
        SortedData.ofCsv(Table.parseLines(new ArrayList<>())).toCsv().toString());
  }

  @Test
  public void testToCsv2() {
    // Arrange
    ArrayList<String> stringList = new ArrayList<>();
    stringList.add("foo");

    // Act
    Table actualToCsvResult = SortedData.ofCsv(Table.parseLines(stringList)).toCsv();

    // Assert
    assertEquals("300,foo", actualToCsvResult.toString());
    assertEquals(1, actualToCsvResult.getLines().size());
  }

  @Test
  public void testToCsv3() {
    // Arrange
    ArrayList<String> stringList = new ArrayList<>();
    stringList.add("foo");
    Table parseLinesResult = Table.parseLines(stringList);
    parseLinesResult.addLine(new Row(Collections.singletonList("Line")));

    // Act
    Table actualToCsvResult = SortedData.ofCsv(parseLinesResult).toCsv();

    // Assert
    assertEquals("0,Line\n300,foo", actualToCsvResult.toString());
    assertEquals(2, actualToCsvResult.getLines().size());
  }
}

