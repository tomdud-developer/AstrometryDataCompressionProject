package org.astronomydatacompression.compression;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;


public class CompressPPMD extends Compress {

    public CompressPPMD(File file, Path workingDirectoryPath) {
        super(
                file,
                workingDirectoryPath,
                Paths.get(
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.directory"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.bsc.folderName"),
                        PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "compressors.bsc.executableFileName")
                ).toFile(),
                CompressMethod.PPMD);
    }


    @Override
    public File compress() {
        String userDirectory = FileSystems.getDefault()
                .getPath("")
                .toAbsolutePath()
                .toString();
        String command = userDirectory + "\\Compressors\\ppmdi2\\PPMd.exe";

        try {
            Files.copy(getFile().toPath(), Path.of(userDirectory + "\\" + getFile().getName()));
            ProcessBuilder processBuilder = new ProcessBuilder(command, "e", "-f" + getCompressedFileName(), getFile().getName());
            processBuilder.command().forEach(System.out::println);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                //System.out.println("Wyjście: " + line);
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
                System.out.println("Thread method " + getMethod() + " was ended with error code" + exitCode);
            }

            Files.delete(Path.of(userDirectory + "\\" + getFile().getName()));
            Files.move(Path.of(userDirectory + "\\" + getCompressedFileName()), Paths.get(getWorkingDirectoryPath().toString(), getCompressedFileName()));


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
