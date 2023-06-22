package org.astronomydatacompression.compression;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class CompressorMCM extends Compressor {

    public CompressorMCM(File file, Path workingDirectoryPath) {
        super(
                file,
                workingDirectoryPath,
                Paths.get(
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.directory"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.mcm.folderName"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.mcm.executableFileName")
                ).toFile(),
                CompressMethod.MCM);
    }

    @Override
    public File compress() {

        String[] options = PropertiesLoader.INSTANCE.getListOfValuesDefinedInArray(
                PropertiesType.EXTERNAL, "compressors.mcm.options").toArray(new String[0]);

        String[] commands = new String[] {
                getCompressorFile().getPath(),
                options[0],
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
