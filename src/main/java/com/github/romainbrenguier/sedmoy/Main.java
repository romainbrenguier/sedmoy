package com.github.romainbrenguier.sedmoy;

import com.github.romainbrenguier.sedmoy.sort.SortedData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

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

  private static CsvData sort(CsvData data) {
    SortedData sortedData = SortedData.ofCsv(data);
    sortedData.printStats();
    return sortedData.toCsv();
  }

  public static void run(MainConfig config) {
    try {
      if (config.isInteractive()) {
        List<String> data = Files.readAllLines(config.path);
        new InteractiveMode(data).run();
        return;
      }
      CsvData data = read(config);
      CsvData transformed =
          config.log() ? data.transform(new LogFile()) : sort(data);
      System.out.println(transformed.toString());
      File asCsv = writeAsCsv(transformed, config);
      System.out.println("wrote " + asCsv.toString());
      HtmlFile asHtml = HtmlFile.makeFromCsv(transformed);
      System.out.println("wrote " + asHtml.toString());
      if (config.writeMobi) {
        File asMobi = asHtml.convertToMobi();
        System.out.println("wrote " + asMobi.toString());
      }
    } catch (IOException e) {
      System.out.println("Error writing a file:");
      e.printStackTrace();
    }
  }
}
