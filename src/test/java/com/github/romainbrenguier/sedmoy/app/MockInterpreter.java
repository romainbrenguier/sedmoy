package com.github.romainbrenguier.sedmoy.app;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import java.util.HashMap;
import java.util.Map;

/**
 * Fake interpreter that returns the result of a hardcoded script
 */
class MockInterpreter extends GroovyInterpreter {

  Map<String, Object> variables = new HashMap<>();

  @Override
  public void set(String variable, Object value) {
    variables.put(variable, value);
  }

  public Object run(String groovyScript) {
    final int i = (int) variables.get("line");
    final int j = (int) variables.get("column");
    final DataTable foo = (DataTable) variables.get("foo");
    return "foo[" + i + "," + j + "]=" + foo.cellAsString(i, j);
  }
}
