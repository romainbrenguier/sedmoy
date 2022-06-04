package com.github.romainbrenguier.sedmoy.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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

  public void add(String tableName, Table table) {
    assert !tables.containsKey(tableName);
    tableNames.add(tableName);
    tables.put(tableName, table);
  }

  public void toJson(OutputStream out) throws IOException {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.writeValue(out, this);
  }
}
