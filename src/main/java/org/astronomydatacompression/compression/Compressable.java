package org.astronomydatacompression.compression;

import org.astronomydatacompression.statistics.CompressionStatistics;
import org.astronomydatacompression.statistics.DecompressionStatistics;

import java.io.File;
import java.io.IOException;

public interface Compressable {
    CompressionStatistics compress(File fileToCompress) throws IOException;
    File deCompress();

    CompressMethod getMethod();
}
