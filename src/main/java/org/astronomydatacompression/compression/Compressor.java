package org.astronomydatacompression.compression;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public abstract class Compressor implements Compressable, Runnable {
    private final String defaultFileExtension;
    private final CompressMethod compressMethod;
    private final File file;
    private final Path workingDirectoryPath;
    private final File compressorFile;
    protected static final Logger logger = Logger.getLogger(Compressor.class.getName());

    public Compressor(File file, Path workingDirectoryPath, File compressorFile, CompressMethod compressMethod, String defaultFileExtension) {
        this.compressMethod = compressMethod;
        this.workingDirectoryPath = workingDirectoryPath;
        this.compressorFile = compressorFile;
        this.file = file;
        this.defaultFileExtension = defaultFileExtension;
    }

    public File getFile() {
        return file;
    }

    @Override
    public CompressMethod getMethod() {
        return compressMethod;
    }

    public Path getWorkingDirectoryPath() {
        return workingDirectoryPath;
    }
    public String getCompressedFileNameWithEndExtension() {
        return file.getName().split("\\.")[0] + "_compressed_" + getMethod().toString()
                + "." + file.getName().split("\\.")[1]
                + (defaultFileExtension.isEmpty() ? "" : "." + defaultFileExtension);
    }

    public String getCompressedFileNameWithoutEndExtension() {
        return file.getName().split("\\.")[0] + "_compressed_" + getMethod().toString()
                + "." + file.getName().split("\\.")[1];
    }

    public Path getCompressedFileNameWithPath() {
        return Paths.get(workingDirectoryPath.toString(), getCompressedFileNameWithEndExtension());
    }

    public File getCompressorFile() {
        return compressorFile;
    }

    protected long compressorRunner(String[] commands) {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            File logFile = Paths.get(getWorkingDirectoryPath().toString(), "logs", getMethod().toString() + "_logs").toFile();
            if(!logFile.createNewFile()) throw new RuntimeException("Error when create new log file");

            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            processBuilder.directory(getWorkingDirectoryPath().toFile());
            System.out.println(processBuilder.directory().getPath());

            long startTime = System.nanoTime();

            Process process = processBuilder.start();

            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            writer = new BufferedWriter(new FileWriter(logFile.getPath()));
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write("\n");
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                writer.write(line);
                writer.write("\n");
            }

            process.waitFor();

            long endTime = System.nanoTime() - startTime;

            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("Thread method " + getMethod() + " was ended");
            } else {
                System.out.println("Thread method " + getMethod() + " was ended with error code " + exitCode);
            }

            return endTime;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException();
    }

    public Path copyFileToCompressToSessionWorkingDirectoryAndSetMethodName() {
        Path copiedFilePath = null;
        try {
            copiedFilePath = Files.copy(getFile().toPath(), getWorkingDirectoryPath().resolve(getCompressedFileNameWithoutEndExtension()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return copiedFilePath;
    }

}
