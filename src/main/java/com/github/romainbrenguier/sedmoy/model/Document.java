package com.github.romainbrenguier.sedmoy.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Table repository.
 * <p> Invariant: there is a one to one mapping of names in {@link #tableNames}
 * and keys in {@link #tables}.
 */
public class Document {

  List<String> tableNames = new ArrayList<>();
  HashMap<String, Table> tables = new HashMap<>();

  void add(String tableName, Table table) {
    assert !tables.containsKey(tableName);
    tableNames.add(tableName);
    tables.put(tableName, table);
  }
}
