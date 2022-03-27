package com.github.romainbrenguier.sedmoy.sort;

import com.github.romainbrenguier.sedmoy.csv.Table;
import com.github.romainbrenguier.sedmoy.csv.Row;
import com.github.romainbrenguier.sedmoy.structure.BucketMap;
import java.util.ArrayList;
import java.util.List;

public class SortedData {
  private final BucketMap<Row> data = new BucketMap<>(new RowComparator());
  private int maxIndex = 0;
  private final List<Row> discarded = new ArrayList<>();

  public SortedData() {
  }

  public void add(Row row) {
    int i = WordIndex.of(row.column(0));
    this.maxIndex = Math.max(i, this.maxIndex);
    boolean added = data.put(i, row);
    if (!added) {
      discarded.add(row);
    }
  }

  public static SortedData ofCsv(Table csv) {
    SortedData sortedData = new SortedData();
    for (Row line : csv.getLines()) {
      sortedData.add(line);
    }
    return sortedData;
  }

  public int nbEntries() {
    return data.size();
  }

  public List<Row> getDiscarded() {
    return discarded;
  }

  /** For debugging only*/
  public void printStats() {
    System.out.println("Number of entries: " + nbEntries());
    System.out.println("Number discarded: " + getDiscarded().size());
    int biggestBucket = data.biggestBucket();
    System.out.println("Biggest bucket index " + biggestBucket + " with size " +
        data.get(biggestBucket).size());
  }
}
