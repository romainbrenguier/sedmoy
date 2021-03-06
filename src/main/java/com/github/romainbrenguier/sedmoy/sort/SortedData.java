package com.github.romainbrenguier.sedmoy.sort;

import com.github.romainbrenguier.sedmoy.CsvData;
import com.github.romainbrenguier.sedmoy.Row;
import com.github.romainbrenguier.sedmoy.structure.BucketMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

  public static SortedData ofCsv(CsvData csv) {
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

  public CsvData toCsv() {
    CsvData result = new CsvData();
    for(int i = 0; i < this.maxIndex + 1; ++i) {
      Set<Row> bucket = this.data.get(i);
      int index = 0;
      for (Row row : bucket) {
        result.addLine(row.addLeft(i * 10 + index++).limit(10));
      }
    }
    return result;
  }

  public void printStats() {
    System.out.println("Number of entries: " + nbEntries());
    System.out.println("Number discarded: " + getDiscarded().size());
    int biggestBucket = data.biggestBucket();
    System.out.println("Biggest bucket index " + biggestBucket + " with size " +
        data.get(biggestBucket).size());
  }
}
