package org.astronomydatacompression.statistics;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class StatisticsCSVPrinter implements Printable {

    private final List<CompressionStatistics> compressionStatistics;
    private final List<DecompressionStatistics> decompressionStatistics;

    public StatisticsCSVPrinter(List<CompressionStatistics> compressionStatistics,
                                List<DecompressionStatistics> decompressionStatistics) {
        this.compressionStatistics = compressionStatistics;
        this.decompressionStatistics = decompressionStatistics;
    }
    

    @Override
    public File saveToPath(Path path) {

    }
}
