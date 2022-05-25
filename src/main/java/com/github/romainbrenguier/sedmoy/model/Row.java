package com.github.romainbrenguier.sedmoy.model;

import java.util.ArrayList;
import java.util.Collection;

public class Row extends ArrayList<String> {
  public Row(Collection<String> data) {
    super();
    addAll(data);
  }

  public String column(int index) {
    return index < size() ? get(index) : "";
  }

  public String toString(String separator) {
    return String.join(separator, this);
  }
}
