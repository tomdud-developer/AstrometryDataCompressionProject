package org.astronomydatacompression.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.util.List;

@AllArgsConstructor
@Getter
public class SessionStatistics {
    private final String SESSION_ID;
    private File originalFile;
    private final List<CompressionStatistics> compressionStatistics;
    private final List<DecompressionStatistics> decompressionStatistics;
}
