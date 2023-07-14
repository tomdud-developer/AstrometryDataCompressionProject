package org.astronomydatacompression.statistics;

import org.astronomydatacompression.compression.CompressMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsCSVPrinterTest {

    StatisticsCSVPrinter statisticsCSVPrinter;
    File testFile;
    @BeforeEach
    void beforeAll() {
        testFile = new File(StatisticsCSVPrinterTest.class.getClassLoader().getResource("test.csv").getPath());
        List<CompressionStatistics> compressionStatistics1 = List.of(
                new CompressionStatistics(CompressMethod.M03, testFile, 1234567L, testFile),
                new CompressionStatistics(CompressMethod.MCM, testFile, 1234567L, testFile)
        );

        List<DecompressionStatistics> decompressionStatistics1 = List.of(
                new DecompressionStatistics(CompressMethod.M03, testFile, 1234567L, testFile),
                new DecompressionStatistics(CompressMethod.MCM, testFile, 1234567L, testFile)
        );

        List<CompressionStatistics> compressionStatistics2 = List.of(
                new CompressionStatistics(CompressMethod.SEVEN_Z, testFile, 1234567L, testFile),
                new CompressionStatistics(CompressMethod.GZIP, testFile, 1234567L, testFile)
        );

        List<DecompressionStatistics> decompressionStatistics2 = List.of(
                new DecompressionStatistics(CompressMethod.SEVEN_Z, testFile, 1234567L, testFile),
                new DecompressionStatistics(CompressMethod.GZIP, testFile, 1234567L, testFile)
        );

        List<SessionStatistics> sessionStatistics = List.of(
                new SessionStatistics(
                        "TEST_SESSION_ID_1",
                        testFile,
                        compressionStatistics1,
                        decompressionStatistics1,
                        List.of(CompressMethod.M03, CompressMethod.MCM)),
                new SessionStatistics(
                        "TEST_SESSION_ID_2",
                        testFile,
                        compressionStatistics2,
                        decompressionStatistics2,
                        List.of(CompressMethod.SEVEN_Z, CompressMethod.GZIP)
                )
        );

        statisticsCSVPrinter = new StatisticsCSVPrinter(sessionStatistics);
    }

    @Test
    void createNewFile() {
        String pathToResourceFolder = testFile.getParentFile().toString();
        Path pathToNewFile = Paths.get(pathToResourceFolder, "statisticsFile.csv");
        statisticsCSVPrinter.deleteIfExistsAndCreateNewFile(pathToNewFile);

        Assertions.assertTrue(pathToNewFile.toFile().exists());
    }

    @Test
    void saveToPath() {
        String pathToResourceFolder = testFile.getParentFile().toString();
        Path pathToNewFile = Paths.get(pathToResourceFolder, "statisticsFile.csv");
        File file = statisticsCSVPrinter.saveToPath(pathToNewFile);

        String fileContent;
        try {
            fileContent = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String fileLines[] = fileContent.split("\n");

        Assertions.assertEquals("### Session ID: TEST_SESSION_ID_1 ###", fileLines[0]);

        System.out.println(fileContent);

    }


}