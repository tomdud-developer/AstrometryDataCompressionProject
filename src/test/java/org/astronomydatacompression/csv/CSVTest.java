package org.astronomydatacompression.csv;

import org.astronomydatacompression.compression.FilesIntegrityChecker;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

class CSVTest {

    @Test
    void loadCSVTest() throws FileNotFoundException {
        File file = new File(getClass().getClassLoader().getResource("test.csv").getPath());
        CSV csv = CSV.loadFromFile(file);

        Assertions.assertEquals(57, csv.getWidth());
        Assertions.assertEquals(65, csv.getHeight());
        Assertions.assertTrue(csv.isVertically());
    }

    @Test
    void saveCSVTest() throws IOException {
        File savedFile = null;
        try {
            File file = new File(getClass().getClassLoader().getResource("test.csv").getPath());
            CSV csv = CSV.loadFromFile(file);
            savedFile = csv.saveToFile();
            Assertions.assertTrue(savedFile.exists());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            assert savedFile != null;
            Files.deleteIfExists(savedFile.toPath());
        }
    }

    @Test
    void transposeTest() throws FileNotFoundException {
        File file = new File(getClass().getClassLoader().getResource("test.csv").getPath());
        CSV csv = CSV.loadFromFile(file);

        Assertions.assertEquals(57, csv.getWidth());
        Assertions.assertEquals(65, csv.getHeight());
        Assertions.assertTrue(csv.isVertically());


        CSV transposedCSV = csv.transpose();

        Assertions.assertEquals(57, transposedCSV.getHeight());
        Assertions.assertEquals(65, transposedCSV.getWidth());
        Assertions.assertFalse(transposedCSV.isVertically());
    }


}