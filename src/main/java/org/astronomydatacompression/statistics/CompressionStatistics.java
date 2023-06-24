package org.astronomydatacompression.statistics;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.astronomydatacompression.compression.CompressMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

@AllArgsConstructor
@Getter
public class CompressionStatistics {
    private final CompressMethod compressMethod;
    private final File inputFile;
    private final long compressionTimeInNs;
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
        this.compressionTimeInNs = compressionTimeInNs;
        this.compressedFile = compressedFile;

        try {
            inputSizeInMB = Files.size(inputFile.toPath()) / 1000.0;
            outputSizeInMB = Files.size(compressedFile.toPath()) / 1000.0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double getCompressionSpeedInKBPS() {
        return inputSizeInMB / compressionTimeInNs * 1_000_000L;
    }

    @Override
    public String toString() {
        return String.format(
                """
                Input File Size: %f B
                Compression Time: %d ns
                Output File Size: %f B
                """,
                inputSizeInMB, compressionTimeInNs, outputSizeInMB);
    }





}
