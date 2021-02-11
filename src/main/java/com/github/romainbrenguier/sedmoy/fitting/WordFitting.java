package com.github.romainbrenguier.sedmoy.fitting;

public interface WordFitting {

  Integer index();

  WordFitting next();

  @FunctionalInterface
  interface Provider {
    WordFitting get(String s);
  }
}
