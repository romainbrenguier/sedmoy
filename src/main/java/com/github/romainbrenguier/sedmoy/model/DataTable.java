package com.github.romainbrenguier.sedmoy.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataTable implements Table {

  private final List<Row> lines;

  private final Dimension dimension;

  public DataTable(List<Row> lines) {
    this.lines = lines;
    dimension = new Dimension(
        lines.size(),
        lines.stream().map(ArrayList::size).reduce(0, Math::max));
  }

  public DataTable(Dimension dimension) {
    this.dimension = dimension;
    this.lines = new ArrayList<>();
    for (int line = 0; line < dimension.numberOfLines; ++line) {
      lines.add(new Row(IntStream.range(0, dimension.numberOfColumns).mapToObj(i -> "")
          .collect(Collectors.toList())));
    }
  }

  public List<Row> getLines() {
    return lines;
  }

  public int height() {
    return dimension.numberOfLines;
  }

  public int width() {
    return dimension.numberOfColumns;
  }

  public void set(int columnIndex, int lineIndex, String content) {
    lines.get(lineIndex).set(columnIndex, content);
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

  public DataTable downFrom(int columnIndex, int lineIndex) {
    return new DataTable(
        lines.stream().skip(lineIndex)
            .map(row -> new Row(row.subList(columnIndex, row.size())))
            .collect(Collectors.toList()));
  }

  public DataTable upFrom(int columnIndex, int lineIndex) {
    return new DataTable(
        lines.stream().limit(lineIndex + 1)
            .map(row -> new Row(row.subList(0, Math.min(row.size(), columnIndex + 1))))
            .collect(Collectors.toList()));
  }

  public DataTable upFrom(int lineIndex) {
    return upFrom(width() - 1, lineIndex);
  }

  public DataTable downFrom(int lineIndex) {
    return downFrom(0, lineIndex);
  }

  public DataTable leftFrom(int columnIndex) {
    return upFrom(columnIndex, height() - 1);
  }

  public DataTable rightFrom(int columnIndex) {
    return downFrom(columnIndex, 0);
  }

  @Override
  public String toString() {
    return lines.stream()
        .map(row -> row.toString(CsvParser.DEFAULT_SEPARATOR))
        .collect(Collectors.joining(System.lineSeparator()));
  }
}
