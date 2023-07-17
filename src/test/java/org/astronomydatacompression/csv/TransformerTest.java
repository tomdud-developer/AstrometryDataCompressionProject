package org.astronomydatacompression.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransformerTest {

    @Test
    void transformNotAvailable() throws FileNotFoundException {
        File file = new File(getClass().getClassLoader().getResource("test.csv").getPath());
        CSV csv = CSV.loadFromFile(file);

        Transformer transformer = new Transformer(csv);
        CSV modifiedCSV = transformer.transformNotAvailable();

        String[][] arr = modifiedCSV.getArray();
        Set<String> setOf_phot_variable_flag_column = new HashSet<>();

        for (int i = 1; i < modifiedCSV.getHeight(); i++) {
            setOf_phot_variable_flag_column.add(arr[i][transformer.phot_variable_flag_column]);
        }

        Assertions.assertEquals(1, setOf_phot_variable_flag_column.size());
        Assertions.assertTrue(setOf_phot_variable_flag_column.contains("?"));

        Assertions.assertFalse(modifiedCSV.equals(csv));
        modifiedCSV = transformer.revertTransformNotAvailable();
        Assertions.assertTrue(modifiedCSV.equals(csv));
    }

    @Test
    void transformBoolean() throws FileNotFoundException {
        File file = new File(getClass().getClassLoader().getResource("test.csv").getPath());
        CSV csv = CSV.loadFromFile(file);

        Transformer transformer = new Transformer(csv);
        CSV modifiedCSV = transformer.transformBoolean();

        String[][] arr = modifiedCSV.getArray();
        Set<String> setOf_primary_flag_column = new HashSet<>();

        for (int i = 1; i < modifiedCSV.getHeight(); i++) {
            setOf_primary_flag_column.add(arr[i][transformer.astrometric_primary_flag_column]);
        }

        Assertions.assertTrue(setOf_primary_flag_column.contains("0") || setOf_primary_flag_column.contains("1"));

        Assertions.assertFalse(modifiedCSV.equals(csv));
        modifiedCSV = transformer.revertTransformBoolean();
        Assertions.assertTrue(modifiedCSV.equals(csv));
    }
}