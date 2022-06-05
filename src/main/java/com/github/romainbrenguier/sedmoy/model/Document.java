package com.github.romainbrenguier.sedmoy.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Table repository.
 * <p> Invariant: there is a one to one mapping of names in {@link #tableNames}
 * and keys in {@link #tables}.
 */
public class Document {

  public List<String> tableNames = new ArrayList<>();
  public HashMap<String, Table> tables = new HashMap<>();

  public static Document ofJson(InputStream input) throws IOException {
    return new ObjectMapper().readValue(input, Document.class);
  }

  public void add(String tableName, Table table) {
    assert !tables.containsKey(tableName);
    tableNames.add(tableName);
    tables.put(tableName, table);
  }

  public void toJson(OutputStream out) throws IOException {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.writeValue(out, this);
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    for (String tableName : tableNames) {
      Table table = tables.get(tableName);
      builder.append("# ").append(tableName)
          .append("\n").append(table).append("\n");
    }
    return builder.toString();
  }
}
