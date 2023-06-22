package org.astronomydatacompression.compression;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;


public class CompressorPPMD extends Compressor {

    public CompressorPPMD(File file, Path workingDirectoryPath) {
        super(
                file,
                workingDirectoryPath,
                Paths.get(
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.directory"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.ppmd.folderName"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.ppmd.executableFileName")
                ).toFile(),
                CompressMethod.PPMD);
    }

    @Override
    public File compress() {
        try {
            Files.copy(getFile().toPath(), getWorkingDirectoryPath().resolve(getFile().getName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] options = PropertiesLoader.INSTANCE.getListOfValuesDefinedInArray(
                PropertiesType.EXTERNAL, "compressors.ppmd.options").toArray(new String[0]);
        String[] commands = new String[3 + options.length + 1];
        commands[0] = getCompressorFile().getPath();
        commands[1] = PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.ppmd.compressCommand");
        commands[2] = "-f" + getCompressedFileName();

        System.arraycopy(options, 0, commands, 3, options.length);

        commands[3 + options.length] = getFile().getName();


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
