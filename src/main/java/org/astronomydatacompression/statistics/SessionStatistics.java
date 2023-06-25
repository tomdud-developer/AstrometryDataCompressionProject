package org.astronomydatacompression.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@AllArgsConstructor
@Getter
public class SessionStatistics {
    private final String SESSION_ID;
    private File originalFile;
    private final List<CompressionStatistics> compressionStatistics;
    private final List<DecompressionStatistics> decompressionStatistics;
    private final double originalFileSizeInMB;

    public SessionStatistics(String SESSION_ID, File originalFile, List<CompressionStatistics> compressionStatistics, List<DecompressionStatistics> decompressionStatistics) {
        this.SESSION_ID = SESSION_ID;
        this.originalFile = originalFile;
        this.compressionStatistics = compressionStatistics;
        this.decompressionStatistics = decompressionStatistics;

        try {
            this.originalFileSizeInMB = Files.size(originalFile.toPath()) / 1000_000.0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
