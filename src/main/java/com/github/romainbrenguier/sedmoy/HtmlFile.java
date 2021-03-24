package com.github.romainbrenguier.sedmoy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HtmlFile {
  private final File file;

  private HtmlFile(File file) {
    this.file = file;
  }

  public static HtmlFile makeFromCsv(CsvData data) throws IOException {
    List<String> lines = data.getLines().stream()
        .map(row -> row.toString(":"))
        .collect(Collectors.toList());
    return makeFromLines(lines);
  }

  public static HtmlFile makeFromLines(List<String> lines) throws IOException {
    List<String> output = new ArrayList<>();
    output.add("<html><head></head><body><ul>");
    for (String line : lines) {
      output.add(String.format("<li>%s</li>", line));
    }
    output.add("</ul></body></html>");
    File tmpFile = File.createTempFile("sorted", ".html");
    Files.write(tmpFile.toPath(), output, StandardOpenOption.WRITE);
    return new HtmlFile(tmpFile);
  }

  public File convertToMobi() throws IOException {
    File tmpMobi = File.createTempFile("sorted", ".mobi");
    Process process =
        new ProcessBuilder("ebook-convert", file.toString(), tmpMobi.toString())
            .start();
    try {
      int result = process.waitFor();
      if (result != 0) {
        System.out.println("Process exited with unexpected code " + result);
      }
    } catch (InterruptedException e) {
      System.out.println("Process interrupted");
    }
    return tmpMobi;
  }
}
