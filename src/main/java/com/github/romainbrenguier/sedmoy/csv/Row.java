package com.github.romainbrenguier.sedmoy.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Row {
  List<String> data;

  @Deprecated
  public Row(String[] data) {
    this.data = Arrays.stream(data).collect(Collectors.toList());
  }

  public Row(List<String> data) {
    this.data = data;
  }


  public Row addLeft(Object content) {
    List<String> result = new ArrayList<>();
    result.add(content.toString());
    result.addAll(data);
    return new Row(result);
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
