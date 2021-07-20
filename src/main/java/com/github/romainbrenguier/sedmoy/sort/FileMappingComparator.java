package com.github.romainbrenguier.sedmoy.sort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Use a file to map symbols to string which are used to define a comparator */
public class FileMappingComparator implements Comparator<String> {

  private final Map<Integer, String> mapping = new HashMap<>();

  public FileMappingComparator(Path path) throws IOException {
    List<String> lines = Files.readAllLines(path);
    for (String line : lines) {
      String[] splitted = line.split(":", 2);
      mapping.put(splitted[0].codePointAt(0), splitted[1]);
    }
  }

  @Override
  public int compare(String s, String t1) {
    if (s.length() == 0) {
      return t1.length() == 0 ? 0 : -1;
    } else if (t1.length() == 0) {
      return 1;
    }
    final int compareRest = compare(s.substring(1), t1.substring(1));
    final String firstInS = mapping.get(s.codePointAt(0));
    final String firstInT = mapping.get(t1.codePointAt(0));
    if (firstInS == null) {
      if (firstInT == null) {
        return compareRest;
      }
      return -1;
    }
    if (firstInT == null) {
      return 1;
    }
    final int compareFirsts = firstInS.compareTo(firstInT);
    if (compareFirsts == 0) {
      return compareRest;
    }
    return compareFirsts;
  }
}
