package org.astronomydatacompression.statistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class StatisticsCSVPrinter implements Printable {

    private final List<SessionStatistics> sessionStatistics;

    public StatisticsCSVPrinter(List<SessionStatistics> sessionStatistics) {
        this.sessionStatistics = sessionStatistics;
    }
    

    @Override
    public File saveToPath(Path path) {
        File file = deleteIfExistsAndCreateNewFile(path);
        writeStatisticsToFile(file);

        return file;
    }

    private void writeStatisticsToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("123");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected File deleteIfExistsAndCreateNewFile(Path path) {
        try {
            if (path.toFile().exists())
                Files.delete(path);
            return Files.createFile(path).toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
