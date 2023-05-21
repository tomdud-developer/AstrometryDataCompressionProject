package org.astronomydatacompression.compression;

import java.io.File;

public interface Compressable {
    File compress(File file);
    File deCompress(File file);

    CompressMethod getMethod();
}
