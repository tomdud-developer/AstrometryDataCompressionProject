package org.astronomydatacompression.compression;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;
import org.astronomydatacompression.statistics.CompressionStatistics;
import org.astronomydatacompression.statistics.DecompressionStatistics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CompressorM03 extends Compressor {

    public CompressorM03(File defaultFileToCompress, Path workingDirectoryPath) {
        this(workingDirectoryPath);
        setFileToCompress(defaultFileToCompress);
    }
    public CompressorM03(Path workingDirectoryPath) {
        super(
                workingDirectoryPath,
                Paths.get(
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.directory"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.m03.folderName"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.m03.executableFileName")
                ).toFile(),
                CompressMethod.M03,
                PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.m03.defaultExtension"));
    }

    @Override
    public CompressionStatistics compress(File file) throws IOException {
        
        setFileToCompress(file);
        
        String[] commands = new String[] {
                getCompressorFile().getPath(),
                PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.m03.compressCommand"),
                PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.m03.blockSize"),
                getFileToCompress().getPath(),
                getCompressedFileNameWithEndExtension()
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
                "d",
                fileToDecompression.getPath(),
                getDecompressedFileName()
        };

        long decompressionTime = compressorRunner(commands, Operation.DECOMPRESSION);

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
