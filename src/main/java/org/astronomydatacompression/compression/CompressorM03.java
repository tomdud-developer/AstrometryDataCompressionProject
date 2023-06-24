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

public class CompressorM03 extends Compressor {

    public CompressorM03(File file, Path workingDirectoryPath) {
        super(
                file,
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
    public CompressionStatistics compress() throws IOException {
        String[] commands = new String[] {
                getCompressorFile().getPath(),
                PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.m03.compressCommand"),
                PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.m03.blockSize"),
                getFile().getPath(),
                getCompressedFileNameWithEndExtension()
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
