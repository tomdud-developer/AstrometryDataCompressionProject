package org.astronomydatacompression.compression;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class CompressBSC extends Compress {

    public CompressBSC(File file, Path workingDirectoryPath) {
        super(
                file,
                workingDirectoryPath,
                Paths.get(
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.directory"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.bsc.folderName"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.bsc.executableFileName")
                ).toFile(),
                CompressMethod.BSC);
    }

    @Override
    public File compress() {
        String[] commands = new String[] {
                getCompressorFile().getPath(),
                PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.bsc.compressCommand"),
                getFile().getPath(),
                getCompressedFileName()
        };

        compressorRunner(commands);

        return null;
    }

    @Override
    public File deCompress() {
        return null;
    }

    @Override
    public void run() {
        logger.log(Level.INFO, "Start compress method" + getMethod().toString() + "Thread.");
        compress();
    }
}
