package com.github.romainbrenguier.sedmoy.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.romainbrenguier.sedmoy.app.TableToHtml;
import com.github.romainbrenguier.sedmoy.model.DataTable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataTableToHtmlTest {
    /**
     * Method under test: {@link TableToHtml#convertToStrings(DataTable)}
     */
    @Test
    public void testConvertToStrings() {
        // Arrange
        TableToHtml tableToHtml = new TableToHtml();
        List<List<String>> lines = Collections.singletonList(Arrays.asList("foo", "bar"));
        DataTable data = new DataTable(lines);

        // Act
        List<String> actualConvertToStringsResult = tableToHtml.convertToStrings(data);

        // Assert
        Assertions.assertEquals(3, actualConvertToStringsResult.size());
        Assertions.assertEquals("<html><head></head><body><ul>", actualConvertToStringsResult.get(0));
        Assertions.assertEquals("<li>foo:bar</li>", actualConvertToStringsResult.get(1));
        Assertions.assertEquals("</ul></body></html>", actualConvertToStringsResult.get(2));
    }
}

