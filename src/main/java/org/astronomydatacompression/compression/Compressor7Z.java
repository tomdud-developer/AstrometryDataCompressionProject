package org.astronomydatacompression.compression;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;
import org.astronomydatacompression.statistics.CompressionStatistics;
import org.astronomydatacompression.statistics.DecompressionStatistics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Compressor7Z extends Compressor {

    public Compressor7Z(File defaultFileToCompress, Path workingDirectoryPath) {
        this(workingDirectoryPath);
        setFileToCompress(defaultFileToCompress);
    }

    public Compressor7Z(Path workingDirectoryPath) {
        super(
                workingDirectoryPath,
                Paths.get(
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.directory"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.7z.folderName"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.7z.executableFileName")
                ).toFile(),
                CompressMethod.SEVEN_Z,
                PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.7z.defaultExtension"));
    }

    @Override
    public CompressionStatistics compress(File file) throws IOException {
        
        setFileToCompress(file);

        Path copiedFilePath = copyFileToCompressToSessionWorkingDirectoryAndSetMethodName();

        String[] options = PropertiesLoader.INSTANCE.getListOfValuesDefinedInArray(
                PropertiesType.EXTERNAL, "compressors.7z.options").toArray(new String[0]);

        String[] commands = new String[] {
                getCompressorFile().getPath(),
                PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.7z.compressCommand"),
                copiedFilePath.toString()
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
