package com.github.romainbrenguier.sedmoy.app;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import java.util.Map;
import org.codehaus.groovy.control.CompilationFailedException;

public class FormulaTableEvaluator {

  public DataTable evaluate(GroovyInterpreter interpreter, Map<String, DataTable> environment,
      FormulaTable table) throws GroovyException {
    interpreter.setFromMap(environment);
    final DataTable result = new DataTable(table.getDimension());
    interpreter.set("current", result);
    for (int line = 0; line < table.getDimension().numberOfLines; ++line) {
      interpreter.set("line", line);
      for (int column = 0; column < table.getDimension().numberOfColumns; ++ column) {
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
}
