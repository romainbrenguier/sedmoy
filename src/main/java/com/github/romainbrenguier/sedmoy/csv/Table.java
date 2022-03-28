package com.github.romainbrenguier.sedmoy.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Table {

  private final List<Row> lines;

  private final int width;

  private final int height;

  public Table(List<Row> lines) {
    this.lines = lines;
    height = lines.size();
    width = lines.stream().map(ArrayList::size).reduce(0, Math::max);
  }

  public List<Row> getLines() {
    return lines;
  }

  public int height() {
    return height;
  }

  public int width() {
    return width;
  }

  public Table limit(int number) {
    return new Table(lines.stream().limit(number).collect(Collectors.toList()));
  }

  public String cell(int columnIndex, int lineIndex) {
    final Row row = lines.get(lineIndex);
    if (row == null) {
      return "";
    }
    return row.column(columnIndex);
  }

  public Table downFrom(int columnIndex, int lineIndex) {
    return new Table(
        lines.stream().skip(lineIndex)
            .map(row -> new Row(row.subList(columnIndex, row.size())))
            .collect(Collectors.toList()));
  }

  public Table upFrom(int columnIndex, int lineIndex) {
    return new Table(
        lines.stream().limit(lineIndex + 1)
            .map(row -> new Row(row.subList(0, Math.min(row.size(), columnIndex + 1))))
            .collect(Collectors.toList()));
  }

  @Override
  public String toString() {
    return lines.stream()
        .map(row -> row.toString(TableParser.DEFAULT_SEPARATOR))
        .collect(Collectors.joining(System.lineSeparator()));
  }
}
