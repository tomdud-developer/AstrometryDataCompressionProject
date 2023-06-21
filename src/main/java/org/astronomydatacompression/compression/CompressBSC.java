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

        try {


            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            processBuilder.directory(getWorkingDirectoryPath().toFile());

            Process process = processBuilder.start();

            File logFile = Paths.get(getWorkingDirectoryPath().toString(), "logs", getMethod().toString() + "_logs").toFile();
            if(!logFile.createNewFile()) throw new RuntimeException("Error when create new log file");

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile.getPath()));
            while ((line = reader.readLine()) != null) {
                writer.write(line);
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println("Błąd: " + line);
            }

            process.waitFor();

            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("Thread method " + getMethod() + " was ended");
            } else {
                System.out.println("Thread method " + getMethod() + " was ended with error code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

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
