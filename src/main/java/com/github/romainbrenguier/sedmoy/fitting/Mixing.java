package com.github.romainbrenguier.sedmoy.fitting;

import com.github.romainbrenguier.sedmoy.WordIndex;

public class Mixing implements WordFitting {
  String word;
  int start = 0;
  int skip = -1;

  private Mixing(String s, int start, int skip) {
    word = s;
    this.start = start;
    this.skip = skip;
  }

  public Mixing(String s) {
    word = s;
  }

  @Override
  public Integer index() {
    String suffix = word.substring(start);
    return WordIndex.of(suffix);
  }

  @Override
  public WordFitting next() {
    if (start < word.length() && word.charAt(start + 1) != ' ') {
      return new Mixing(word, start + 1, skip);
    }
    return null;
  }

}
