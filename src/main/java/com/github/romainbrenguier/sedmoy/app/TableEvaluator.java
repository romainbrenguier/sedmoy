package com.github.romainbrenguier.sedmoy.app;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Document;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.model.Table;
import java.util.HashMap;
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
      try {
        return new FormulaTableEvaluator().evaluate(
            groovyInterpreter, environment, (FormulaTable) table);
      } catch (GroovyException e) {
        return new DataTable(((FormulaTable) table).getDimension());
      }
    }
    throw new IllegalArgumentException("Table of unknown type: " + table.getClass());
  }

  /** Evaluate all formulas to DataTable */
  public Document evaluate(Document document) {
    final Document result = new Document();
    final HashMap<String, DataTable> environment = new HashMap<>();
    for (String tableName : document.tableNames) {
      final Table table = document.tables.get(tableName);
      DataTable dataTable = evaluate(environment, table);
      environment.put(tableName, dataTable);
      result.add(tableName, dataTable);
    }
    return result;
  }

}
