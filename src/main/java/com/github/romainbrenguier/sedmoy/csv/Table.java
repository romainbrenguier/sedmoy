package com.github.romainbrenguier.sedmoy.csv;

import com.github.romainbrenguier.sedmoy.sort.SortedData;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Table {

  private final List<Row> lines;

  private static final String SEPARATOR = ",";

  public List<Row> getLines() {
    return lines;
  }

  public Table() {
    lines = new ArrayList<>();
  }

  public void addLine(Row row) {
    lines.add(row);
  }

  public static Table parseLines(List<String> lines) {
    return new Table(lines.stream()
        .map(line -> line.replace("\"", ""))
        .map(line -> Row.split(line, SEPARATOR))
        .collect(Collectors.toList()));
  }

  private Table(List<Row> lines) {
    this.lines = lines;
  }

  public Table limit(int number) {
    return new Table(lines.stream().limit(number).collect(Collectors.toList()));
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
}
