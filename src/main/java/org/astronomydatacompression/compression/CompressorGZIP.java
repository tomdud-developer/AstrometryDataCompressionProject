package org.astronomydatacompression.compression;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;
import org.astronomydatacompression.statistics.CompressionStatistics;
import org.astronomydatacompression.statistics.DecompressionStatistics;

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
        Path pathToCopiedOriginalFileToWorkspace = copyFileToCompressToSessionWorkingDirectoryAndSetMethodName();


        String[] commands = new String[] {
                getCompressorFile().getPath(),
                pathToCopiedOriginalFileToWorkspace.toString()
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
    public DecompressionStatistics deCompress(File fileToDecompression) {
        String[] commands = new String[] {
                getCompressorFile().getPath(),
                "-dk",
                fileToDecompression.getPath(),
        };

        long decompressionTime = compressorRunner(commands, Operation.DECOMPRESSION);

        //It produces compressedFileWithoutCompressionExtension, it must be renamed
        File oldDeocmpressedFile = getCompressedFileNameWithoutEndExtensionPath().toFile();
        File newDecompressedFile = getDecompressedFileNameWithPath().toFile();
        if(!oldDeocmpressedFile.renameTo(newDecompressedFile))
            throw new RuntimeException("There is a problem with rename decompressed file, method: " + getMethod());


        File decompressedFile = new File(getDecompressedFileNameWithPath().toUri());
        if(!decompressedFile.exists()) throw new RuntimeException("There is no decompressed file, method: " + getMethod());

        return new DecompressionStatistics(
                getMethod(),
                fileToDecompression,
                decompressionTime,
                decompressedFile
        );
    }


}

