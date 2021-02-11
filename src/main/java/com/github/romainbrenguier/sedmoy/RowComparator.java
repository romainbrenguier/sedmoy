package com.github.romainbrenguier.sedmoy;

import java.util.Comparator;

public class RowComparator implements Comparator<Row> {

  @Override
  public int compare(Row row1, Row row2) {
    int indexDiff = WordIndex.of(row1.key) - WordIndex.of(row2.key);
    if (indexDiff != 0) {
      return indexDiff;
    }
    indexDiff = row1.key.length() > 2 && row2.key.length() > 2
        ? WordIndex.of(row1.key.substring(2)) - WordIndex.of(row2.key.substring(2))
        : 0;
    if (indexDiff != 0) {
      return indexDiff;
    }
    return row1.key.compareTo(row2.key);
  }
}
