package com.github.romainbrenguier.sedmoy;

import java.nio.file.Path;

public class MainConfig {
  Path path = null;

  Integer limit = null;

  String outputSeparator = ";";

  boolean writeMobi = false;

  public MainConfig withPath(Path path) {
    this.path = path;
    return this;
  }

  public MainConfig withLimit(Integer limit) {
    this.limit = limit;
    return this;
  }

  public MainConfig writeMobi() {
    this.writeMobi = true;
    return this;
  }
}

