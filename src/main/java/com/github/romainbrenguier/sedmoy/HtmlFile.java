package com.github.romainbrenguier.sedmoy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class HtmlFile {
  private final File file;

  private HtmlFile(File file) {
    this.file = file;
  }

  public static HtmlFile makeFromCsv(CsvData data) throws IOException {
    File tmpFile = File.createTempFile("sorted", ".html");
    Files.write(tmpFile.toPath(), data.toHtml(), StandardOpenOption.WRITE);
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
