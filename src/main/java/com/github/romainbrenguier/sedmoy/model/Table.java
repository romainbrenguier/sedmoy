package com.github.romainbrenguier.sedmoy.model;

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

  public String cell(int columnIndex, int lineIndex) {
    final Row row = lines.get(lineIndex);
    if (row == null) {
      return "";
    }
    return row.column(columnIndex);
  }

  public Row row(int lineIndex) {
    return lines.get(lineIndex);
  }

  public List<String> column(int columnIndex) {
    return
        lines.stream()
            .map(row -> row.column(columnIndex))
            .collect(Collectors.toList());
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

  public Table upFrom(int lineIndex) {
    return upFrom(width - 1, lineIndex);
  }

  public Table downFrom(int lineIndex) {
    return downFrom(0, lineIndex);
  }

  public Table leftFrom(int columnIndex) {
    return upFrom(columnIndex, height - 1);
  }

  public Table rightFrom(int columnIndex) {
    return downFrom(columnIndex, 0);
  }

  @Override
  public String toString() {
    return lines.stream()
        .map(row -> row.toString(CsvParser.DEFAULT_SEPARATOR))
        .collect(Collectors.joining(System.lineSeparator()));
  }
}
