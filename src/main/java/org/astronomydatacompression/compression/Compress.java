package org.astronomydatacompression.compression;

import org.astronomydatacompression.session.Session;

import java.io.File;
import java.util.logging.Logger;

public abstract class Compress implements Compressable, Runnable {
    private final CompressMethod compressMethod;
    private final File file;

    protected static final Logger logger = Logger.getLogger(Compress.class.getName());

    public Compress(File file, CompressMethod compressMethod) {
        this.compressMethod = compressMethod;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public CompressMethod getMethod() {
        return compressMethod;
    }
}
