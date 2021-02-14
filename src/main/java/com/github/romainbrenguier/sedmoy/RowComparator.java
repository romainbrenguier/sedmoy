package com.github.romainbrenguier.sedmoy;

import java.util.Comparator;

public class RowComparator implements Comparator<Row> {

  @Override
  public int compare(Row row1, Row row2) {
    String key1 = row1.column(0);
    String key2 = row2.column(0);
    int indexDiff = WordIndex.of(key1) - WordIndex.of(key2);
    if (indexDiff != 0) {
      return indexDiff;
    }
    indexDiff = key1.length() > 2 && key2.length() > 2
        ? WordIndex.of(key1.substring(2)) - WordIndex.of(key2.substring(2))
        : 0;
    if (indexDiff != 0) {
      return indexDiff;
    }
    return key1.compareTo(key2);
  }
}
