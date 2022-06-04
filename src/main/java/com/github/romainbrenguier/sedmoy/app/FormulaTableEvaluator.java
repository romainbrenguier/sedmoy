package com.github.romainbrenguier.sedmoy.app;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.model.Row;
import com.github.romainbrenguier.sedmoy.model.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FormulaTableEvaluator {

  public DataTable evaluate(GroovyInterpreter interpreter, Map<String, Table> environment,
      FormulaTable table) {
    interpreter.setFromMap(environment);
    final List<Row> rows = new ArrayList<>();
    for (int line = 0; line < table.getDimension().numberOfLines; ++line) {
      interpreter.set("line", line);
      final List<String> row = new ArrayList<>();
      for (int column = 0; column < table.getDimension().numberOfColumns; ++ column) {
        interpreter.set("column", column);
        final Object result = interpreter.run(table.getGroovyScript());
        row.add(result.toString());
      }
      rows.add(new Row(row));
    }
    return new DataTable(rows);
  }
}
