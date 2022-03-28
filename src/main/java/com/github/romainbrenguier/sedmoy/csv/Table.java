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

  @Override
  public String toString() {
    return lines.stream()
        .map(row -> row.toString(TableParser.DEFAULT_SEPARATOR))
        .collect(Collectors.joining(System.lineSeparator()));
  }
}
