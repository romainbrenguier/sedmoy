package com.github.romainbrenguier.sedmoy.app;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Dimension;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.model.Row;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FormulaTableEvaluatorTest {

  /** Fake interpreter that returns the result of a hardcoded script */
  class MockInterpreter extends GroovyInterpreter {

    Map<String, Object> variables = new HashMap<>();

    @Override
    public void set(String variable, Object value) {
      variables.put(variable, value);
    }

    @Override
    public Object run(String groovyScript) {
      final Object i = variables.get("line");
      final Object j = variables.get("column");
      final DataTable foo = (DataTable) variables.get("foo");
      return foo.cell(0, 0) + ";" + i + ";" + j;
    }
  }

  @Test
  void testEvaluate() {
    GroovyInterpreter interpreter = new MockInterpreter();
    final Map<String, DataTable> environment = Collections.singletonMap("foo",
        new DataTable(Collections.singletonList(new Row(Collections.singletonList("bar")))));
    final FormulaTable formulaTable = new FormulaTable(new Dimension(2, 2));

    // Act
    final DataTable result = new FormulaTableEvaluator()
        .evaluate(interpreter, environment, formulaTable);

    // Assert
    assertThat(result, not(nullValue()));
  }
}