package com.github.romainbrenguier.sedmoy.model;

import java.util.List;

public class Row {
  private Row () {
  }

  public static String column(List<String> arrayList, int index) {
    return index < arrayList.size() ? arrayList.get(index) : "";
  }

  public static String toString(List<String> arrayList, String separator) {
    return String.join(separator, arrayList);
  }

  public static List<String> subList(List<String> input, int start, int end) {
    return input.subList(start, end);
  }
}
