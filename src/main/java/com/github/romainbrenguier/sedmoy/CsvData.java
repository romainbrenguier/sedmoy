package com.github.romainbrenguier.sedmoy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CsvData {

  public List<String[]> getLines() {
    return lines;
  }

  private final List<String[]> lines;

  private static String SEPARATOR = ",";

  public CsvData() {
    lines = new ArrayList<>();
  }

  public void addLine(String[] line) {
    lines.add(line);
  }

  public static CsvData parseLines(List<String> lines) {
    return new CsvData(lines.stream()
        .map(line -> line.replace("\"", ""))
        .map(line -> line.split(SEPARATOR))
        .collect(Collectors.toList()));
  }

  public CsvData(List<String[]> lines) {
    this.lines = lines;
  }

  public CsvData limit(int number) {
    return new CsvData(lines.stream().limit(number).collect(Collectors.toList()));
  }

  public List<String> toStrings(String separator) {
    return lines.stream()
        .map(line -> String.join(separator, line))
        .collect(Collectors.toList());
  }

  public String toString(String separator) {
    return String.join(System.lineSeparator(), toStrings(separator));
  }

  public List<String> toHtml() {
    List<String> output = new ArrayList<>();
    output.add("<html><head></head><body><ul>");
    for (String[] line : lines) {
      output.add(
          String.format("<li>%s</li>", String.join(" : ", line)));
    }
    output.add("</ul></body></html>");
    return output;
  }

  @Override
  public String toString() {
    return toString(SEPARATOR);
  }
}
