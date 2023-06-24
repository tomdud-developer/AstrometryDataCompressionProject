package org.astronomydatacompression.compression;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;
import org.astronomydatacompression.statistics.CompressionStatistics;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class CompressorGZIP extends Compressor {

    public CompressorGZIP(File defaultFileToCompress, Path workingDirectoryPath) {
        this(workingDirectoryPath);
        setFileToCompress(defaultFileToCompress);
    }

    public CompressorGZIP(Path workingDirectoryPath) {
        super(
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
    public CompressionStatistics compress(File file) throws IOException {
        
        setFileToCompress(file);
        Path pathToCopiedFile = null;
        try {
            pathToCopiedFile = Files.copy(
                    getFileToCompress().toPath(),
                    getWorkingDirectoryPath().resolve(
                            getCompressedFileNameWithoutEndExtension()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] commands = new String[] {
                getCompressorFile().getPath(),
                pathToCopiedFile.toString()
        };

        long compressionTime = compressorRunner(commands, Operation.COMPRESSION);

        File compressedFile = new File(getCompressedFileNameWithPath().toUri());
        if(!compressedFile.exists()) throw new RuntimeException("There is no compressed file, method: " + getMethod());
        
        return new CompressionStatistics(
                getMethod(),
                getFileToCompress(),
                compressionTime,
                compressedFile
        );
    }

    @Override
    public File deCompress() {
        return null;
    }


}

