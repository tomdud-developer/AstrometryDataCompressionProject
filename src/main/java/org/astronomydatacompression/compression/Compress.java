package org.astronomydatacompression.compression;

import java.io.File;

public abstract class Compress implements Compressable, Runnable {
    private final CompressMethod compressMethod;
    private final File file;

    public Compress(File file, CompressMethod compressMethod) {
        this.compressMethod = compressMethod;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public CompressMethod getCompressMethod() {
        return compressMethod;
    }
}
