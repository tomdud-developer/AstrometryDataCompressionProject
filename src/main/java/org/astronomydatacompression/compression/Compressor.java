package org.astronomydatacompression.compression;

import org.astronomydatacompression.statistics.CompressionStatistics;
import org.astronomydatacompression.statistics.DecompressionStatistics;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Compressor implements Compressable, Runnable {
    private final String defaultFileExtension;
    private final CompressMethod compressMethod;
    protected File fileToCompress;
    private final Path workingDirectoryPath;
    private final File compressorFile;

    private CompressionStatistics compressionStatistics;
    private DecompressionStatistics decompressionStatistics;


    public Compressor(Path workingDirectoryPath, File compressorFile, CompressMethod compressMethod, String defaultFileExtension) {
        this.compressMethod = compressMethod;
        this.workingDirectoryPath = workingDirectoryPath;
        this.compressorFile = compressorFile;
        this.defaultFileExtension = defaultFileExtension;
    }

    @Override
    public void run() {
        try {
            System.out.println("Start compress method " + getMethod().toString());
            compressionStatistics = compress(fileToCompress);

            System.out.println("Start decompress method " + getMethod().toString());
            decompressionStatistics = deCompress(compressionStatistics.getCompressedFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFileToCompress(File fileToCompress) {
        this.fileToCompress = fileToCompress;
    }

    public enum Operation {COMPRESSION, DECOMPRESSION}

    public File getFileToCompress() {
        return fileToCompress;
    }

    @Override
    public CompressMethod getMethod() {
        return compressMethod;
    }

    public Path getWorkingDirectoryPath() {
        return workingDirectoryPath;
    }
    public String getCompressedFileNameWithEndExtension() {
        return fileToCompress.getName().split("\\.")[0] + "_compressed_" + getMethod().toString()
                + "." + fileToCompress.getName().split("\\.")[1]
                + (defaultFileExtension.isEmpty() ? "" : "." + defaultFileExtension);
    }

    public String getCompressedFileNameWithoutEndExtension() {
        return fileToCompress.getName().split("\\.")[0] + "_compressed_" + getMethod().toString()
                + "." + fileToCompress.getName().split("\\.")[1];
    }

    public String getDecompressedFileName() {
        return fileToCompress.getName().split("\\.")[0] + "_decompressed_" + getMethod().toString()
                + "." + fileToCompress.getName().split("\\.")[1];
    }

    public Path getCompressedFileNameWithoutEndExtensionPath() {
        return Paths.get(workingDirectoryPath.toString(), getCompressedFileNameWithoutEndExtension());
    }

    public Path getCompressedFileNameWithPath() {
        return Paths.get(workingDirectoryPath.toString(), getCompressedFileNameWithEndExtension());
    }

    public Path getDecompressedFileNameWithPath() {
        return Paths.get(workingDirectoryPath.toString(), getDecompressedFileName());
    }

    public File getCompressorFile() {
        return compressorFile;
    }

    protected long compressorRunner(String[] commands, Operation operation) {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            File logFile = Paths.get(getWorkingDirectoryPath().toString(), "logs_" + operation, getMethod().toString() + "_" + operation + "_logs").toFile();
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
            copiedFilePath = Files.copy(getFileToCompress().toPath(), getWorkingDirectoryPath().resolve(getCompressedFileNameWithoutEndExtension()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return copiedFilePath;
    }

    public CompressionStatistics getCompressionStatistics() {
        return compressionStatistics;
    }

    public DecompressionStatistics getDecompressionStatistics() {
        return decompressionStatistics;
    }



}
