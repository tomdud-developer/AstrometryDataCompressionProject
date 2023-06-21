package org.astronomydatacompression.compression;

import org.astronomydatacompression.session.Session;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public abstract class Compress implements Compressable, Runnable {
    private final CompressMethod compressMethod;
    private final File file;
    private final Path workingDirectoryPath;
    private final File compressorFile;
    protected static final Logger logger = Logger.getLogger(Compress.class.getName());

    public Compress(File file, Path workingDirectoryPath, File compressorFile, CompressMethod compressMethod) {
        this.compressMethod = compressMethod;
        this.workingDirectoryPath = workingDirectoryPath;
        this.compressorFile = compressorFile;
        this.file = file;
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
    public String getCompressedFileName() {
        return file.getName().split("\\.")[0] + "_compressed_" + getMethod().toString();
    }

    public Path getCompressedFileNameWithPath() {
        return Paths.get(workingDirectoryPath.toString(), getCompressedFileName());
    }

    public File getCompressorFile() {
        return compressorFile;
    }

    protected void compressorRunner(String[] commands) {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            File logFile = Paths.get(getWorkingDirectoryPath().toString(), "logs", getMethod().toString() + "_logs").toFile();
            if(!logFile.createNewFile()) throw new RuntimeException("Error when create new log file");

            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            processBuilder.directory(getWorkingDirectoryPath().toFile());

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

            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("Thread method " + getMethod() + " was ended");
            } else {
                System.out.println("Thread method " + getMethod() + " was ended with error code " + exitCode);
            }

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
    }

}
