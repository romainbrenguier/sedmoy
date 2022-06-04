package com.github.romainbrenguier.sedmoy.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.romainbrenguier.sedmoy.model.Row;
import com.github.romainbrenguier.sedmoy.model.DataTable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class DataTableToHtmlTest {
    /**
     * Method under test: {@link TableToHtml#convertToStrings(DataTable)}
     */
    @Test
    void testConvertToStrings() {
        // Arrange
        TableToHtml tableToHtml = new TableToHtml();
        List<Row> lines = Collections.singletonList(new Row(Arrays.asList("foo", "bar")));
        DataTable data = new DataTable(lines);

        // Act
        List<String> actualConvertToStringsResult = tableToHtml.convertToStrings(data);

        // Assert
        assertEquals(3, actualConvertToStringsResult.size());
        assertEquals("<html><head></head><body><ul>", actualConvertToStringsResult.get(0));
        assertEquals("<li>foo:bar</li>", actualConvertToStringsResult.get(1));
        assertEquals("</ul></body></html>", actualConvertToStringsResult.get(2));
    }
}

