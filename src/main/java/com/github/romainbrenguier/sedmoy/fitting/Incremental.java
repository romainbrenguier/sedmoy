package com.github.romainbrenguier.sedmoy.fitting;

import com.github.romainbrenguier.sedmoy.WordIndex;

public class Incremental implements WordFitting {
  int baseIndex;
  int max = 10;

  private Incremental(int baseIndex, int max) {
    this.baseIndex = baseIndex;
    this.max = max;
  }

  public Incremental(String s) {
    baseIndex = (WordIndex.of(s) / 10) + 10 ;
    // Todo: adapt according to first letter
    max = baseIndex + 20;
  }

  @Override
  public Integer index() {
    return baseIndex;
  }

  @Override
  public WordFitting next() {
    if (baseIndex == max) {
      return null;
    }
    return new Incremental(baseIndex + 1, max);
  }
}
