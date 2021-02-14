package com.github.romainbrenguier.sedmoy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    SortedData ofCsvResult = SortedData.ofCsv(CsvData.parseLines(new ArrayList<>()));

    // Act
    ofCsvResult.add(new Row(new String[]{"foo", "foo", "foo"}));
  }

  @Test
  public void testOfCsv() throws IOException {
    // Arrange
    Path example = Paths
        .get(System.getProperty("user.home"), "Downloads", "mcrw10000.csv");
    CsvData input = CsvData
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
    assertEquals(0, SortedData.ofCsv(CsvData.parseLines(new ArrayList<>())).nbEntries());
  }

  @Test
  public void testToCsv() {
    // Arrange, Act and Assert
    assertEquals("",
        SortedData.ofCsv(CsvData.parseLines(new ArrayList<String>())).toCsv().toString());
  }

  @Test
  public void testToCsv2() {
    // Arrange
    ArrayList<String> stringList = new ArrayList<String>();
    stringList.add("foo");

    // Act
    CsvData actualToCsvResult = SortedData.ofCsv(CsvData.parseLines(stringList)).toCsv();

    // Assert
    assertEquals("100,foo", actualToCsvResult.toString());
    assertEquals(1, actualToCsvResult.getLines().size());
  }

  @Test
  public void testToCsv3() {
    // Arrange
    ArrayList<String> stringList = new ArrayList<String>();
    stringList.add("foo");
    CsvData parseLinesResult = CsvData.parseLines(stringList);
    parseLinesResult.addLine(new String[]{"Line"});

    // Act
    CsvData actualToCsvResult = SortedData.ofCsv(parseLinesResult).toCsv();

    // Assert
    assertEquals("100,Line\n100,foo", actualToCsvResult.toString());
    assertEquals(2, actualToCsvResult.getLines().size());
  }
}

