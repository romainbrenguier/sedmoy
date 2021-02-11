package com.github.romainbrenguier.sedmoy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {
  public static void main(String []args) {
    Path path = Paths.get(args[0]);
    MainConfig config = new MainConfig().withPath(path);
    run(config);
  }

  public static CsvData sort(MainConfig config) throws IOException {
    CsvData data = CsvData.parseLines(Files.readAllLines(config.path));
    if (config.limit != null) {
      data = data.limit(config.limit);
    }
    SortedData sortedData = SortedData.ofCsv(data);
    sortedData.printStats();
    return sortedData.toCsv();
  }

  public static File writeAsCsv(CsvData data, MainConfig config) throws IOException {
    File csvFile = File.createTempFile("sorted", ".csv");
    Files.write(csvFile.toPath(), data.toStrings(config.outputSeparator), StandardOpenOption.WRITE);
    return csvFile;
  }

  public static File writeAsHtml(CsvData data, MainConfig config) throws IOException {
    File tmpFile = File.createTempFile("sorted", ".html");
    Files.write(tmpFile.toPath(), data.toHtml(), StandardOpenOption.WRITE);
    return tmpFile;
  }

  public static File writeAsMobi(File htmlFile, MainConfig config) throws IOException {
    File tmpMobi = File.createTempFile("sorted", ".mobi");
    Process process =
        new ProcessBuilder("ebook-convert", htmlFile.toString(), tmpMobi.toString())
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

  public static void run(MainConfig config) {
    try {
      CsvData sorted = sort(config);
      System.out.println(sorted.toString());
      File asCsv = writeAsCsv(sorted, config);
      System.out.println("wrote " + asCsv.toString());
      File asHtml = writeAsHtml(sorted, config);
      System.out.println("wrote " + asHtml.toString());
      if (config.writeMobi) {
        File asMobi = writeAsMobi(asHtml, config);
        System.out.println("wrote " + asMobi.toString());
      }
    } catch (IOException e) {
      System.out.println("Error writing a file:");
      e.printStackTrace();
    }
  }
}
