package com.github.romainbrenguier.sedmoy.app;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Dimension;
import com.github.romainbrenguier.sedmoy.model.Document;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.model.Table;
import org.junit.jupiter.api.Test;

public class TableEvaluatorTest {

  @Test
  public void testEvaluate() {
    final GroovyInterpreter interpreter = new MockInterpreter();
    final Document document = new Document();

    final DataTable dataTable = new DataTable(new Dimension(1, 1));
    dataTable.set(0, 0, "a");
    document.add("foo", dataTable);

    final FormulaTable formulaTable = new FormulaTable(new Dimension(1, 2));
    document.add("bar", formulaTable);

    // Act
    final Document result = new TableEvaluator(interpreter).evaluate(document);

    // Assert
    assertThat(result, not(nullValue()));
    final Table foo = result.tables.get("foo");
    assertThat(foo, instanceOf(DataTable.class));
    assertThat(((DataTable)foo).cell(0, 0), equalTo("a"));
    final Table bar = result.tables.get("bar");
    assertThat(bar, instanceOf(DataTable.class));
    assertThat(((DataTable)bar).cell(0, 0), equalTo("foo[0,0]=a"));
  }
}