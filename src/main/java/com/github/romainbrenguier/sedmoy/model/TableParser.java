package com.github.romainbrenguier.sedmoy.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TableParser {
  public static final String DEFAULT_SEPARATOR = ",";

  private String separator;

  public TableParser(String separator) {
    this.separator = separator;
  }

  public TableParser() {
    this(DEFAULT_SEPARATOR);
  }

  public Table parseLines(List<String> lines) {
    return new Table(lines.stream()
        .map(line -> line.replace("\"", ""))
        .map(line -> new Row(splitRow(line, separator)))
        .collect(Collectors.toList()));
  }

  static List<String> splitRow(String line, String separator) {
    return Arrays.stream(line.split(separator)).collect(Collectors.toList());
  }

}
