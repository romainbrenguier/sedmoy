package com.github.romainbrenguier.sedmoy.app;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Dimension;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.control.CompilationFailedException;

public class FormulaTableEvaluator {

  public DataTable evaluate(GroovyInterpreter interpreter,
      Map<String, DataTable> environment,
      FormulaTable table) throws GroovyException {
    interpreter.setFromMap(environment);
    if (table.getDimension().numberOfLines == 1
        && table.getDimension().numberOfColumns == 1) {
      return evaluateCollector(interpreter, table);
    }
    final DataTable result = new DataTable(table.getDimension());
    interpreter.set("current", result);
    for (int line = 0; line < table.getDimension().numberOfLines; ++line) {
      interpreter.set("line", line);
      for (int column = 0; column < table.getDimension().numberOfColumns;
          ++column) {
        interpreter.set("column", column);
        try {
          final Object cellResult = interpreter.run(table.getGroovyScript());
          result.set(column, line, cellResult.toString());
        } catch (CompilationFailedException e) {
          throw new GroovyException(table.getGroovyScript(), e);
        } catch (Exception e) {
          result.set(column, line, e.toString());
        }
      }
    }
    return result;
  }

  /**
   * Special case of a Collector table, that is a formula that is evaluated only
   * once (size 1 by 1) and can return an object that is either a String, a
   * List, a Map or a DataTable.
   */
  private DataTable evaluateCollector(GroovyInterpreter interpreter,
      FormulaTable table) {
    Object result;
    try {
      result = interpreter.run(table.getGroovyScript());
    } catch (GroovyException e) {
      result = e.getCause();
    }

    if (result instanceof List) {
      List<?> list = (List<?>) result;
      final DataTable resultTable =
          new DataTable(new Dimension(list.size(), 1));
      for (int line = 0; line < list.size(); ++line) {
        resultTable.set(0, line, list.get(line).toString());
      }
      return resultTable;
    }

    if (result instanceof String) {
      String[] lines = ((String) result).split("\n");
      final DataTable resultTable =
          new DataTable(new Dimension(lines.length, 1));
      for (int line = 0; line < lines.length; ++line) {
        resultTable.set(0, line, lines[line]);
      }
      return resultTable;
    }

    if (result instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) result;
      final DataTable resultTable =
          new DataTable(new Dimension(map.size(), 2));
      int line = 0;
      for (Map.Entry<?, ?> entry : map.entrySet()) {
        resultTable.set(0, line, entry.getKey().toString());
        resultTable.set(1, line, entry.getValue().toString());
        line++;
      }
      return resultTable;
    }

    if (result instanceof DataTable) {
      return (DataTable) result;
    }

    final DataTable resultTable =
        new DataTable(new Dimension(1, 1));
    resultTable.set(0, 0, result.toString());
    return resultTable;
  }
}
