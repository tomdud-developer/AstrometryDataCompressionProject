package org.astronomydatacompression.compression;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;


public class CompressorGZIP extends Compressor {

    public CompressorGZIP(File file, Path workingDirectoryPath) {
        super(
                file,
                workingDirectoryPath,
                Paths.get(
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.directory"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.gzip.folderName"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.gzip.executableFileName")
                ).toFile(),
                CompressMethod.GZIP);
    }

    @Override
    public File compress() {
        Path pathToCopiedFile = null;
        try {
            pathToCopiedFile = Files.copy(
                    getFile().toPath(),
                    getWorkingDirectoryPath().resolve(
                            addStrBeforeDotInFileName(getFile(), "_gzip"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] commands = new String[] {
                getCompressorFile().getPath(),
                pathToCopiedFile.toString()
        };

        compressorRunner(commands);

        return null;
    }

    @Override
    public File deCompress() {
        return null;
    }

    @Override
    public void run() {
        logger.log(Level.INFO, "Start compress method" + getMethod().toString() + "Thread.");
        compress();
    }
}

