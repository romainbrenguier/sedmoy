package com.github.romainbrenguier.sedmoy.sort;

import com.github.romainbrenguier.sedmoy.csv.Row;
import java.util.Comparator;

public class RowComparator implements Comparator<Row> {

  private int keyColumn = 0;

  public RowComparator() {
  }

  public RowComparator withKeyColumn(int keyColumn) {
    this.keyColumn = keyColumn;
    return this;
  }

  @Override
  public int compare(Row row1, Row row2) {
    String key1 = row1.column(keyColumn);
    String key2 = row2.column(keyColumn);
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
    for (int i = 0; i < key1.length(); ++i) {
      if (i >= key2.length()) {
        return 1;
      }
      int charDiff = WordIndex.forChar(key1.charAt(i)) - WordIndex.forChar(key2.charAt(i));
      if (charDiff != 0) {
        return charDiff;
      }
    }
    return key2.length() > key1.length() ? -1 : 0;
  }
}
