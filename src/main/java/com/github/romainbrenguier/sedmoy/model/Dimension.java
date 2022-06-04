package com.github.romainbrenguier.sedmoy.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Dimension {

  public final int numberOfLines;
  public final int numberOfColumns;

  @JsonCreator
  public Dimension(
      @JsonProperty("numberOfLines") int numberOfLines,
      @JsonProperty("numberOfColumns") int numberOfColumns) {
    this.numberOfLines = Math.max(0, numberOfLines);
    this.numberOfColumns = Math.max(0, numberOfColumns);
  }
}
