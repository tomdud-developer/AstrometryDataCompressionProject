package org.astronomydatacompression.compression;

import org.astronomydatacompression.session.Session;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public abstract class Compress implements Compressable, Runnable {
    private final CompressMethod compressMethod;
    private final File file;
    private final Path workingDirectoryPath;
    private final File compressorFile;
    protected static final Logger logger = Logger.getLogger(Compress.class.getName());

    public Compress(File file, Path workingDirectoryPath, File compressorFile, CompressMethod compressMethod) {
        this.compressMethod = compressMethod;
        this.workingDirectoryPath = workingDirectoryPath;
        this.compressorFile = compressorFile;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public CompressMethod getMethod() {
        return compressMethod;
    }

    public Path getWorkingDirectoryPath() {
        return workingDirectoryPath;
    }
    public String getCompressedFileName() {
        return file.getName().split("\\.")[0] + "_compressed_" + getMethod().toString();
    }

    public Path getCompressedFileNameWithPath() {
        return Paths.get(workingDirectoryPath.toString(), getCompressedFileName());
    }

    public File getCompressorFile() {
        return compressorFile;
    }
}
