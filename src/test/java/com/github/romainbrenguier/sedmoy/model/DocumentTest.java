package com.github.romainbrenguier.sedmoy.model;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class DocumentTest {

  @Test
  void toJson() throws IOException {
    final Document document = new Document();
    final DataTable dataTable = new DataTable(new Dimension(2, 1));
    dataTable.set(0,1, "foo");
    document.add("foo_table", dataTable);
    final FormulaTable formulaTable = new FormulaTable(new Dimension(1, 2));
    formulaTable.setGroovyScript("bar");
    document.add("bar_table", formulaTable);

    document.toJson(System.out);
    System.out.println("---");
  }
}