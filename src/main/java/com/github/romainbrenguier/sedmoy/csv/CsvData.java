package com.github.romainbrenguier.sedmoy.csv;

import com.github.romainbrenguier.sedmoy.sort.SortedData;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CsvData {

  public List<Row> getLines() {
    return lines;
  }

  private final List<Row> lines;

  private static String SEPARATOR = ",";

  public CsvData() {
    lines = new ArrayList<>();
  }

  public void addLine(Row row) {
    lines.add(row);
  }

  public static CsvData parseLines(List<String> lines) {
    return new CsvData(lines.stream()
        .map(line -> line.replace("\"", ""))
        .map(line -> Row.split(line, SEPARATOR))
        .collect(Collectors.toList()));
  }

  private CsvData(List<Row> lines) {
    this.lines = lines;
  }

  public CsvData limit(int number) {
    return new CsvData(lines.stream().limit(number).collect(Collectors.toList()));
  }

  private List<String> toStrings(String separator) {
    return lines.stream()
        .map(row -> row.toString(separator))
        .collect(Collectors.toList());
  }

  private String toString(String separator) {
    return String.join(System.lineSeparator(), toStrings(separator));
  }

  @Override
  public String toString() {
    return toString(SEPARATOR);
  }

  public CsvData sort() {
    SortedData sortedData = SortedData.ofCsv(this);
    sortedData.printStats();
    return sortedData.toCsv();
  }
}
