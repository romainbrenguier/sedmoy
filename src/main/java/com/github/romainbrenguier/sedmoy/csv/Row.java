package com.github.romainbrenguier.sedmoy.csv;

import java.util.List;
import java.util.stream.Collectors;

public class Row {
  List<String> data;

  public Row(List<String> data) {
    this.data = data;
  }

  public String column(int index) {
    return index < data.size() ? data.get(index) : "";
  }

  public Row limit(int nbColumns) {
    if (nbColumns >= data.size()) {
      return this;
    }
    return new Row(data.stream().limit(nbColumns).collect(Collectors.toList()));
  }

  public String toString(String separator) {
    return String.join(separator, data);
  }
}
