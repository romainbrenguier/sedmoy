package com.github.romainbrenguier.sedmoy;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CsvData {

  public List<Row> getLines() {
    return lines;
  }

  private final List<Row> lines;

  private static String SEPARATOR = ",";

  public CsvData() {
    lines = new ArrayList<>();
  }

  public void addLine(Row row) {
    lines.add(row);
  }

  public static CsvData parseLines(List<String> lines) {
    return new CsvData(lines.stream()
        .map(line -> line.replace("\"", ""))
        .map(line -> Row.split(line, SEPARATOR))
        .collect(Collectors.toList()));
  }

  public CsvData(List<Row> lines) {
    this.lines = lines;
  }

  public CsvData limit(int number) {
    return new CsvData(lines.stream().limit(number).collect(Collectors.toList()));
  }

  public List<String> toStrings(String separator) {
    return lines.stream()
        .map(row -> row.toString(separator))
        .collect(Collectors.toList());
  }

  public String toString(String separator) {
    return String.join(System.lineSeparator(), toStrings(separator));
  }

  public List<String> toHtml() {
    List<String> output = new ArrayList<>();
    output.add("<html><head></head><body><ul>");
    for (Row row : lines) {
      output.add(String.format("<li>%s</li>", row.toString(":")));
    }
    output.add("</ul></body></html>");
    return output;
  }

  @Override
  public String toString() {
    return toString(SEPARATOR);
  }

  public CsvData transform(RowTransform transform) {
    CsvData result = new CsvData();
    lines.forEach(result::addLine);
    return result;
  }

  public CsvData sort() {
    SortedData sortedData = SortedData.ofCsv(this);
    sortedData.printStats();
    return sortedData.toCsv();
  }
}
