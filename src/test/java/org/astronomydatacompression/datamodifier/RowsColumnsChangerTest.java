package org.astronomydatacompression.datamodifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class RowsColumnsChangerTest {

    @Test
    void changeRowWithColumns() {
        File file = new File(getClass().getClassLoader().getResource("temp_3819654134980442587.tmp").getPath());
        Assertions.assertNotNull(file);

        RowsColumnsChanger rowsColumnsChanger = new RowsColumnsChanger();
        rowsColumnsChanger.changeRowWithColumns(file);
    }

    @Test
    void getColumnFromLine() {
        RowsColumnsChanger rowsColumnsChanger = new RowsColumnsChanger();
        Assertions.assertEquals("456", rowsColumnsChanger.getColumnFromLine("123,456,789", 1));
        Assertions.assertEquals("789", rowsColumnsChanger.getColumnFromLine("123,456,789", 2));
    }
}