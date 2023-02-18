package com.github.romainbrenguier.sedmoy.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.columns.strings.ByteDictionaryMap;
import tech.tablesaw.columns.strings.DictionaryMap;
import tech.tablesaw.columns.strings.StringColumnType;
import tech.tablesaw.columns.strings.StringParser;

class DatasawTableTest {
    /**
     * Method under test: {@link DatasawTable#mergeByFirstColumn(List)}
     */
    @Test
    void testMergeByFirstColumn() {
        // Arrange
        ArrayList<Table> tableList = new ArrayList<>();

        // Act and Assert
        List<Column<?>> columnsResult = DatasawTable.mergeByFirstColumn(tableList).columns();
        assertEquals(1, columnsResult.size());
        Column<?> getResult = columnsResult.get(0);
        assertTrue(getResult.type() instanceof StringColumnType);
        assertTrue(getResult.parser() instanceof StringParser);
        assertTrue(getResult.isEmpty());
        assertEquals("joining key", getResult.name());
        DictionaryMap dictionary = ((StringColumn) getResult).getDictionary();
        assertTrue(((ByteDictionaryMap) dictionary).getKeyValueEntries().isEmpty());
        assertTrue(((ByteDictionaryMap) dictionary).getKeyCountEntries().isEmpty());
        assertEquals(tableList, dictionary.getDummies());
        assertTrue(((ByteDictionaryMap) dictionary).values().isEmpty());
        assertEquals("", ((StringColumn) getResult).getPrintFormatter().getMissingString());
    }
}

