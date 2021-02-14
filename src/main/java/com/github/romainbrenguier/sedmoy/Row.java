package com.github.romainbrenguier.sedmoy;

public class Row {
  String[] data;

  public Row(String[] data) {
    this.data = data;
  }

  public Row addLeft(Object content) {
    String[] result = new String[data.length + 1];
    result[0] = content.toString();
    System.arraycopy(data, 0, result, 1, data.length);
    return new Row(result);
  }

  static int lengthOfFirstWord(String s) {
    int firstSpace = s.indexOf(' ');
    return firstSpace == -1 ? s.length() : firstSpace;
  }

  public String column(int index) {
    return index < data.length ? data[index] : "";
  }

  Row limit(int nbColumns) {
    String[] result = new String[nbColumns];
    System.arraycopy(data, 0, result, 0, nbColumns);
    return new Row(result);
  }

  String[] toCsvLine(Integer dataLimit) {
    return limit(dataLimit).data;
  }
}
