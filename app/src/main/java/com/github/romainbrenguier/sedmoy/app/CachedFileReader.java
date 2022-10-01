package com.github.romainbrenguier.sedmoy.app;

import com.github.romainbrenguier.sedmoy.model.CsvParser;
import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Dimension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CachedFileReader {
  private final Map<String, DataTable> cache = new HashMap<>();
  private final CsvParser defaultCsvParser = new CsvParser(CsvParser.DEFAULT_SEPARATOR);

  public DataTable readCsv(String fileName) {
    return cache.computeIfAbsent(fileName, this::readCsvUncached);
  }

  private DataTable readCsvUncached(String fileName) {
    try {
      return defaultCsvParser
          .parseLines(Files.readAllLines(Path.of(fileName)));
    } catch (IOException exception) {
      exception.printStackTrace();
      final DataTable table = new DataTable(new Dimension(1, 1));
      table.set(0, 0, exception.getMessage());
      return table;
    }
  }

}
