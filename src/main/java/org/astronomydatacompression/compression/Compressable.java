package org.astronomydatacompression.compression;

import org.astronomydatacompression.statistics.CompressionStatistics;

import java.io.File;
import java.io.IOException;

public interface Compressable {
    CompressionStatistics compress() throws IOException;
    File deCompress();

    CompressMethod getMethod();
}
