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

public class CompressorZSTD extends Compressor {

    public CompressorZSTD(File file, Path workingDirectoryPath) {
        super(
                file,
                workingDirectoryPath,
                Paths.get(
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.directory"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.zstd.folderName"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.zstd.executableFileName")
                ).toFile(),
                CompressMethod.ZSTD,
                PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.zstd.defaultExtension"));
    }

    @Override
    public CompressionStatistics compress() throws IOException {

        String[] options = PropertiesLoader.INSTANCE.getListOfValuesDefinedInArray(
                PropertiesType.EXTERNAL, "compressors.zstd.options").toArray(new String[0]);

        String[] commands = new String[] {
                getCompressorFile().getPath(),
                options[0],
                getFile().getPath(),
                options[1],
                getCompressedFileNameWithEndExtension()
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
