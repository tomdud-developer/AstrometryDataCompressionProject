package org.astronomydatacompression.compression;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;
import org.astronomydatacompression.statistics.CompressionStatistics;
import org.astronomydatacompression.statistics.DecompressionStatistics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class CompressorPPMD extends Compressor {

    public CompressorPPMD(File defaultFileToCompress, Path workingDirectoryPath) {
        this(workingDirectoryPath);
        setFileToCompress(defaultFileToCompress);
    }

    public CompressorPPMD(Path workingDirectoryPath) {
        super(
                workingDirectoryPath,
                Paths.get(
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.directory"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.ppmd.folderName"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.ppmd.executableFileName")
                ).toFile(),
                CompressMethod.PPMD,
                PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.ppmd.defaultExtension"));
    }

    @Override
    public CompressionStatistics compress(File file) throws IOException {
        
        setFileToCompress(file);

        try {
            Files.copy(getFileToCompress().toPath(), getWorkingDirectoryPath().resolve(getFileToCompress().getName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] options = PropertiesLoader.INSTANCE.getListOfValuesDefinedInArray(
                PropertiesType.EXTERNAL, "compressors.ppmd.options").toArray(new String[0]);
        String[] commands = new String[3 + options.length + 1];
        commands[0] = getCompressorFile().getPath();
        commands[1] = PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.ppmd.compressCommand");
        commands[2] = "-f" + getCompressedFileNameWithEndExtension();

        System.arraycopy(options, 0, commands, 3, options.length);

        commands[3 + options.length] = getFileToCompress().getName();


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
