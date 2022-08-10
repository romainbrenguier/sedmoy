package com.github.romainbrenguier.sedmoy.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvParser {
  public static final String DEFAULT_SEPARATOR = ",";

  private String separator;

  public CsvParser(String separator) {
    this.separator = separator;
  }

  public CsvParser() {
    this(DEFAULT_SEPARATOR);
  }

  public DataTable parseLines(List<String> lines) {
    return new DataTable(lines.stream()
        .map(line -> line.replace("\"", ""))
        .map(line -> splitRow(line, separator))
        .collect(Collectors.toList()));
  }

  static List<String> splitRow(String line, String separator) {
    return Arrays.stream(line.split(separator)).collect(Collectors.toList());
  }

}
