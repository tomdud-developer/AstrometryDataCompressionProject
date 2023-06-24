package org.astronomydatacompression.compression;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;
import org.astronomydatacompression.statistics.CompressionStatistics;
import org.astronomydatacompression.statistics.DecompressionStatistics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CompressorMCM extends Compressor {

    public CompressorMCM(File defaultFileToCompress, Path workingDirectoryPath) {
        this(workingDirectoryPath);
        setFileToCompress(defaultFileToCompress);
    }

    public CompressorMCM(Path workingDirectoryPath) {
        super(
                workingDirectoryPath,
                Paths.get(
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.directory"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.mcm.folderName"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.mcm.executableFileName")
                ).toFile(),
                CompressMethod.MCM,
                PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.m03.defaultExtension"));
    }

    @Override
    public CompressionStatistics compress(File file) throws IOException {

        setFileToCompress(file);

        String[] options = PropertiesLoader.INSTANCE.getListOfValuesDefinedInArray(
                PropertiesType.EXTERNAL, "compressors.mcm.options").toArray(new String[0]);

        String[] commands = new String[] {
                getCompressorFile().getPath(),
                options[0],
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
        return null;
    }


}
