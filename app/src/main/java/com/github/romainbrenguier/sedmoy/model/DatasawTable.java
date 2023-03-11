package com.github.romainbrenguier.sedmoy.model;

import tech.tablesaw.api.Row;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DatasawTable {

    public static Table mergeByFirstColumn(List<Table> tables) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (Table table : tables) {
            for (Row line : table.rows()) {
                if (line.columnCount() > 0) {
                    final String key = line.getString(0);
                    final List<String> value = map
                            .computeIfAbsent(key, k -> new ArrayList<>());
                    for (int i = 1; i < line.columnCount(); i++)
                        value.add(line.getString(i));
                }
            }
        }
        final Table result = Table.create();
        final StringColumn keyColumn = StringColumn.create("joining key");
        map.keySet().forEach(keyColumn::append);
        result.addColumns(keyColumn);

        for (int i = 0; i < tables.size(); ++i) {
            final StringColumn column = StringColumn.create("table " + i);
            final int i1 = i;
            map.keySet().stream().map(key -> map.get(key).get(i1))
                    .filter(Objects::nonNull)
                    .forEach(column::append);
            result.addColumns(column);
        }
        return result;
    }

}
