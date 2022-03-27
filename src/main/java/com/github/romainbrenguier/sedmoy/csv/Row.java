package com.github.romainbrenguier.sedmoy.csv;

public class Row {
  String[] data;

  public Row(String[] data) {
    this.data = data;
  }

  public static Row split(String line, String separator) {
    return new Row(line.split(separator));
  }

  public Row addLeft(Object content) {
    String[] result = new String[data.length + 1];
    result[0] = content.toString();
    System.arraycopy(data, 0, result, 1, data.length);
    return new Row(result);
  }

  public String column(int index) {
    return index < data.length ? data[index] : "";
  }

  public Row limit(int nbColumns) {
    if (nbColumns >= data.length) {
      return this;
    }
    String[] result = new String[nbColumns];
    System.arraycopy(data, 0, result, 0, nbColumns);
    return new Row(result);
  }

  public String toString(String separator) {
    return String.join(separator, data);
  }
}
