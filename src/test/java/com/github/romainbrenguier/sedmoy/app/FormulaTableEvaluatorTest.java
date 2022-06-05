package com.github.romainbrenguier.sedmoy.app;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Dimension;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class FormulaTableEvaluatorTest {

  @Test
  public void testEvaluate() {
    GroovyInterpreter interpreter = new MockInterpreter();
    final DataTable dataTable = new DataTable(new Dimension(2, 2));
    dataTable.set(0, 0, "a");
    dataTable.set(0, 1, "A");
    dataTable.set(1, 0, "b");
    final Map<String, DataTable> environment =
        Collections.singletonMap("foo", dataTable);
    final FormulaTable formulaTable = new FormulaTable(new Dimension(2, 2));

    // Act
    final DataTable result = new FormulaTableEvaluator()
        .evaluate(interpreter, environment, formulaTable);

    // Assert
    assertThat(result, not(nullValue()));
    assertEquals(2, result.width());
    assertEquals(2, result.height());
    // FIXME order of line and column differs which is confusing
    assertThat(result.cell(0, 0), equalTo("foo[0,0]=a"));
    assertThat(result.cell(0, 1), equalTo("foo[1,0]=b"));
    assertThat(result.cell(1, 0), equalTo("foo[0,1]=A"));
    assertThat(result.cell(1, 1), equalTo("foo[1,1]="));
  }

}