package org.astronomydatacompression.statistics;


import lombok.Getter;
import org.astronomydatacompression.compression.CompressMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Getter
public class CompressionStatistics {
    private final CompressMethod compressMethod;
    private final File inputFile;
    private final double compressionTimeInSeconds;
    private final File compressedFile;

    private final double inputSizeInMB;
    private final double outputSizeInMB;

    public CompressionStatistics(
            CompressMethod compressMethod,
            File inputFile,
            long compressionTimeInNs,
            File compressedFile) {

        this.compressMethod = compressMethod;
        this.inputFile = inputFile;
        this.compressionTimeInSeconds = compressionTimeInNs / 1_000_000_000.0;
        this.compressedFile = compressedFile;

        try {
            inputSizeInMB = Files.size(inputFile.toPath()) / 1000_000.0;
            outputSizeInMB = Files.size(compressedFile.toPath()) / 1000_000.0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double getCompressionSpeedInMBPS() {
        return inputSizeInMB / compressionTimeInSeconds;
    }

    @Override
    public String toString() {
        return String.format(
                """
                ###  COMPRESSION STATISTICS for Method %s  ###
                Input File Size: %f MB
                Compression Time: %f seconds
                Output File Size: %f MB
                """,
                compressMethod, inputSizeInMB, compressionTimeInSeconds, outputSizeInMB);
    }





}
