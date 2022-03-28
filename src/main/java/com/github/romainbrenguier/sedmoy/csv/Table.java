package com.github.romainbrenguier.sedmoy.csv;

import java.util.List;
import java.util.stream.Collectors;

public class Table {

  private final List<Row> lines;

  public List<Row> getLines() {
    return lines;
  }

  public Table(List<Row> lines) {
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
    return toString(TableParser.DEFAULT_SEPARATOR);
  }
}
