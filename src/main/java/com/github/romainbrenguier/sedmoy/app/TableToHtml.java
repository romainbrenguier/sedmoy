package com.github.romainbrenguier.sedmoy.app;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableToHtml {
  private static final int MAX_ITEMS_IN_LIST = 40;

  public List<String> convertToStrings(DataTable data) {
    List<String> lines = data.getLines().stream()
            .map(row -> row.toString(":"))
            .collect(Collectors.toList());
    List<String> output = new ArrayList<>();
    output.add("<html><head></head><body><ul>");
    int index = 0;
    for (String line : lines) {
      output.add(String.format("<li>%s</li>", line));
      if (++index % MAX_ITEMS_IN_LIST == 0) {
        output.add("</ul>");
        output.add("<ul>");
      }
    }
    output.add("</ul></body></html>");
    return output;
  }

  public File writeToTmpFile(DataTable data) throws IOException {
    List<String> output = convertToStrings(data);
    File tmpFile = File.createTempFile("sorted", ".html");
    Files.write(tmpFile.toPath(), output, StandardOpenOption.WRITE);
    return tmpFile;
  }
}
