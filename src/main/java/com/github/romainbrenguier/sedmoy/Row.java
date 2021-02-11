package com.github.romainbrenguier.sedmoy;

import java.util.ArrayList;
import java.util.List;

public class Row {
  Integer rank;
  String key;
  String[] data;
  Integer index = null;

  static Row ofCsvLine(String[] line, Integer rank) {
    assert line.length > 0;
    Row result = new Row();
    result.key = line[0];
    result.data = new String[line.length - 1];
    for (int i = 0; i < result.data.length; ++i) {
      result.data[i] = line[i+1];
    }
    result.rank = rank;
    return result;
  }

  public Row withIndex(Integer index) {
    this.index = index;
    return this;
  }

  static int lengthOfFirstWord(String s) {
    int firstSpace = s.indexOf(' ');
    return firstSpace == -1 ? s.length() : firstSpace;
  }

  int lengthOfFirstWord() {
    return lengthOfFirstWord(key);
  }

  String[] toCsvLine(Integer dataLimit) {
    List<String> list = new ArrayList<>();
    if (index != null) {
      list.add(index.toString());
    }
    list.add(key);
    for (int i = 0; i < data.length && i < dataLimit; ++i) {
      list.add(data[i]);
    }
    return list.toArray(new String[0]);
  }
}
