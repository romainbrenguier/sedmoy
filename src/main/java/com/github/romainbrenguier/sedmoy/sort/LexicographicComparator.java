package com.github.romainbrenguier.sedmoy.sort;

import java.util.Comparator;

/**
 * Compare using a user define lexical ordering, ignoring double quotes and case.
 */
public class LexicographicComparator implements Comparator<String> {
  private final String order;

  public LexicographicComparator(String order) {
    this.order = order;
  }

  @Override
  public int compare(String s, String t) {
    s = s.replace("\"", "").toLowerCase();
    t = t.replace("\"", "").toLowerCase();
    for (int i = 0; i < s.length(); ++i) {
      if (i >= t.length()) {
        return -1;
      }
      int sIndex = order.indexOf(s.charAt(i));
      int tIndex = order.indexOf(t.charAt(i));
      if (sIndex < tIndex) {
        return -1;
      }
      if (tIndex < sIndex) {
        return 1;
      }
    }
    return 0;
  }
}
