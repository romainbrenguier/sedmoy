package com.github.romainbrenguier.sedmoy.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Dimension;
import com.github.romainbrenguier.sedmoy.model.Document;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class DocumentTest {

  @Test
  public void testToJson() throws IOException {
    final Document document = new Document();
    final DataTable dataTable = new DataTable(new Dimension(2, 1));
    dataTable.set(0,1, "foo");
    document.add("foo_table", dataTable);
    final FormulaTable formulaTable = new FormulaTable(new Dimension(1, 2));
    formulaTable.setGroovyScript("bar");
    document.add("bar_table", formulaTable);
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    // Serialize
    document.toJson(outputStream);
    String result = outputStream.toString();

    // Deserialize
    final ByteArrayInputStream inputStream = new ByteArrayInputStream(
        result.getBytes(StandardCharsets.UTF_8));
    Document deserialized = Document.ofJson(inputStream);

    // Assert
    MatcherAssert.assertThat(deserialized.tableNames, Matchers.equalTo(document.tableNames));
  }
}
