package com.github.romainbrenguier.sedmoy.app;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.model.Row;
import com.github.romainbrenguier.sedmoy.model.Table;
import java.util.Collections;
import java.util.Map;

public class FormulaTableEvaluator {

  public DataTable evaluate(GroovyInterpreter interpreter, Map<String, Table> environment,
      FormulaTable table) {
    interpreter.setFromMap(environment);
    final Object result = interpreter.run(table.getGroovyScript());
    if (result instanceof DataTable) {
      return (DataTable) result;
    }
    return new DataTable(Collections.singletonList(new Row(
        Collections.singletonList("ERROR: script result of wrong type:" + result.getClass()))));
  }
}
