package com.github.romainbrenguier.sedmoy.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataTable implements Table {

  private final static Pattern INT_PATTERN = Pattern.compile("[0-9]+");
  private final static Pattern FLOAT_PATTERN = Pattern.compile("[0-9]*\\.[0-9]*");

  private final List<List<String>> lines;
  private final Dimension dimension;

  @JsonCreator
  public DataTable(
      @JsonProperty("lines") List<List<String>> lines,
      @JsonProperty("dimension") Dimension dimension) {
    assert dimension.numberOfLines >= lines.size();
    assert dimension.numberOfColumns >=
        lines.stream().map(List::size).reduce(0, Math::max);
    this.lines = lines;
    this.dimension = dimension;
  }

  public DataTable(List<List<String>> lines) {
    this(lines, new Dimension(
        lines.size(),
        lines.stream().map(List::size).reduce(0, Math::max)));
  }

  public DataTable(Dimension dimension) {
    this.dimension = dimension;
    this.lines = new ArrayList<>();
    for (int line = 0; line < dimension.numberOfLines; ++line) {
      lines.add(IntStream.range(0, dimension.numberOfColumns).mapToObj(i -> "")
          .collect(Collectors.toList()));
    }
  }

  public Dimension getDimension() {
    return dimension;
  }

  public List<List<String>> getLines() {
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

  public String cellAsString(int columnIndex, int lineIndex) {
    final List<String> row = lines.get(lineIndex);
    if (row == null) {
      return "";
    }
    return Row.column(row, columnIndex);
  }

  public Object cell(int columnIndex, int lineIndex) {
    final String string = cellAsString(columnIndex, lineIndex);
    if (INT_PATTERN.matcher(string).matches()) {
      return Integer.parseInt(string);
    }
    if (FLOAT_PATTERN.matcher(string).matches()) {
      return Float.parseFloat(string);
    }
    return string;
  }

  public List<String> row(int lineIndex) {
    return lines.get(lineIndex);
  }

  public List<String> column(int columnIndex) {
    return
        lines.stream()
            .map(row -> Row.column(row, columnIndex))
            .collect(Collectors.toList());
  }

  public DataTable downFrom(int columnIndex, int lineIndex) {
    return new DataTable(
        lines.stream().skip(lineIndex)
            .map(row -> Row.subList(row, columnIndex, row.size()))
            .collect(Collectors.toList()));
  }

  public DataTable upFrom(int columnIndex, int lineIndex) {
    return new DataTable(
        lines.stream().limit(lineIndex + 1)
            .map(row -> Row.subList(row, 0, Math.min(row.size(), columnIndex + 1)))
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
        .map(row -> Row.toString(row, CsvParser.DEFAULT_SEPARATOR))
        .collect(Collectors.joining(System.lineSeparator()));
  }
}
