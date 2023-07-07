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
        File file = new File(getClass().getClassLoader().getResource("GaiaSource_000-000-036.csv").getPath());
        Assertions.assertNotNull(file);

        RowsColumnsChanger rowsColumnsChanger = new RowsColumnsChanger();
        rowsColumnsChanger.changeRowWithColumns(file);
    }
}