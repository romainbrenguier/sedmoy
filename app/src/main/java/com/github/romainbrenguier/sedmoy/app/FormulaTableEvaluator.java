package com.github.romainbrenguier.sedmoy.app;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Dimension;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.codehaus.groovy.control.CompilationFailedException;

public class FormulaTableEvaluator {

  private final CachedFileReader cachedFileReader = new CachedFileReader();

  public DataTable evaluate(GroovyInterpreter interpreter,
      Map<String, DataTable> environment,
      FormulaTable table) throws GroovyException {
    interpreter.setFromMap(environment);
    if (table.getDimension().numberOfLines == 1
        && table.getDimension().numberOfColumns == 1) {
      try {
        return evaluateCollector(interpreter, table);
      } catch (GroovyException e) {
        return singletonTable(e.getCause());
      }
    }
    final DataTable result = new DataTable(table.getDimension());
    final String groovyScript = table.getGroovyScript();
    interpreter.setScript(groovyScript);
    interpreter.set("current", result);
    for (int line = 0; line < table.getDimension().numberOfLines; ++line) {
      interpreter.set("line", line);
      for (int column = 0; column < table.getDimension().numberOfColumns;
          ++column) {
        interpreter.set("column", column);
        try {
          final Object cellResult = interpreter.run();
          result.set(column, line, cellResult.toString());
        } catch (CompilationFailedException e) {
          throw new GroovyException(groovyScript, e);
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
  public DataTable evaluateCollector(GroovyInterpreter interpreter,
      FormulaTable table) throws GroovyException {
    Object result;
    interpreter.setScript(table.getGroovyScript());
    interpreter.set("cachedFileReader", cachedFileReader);
    result = interpreter.run();

    return convertObjectToTable(result);
  }

  private DataTable convertObjectToTable(Object result) {
    if (result instanceof List) {
      List<?> list = (List<?>) result;
      final List<DataTable> tables = list.stream()
          .map(this::convertObjectToTable).collect(Collectors.toList());
      return DataTable.mergeByFirstColumn(tables);
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

    if (result.getClass().isArray()) {
      final Object[] objects = Arrays
          .copyOf((Object[]) result.getClass().cast(result),
              Array.getLength(result));
      final List<Object> objectList = Arrays.stream(objects)
          .collect(Collectors.toList());
      return convertObjectToTable(objectList);
    }

    return singletonTable(result);
  }

  private DataTable singletonTable(Object result) {
    final List<Field> fields =
        Arrays.stream(result.getClass().getDeclaredFields()).filter(
            field -> !field.isSynthetic()
        ).collect(Collectors.toList());
    if (fields.isEmpty()) {
      final DataTable resultTable = new DataTable(new Dimension(1, 1));
      resultTable.set(0, 0, result.toString());
      return resultTable;
    }

    final DataTable resultTable =
        new DataTable(new Dimension(fields.size(), 2));
    for (int i = 0; i < fields.size(); ++i) {
      final Field field = fields.get(i);
      field.setAccessible(true);
      resultTable.set(0, i, field.getName());
      String valueString;
      try {
        valueString = Objects.toString(field.get(result), "null");
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        valueString = e.getMessage();
      }
      resultTable.set(1, i, valueString);
    }
    return resultTable;
  }
}
