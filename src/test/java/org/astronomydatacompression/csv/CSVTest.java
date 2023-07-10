package org.astronomydatacompression.csv;

import org.astronomydatacompression.compression.FilesIntegrityChecker;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class CSVTest {

    @Test
    void loadCSVTest() throws FileNotFoundException {
        File file = new File(getClass().getClassLoader().getResource("test.csv").getPath());
        CSV csv = CSV.loadFromFile(file);

        Assertions.assertEquals(57, csv.getWidth());
        Assertions.assertEquals(65, csv.getHeight());
    }

    @Test
    void saveCSVTest() throws IOException {
        File savedFile = null;
        try {
            File file = new File(getClass().getClassLoader().getResource("test.csv").getPath());
            CSV csv = CSV.loadFromFile(file);

            savedFile = csv.saveToFile();
            Assertions.assertTrue(savedFile.exists());
            Assertions.assertTrue(
                    FilesIntegrityChecker.compareByMemoryMappedFiles(savedFile.toPath(), file.toPath())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //Files.deleteIfExists(savedFile.toPath());
        }
    }


}