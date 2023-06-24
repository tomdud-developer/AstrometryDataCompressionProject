package org.astronomydatacompression.compression;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;
import org.astronomydatacompression.statistics.CompressionStatistics;

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
                CompressMethod.GZIP,
                PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.gzip.defaultExtension"));
    }

    @Override
    public CompressionStatistics compress() throws IOException {
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

        long compressionTime = compressorRunner(commands);

        return new CompressionStatistics(compressionTime, Files.size(getFile().toPath()));
    }

    @Override
    public File deCompress() {
        return null;
    }

    @Override
    public void run() {
        logger.log(Level.INFO, "Start compress method" + getMethod().toString() + "Thread.");
        try {
            System.out.println(compress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

