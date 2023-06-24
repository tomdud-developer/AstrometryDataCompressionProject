package org.astronomydatacompression.compression;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;
import org.astronomydatacompression.statistics.CompressionStatistics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class Compressor7Z extends Compressor {

    public Compressor7Z(File file, Path workingDirectoryPath) {
        super(
                file,
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
    public CompressionStatistics compress() throws IOException {
        Path copiedFilePath = copyFileToCompressToSessionWorkingDirectoryAndSetMethodName();

        String[] options = PropertiesLoader.INSTANCE.getListOfValuesDefinedInArray(
                PropertiesType.EXTERNAL, "compressors.7z.options").toArray(new String[0]);

        String[] commands = new String[] {
                getCompressorFile().getPath(),
                PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.7z.compressCommand"),
                copiedFilePath.toString()
        };

        long compressionTime = compressorRunner(commands);

        File compressedFile = new File(getCompressedFileNameWithPath().toUri());
        if(!compressedFile.exists()) throw new RuntimeException("There is no compressed file, method: " + getMethod());

        return new CompressionStatistics(
                getMethod(),
                getFile(),
                compressionTime,
                compressedFile
        );
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
