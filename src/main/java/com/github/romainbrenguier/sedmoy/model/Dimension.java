package com.github.romainbrenguier.sedmoy.model;

public class Dimension {
  public final int numberOfLines;
  public final int numberOfColumns;

  public Dimension(int numberOfLines, int numberOfColumns) {
    this.numberOfLines = Math.max(0, numberOfLines);
    this.numberOfColumns = Math.max(0, numberOfColumns);
  }
}
