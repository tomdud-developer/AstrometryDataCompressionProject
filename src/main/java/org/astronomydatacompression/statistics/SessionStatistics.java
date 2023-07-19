package org.astronomydatacompression.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.astronomydatacompression.compression.CompressMethod;

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
    private final List<CompressMethod> compressMethodList;
    private final ModificationStatistics modificationStatistics;

    public SessionStatistics(String SESSION_ID,
                             File originalFile,
                             ModificationStatistics modificationStatistics,
                             List<CompressionStatistics> compressionStatistics,
                             List<DecompressionStatistics> decompressionStatistics,
                             List<CompressMethod> compressMethodList) {
        this.SESSION_ID = SESSION_ID;
        this.originalFile = originalFile;
        this.modificationStatistics = modificationStatistics;
        this.compressionStatistics = compressionStatistics;
        this.decompressionStatistics = decompressionStatistics;
        this.compressMethodList = compressMethodList;

        try {
            this.originalFileSizeInMB = Files.size(originalFile.toPath()) / 1000_000.0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
