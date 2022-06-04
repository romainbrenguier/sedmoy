package com.github.romainbrenguier.sedmoy.app;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.model.Table;
import java.util.Map;

public class TableEvaluator {

  private final GroovyInterpreter groovyInterpreter;

  public TableEvaluator(GroovyInterpreter groovyInterpreter) {
    this.groovyInterpreter = groovyInterpreter;
  }

  public DataTable evaluate(Map<String, DataTable> environment, Table table) {
    if (table instanceof DataTable) {
      return (DataTable) table;
    }
    if (table instanceof FormulaTable) {
      new FormulaTableEvaluator().evaluate(
          groovyInterpreter, environment, (FormulaTable) table);
    }
    throw new IllegalArgumentException("Table of unknown type: " + table.getClass());
  }
}
