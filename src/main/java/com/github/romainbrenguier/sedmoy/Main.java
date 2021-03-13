package com.github.romainbrenguier.sedmoy;

import com.github.romainbrenguier.sedmoy.sort.SortedData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {
  public static void main(String[] args) {
    Path path = Paths.get(args[0]);
    MainConfig config = new MainConfig().withPath(path);
    run(config);
  }

  public static CsvData read(MainConfig config) throws IOException {
    CsvData data = CsvData.parseLines(Files.readAllLines(config.path));
    if (config.limit != null) {
      data = data.limit(config.limit);
    }
    return data;
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

  private static CsvData sort(CsvData data) {
    SortedData sortedData = SortedData.ofCsv(data);
    sortedData.printStats();
    return sortedData.toCsv();
  }

  public static void run(MainConfig config) {
    try {
      CsvData data = read(config);
      CsvData transformed =
          config.log() ? data.transform(new LogFile()) : sort(data);
      System.out.println(transformed.toString());
      File asCsv = writeAsCsv(transformed, config);
      System.out.println("wrote " + asCsv.toString());
      File asHtml = writeAsHtml(transformed, config);
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
