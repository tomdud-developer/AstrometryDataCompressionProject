package org.astronomydatacompression.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class CSVTest {

    @Test
    void loadCSVTest() throws FileNotFoundException {
        File file = new File(getClass().getClassLoader().getResource("test.csv").getPath());
        CSV csv = CSV.loadFromFile(file);

        Assertions.assertEquals(57, csv.getWidth());
        Assertions.assertEquals(65, csv.getHeight());
    }

}