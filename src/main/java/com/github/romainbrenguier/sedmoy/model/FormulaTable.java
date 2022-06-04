package com.github.romainbrenguier.sedmoy.model;

/** Table whose data is obtained by the evaluation of a formula */
public class FormulaTable {
  private Dimension dimension;
  private String groovyScript = "";

  public FormulaTable(Dimension dimension) {
    this.dimension = dimension;
  }

  public String getGroovyScript() {
    return groovyScript;
  }

  public void setGroovyScript(String groovyScript) {
    this.groovyScript = groovyScript;
  }
}
