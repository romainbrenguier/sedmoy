package com.github.romainbrenguier.sedmoy.app;

import static org.hamcrest.MatcherAssert.assertThat;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Dimension;
import com.github.romainbrenguier.sedmoy.model.Document;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.model.Table;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
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
    MatcherAssert.assertThat(result, Matchers.not(Matchers.nullValue()));
    final Table foo = result.tables.get("foo");
    MatcherAssert.assertThat(foo, Matchers.instanceOf(DataTable.class));
    assertThat(((DataTable)foo).cellAsString(0, 0), Matchers.equalTo("a"));
    final Table bar = result.tables.get("bar");
    MatcherAssert.assertThat(bar, Matchers.instanceOf(DataTable.class));
    assertThat(((DataTable)bar).cellAsString(0, 0), Matchers.equalTo("foo[0,"
        + "0]=a"));
  }
}
