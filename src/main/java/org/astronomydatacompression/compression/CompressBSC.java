package org.astronomydatacompression.compression;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.logging.Level;

public class CompressBSC extends Compress {

    public CompressBSC(File file, Path workingDirectoryPath) {
        super(file, workingDirectoryPath, CompressMethod.BSC);
    }

    @Override
    public File compress() {
        String userDirectory = FileSystems.getDefault()
                .getPath("")
                .toAbsolutePath()
                .toString();
        String command =  "Compressors\\bsc\\bsc.exe";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command, "e", getFile().getPath(), getCompressedFileNameWithPath().toString());

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
                System.out.println("Thread method " + getMethod() + "was ended");
            } else {
                System.out.println("Thread method " + getMethod() + "was ended with error code" + exitCode);
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
