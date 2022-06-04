package com.github.romainbrenguier.sedmoy.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Table whose data is obtained by the evaluation of a formula */
public class FormulaTable implements Table {
  private Dimension dimension;
  private String groovyScript = "";

  @JsonCreator
  public FormulaTable(
      @JsonProperty("dimension") Dimension dimension,
      @JsonProperty("groovyScript") String groovyScript) {
    this.groovyScript = groovyScript;
    this.dimension = dimension;
  }

  public FormulaTable(Dimension dimension) {
    this.dimension = dimension;
  }

  public String getGroovyScript() {
    return groovyScript;
  }

  public void setGroovyScript(String groovyScript) {
    this.groovyScript = groovyScript;
  }

  public Dimension getDimension() {
    return dimension;
  }

  public FormulaTable withDimension(Dimension dimension) {
    this.dimension = dimension;
    return this;
  }
}
