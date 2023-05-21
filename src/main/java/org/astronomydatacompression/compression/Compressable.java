package org.astronomydatacompression.compression;

import java.io.File;

public interface Compressable {
    File compress();
    File deCompress();

    CompressMethod getMethod();
}
