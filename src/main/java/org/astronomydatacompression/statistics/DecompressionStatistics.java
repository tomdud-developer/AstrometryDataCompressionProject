package org.astronomydatacompression.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.astronomydatacompression.compression.CompressMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Getter
public class DecompressionStatistics {

    private final CompressMethod compressMethod;
    private final File compressedFile;
    private final double decompressionTimeInSeconds;
    private final File decompressedFile;
    private final double compressedFileInMB;
    private final double decompressedFileInMB;
    public DecompressionStatistics(
        CompressMethod compressMethod,
        File compressedFile,
        long decompressionTimeInNs,
        File decompressedFile
    ) {
        this.compressMethod = compressMethod;
        this.compressedFile = compressedFile;
        this.decompressionTimeInSeconds = decompressionTimeInNs / 1_000_000_000.0;
        this.decompressedFile = decompressedFile;

        try {
            compressedFileInMB = Files.size(compressedFile.toPath()) / 1000_000.0;
            decompressedFileInMB = Files.size(decompressedFile.toPath()) / 1000_000.0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.format(
                """
                ###  DECOMPRESSION STATISTICS for Method %s  ###
                File to decompress size: %f MB
                Decompression Time: %f seconds
                Decompressed File Size: %f MB
                """,
                compressMethod, compressedFileInMB, decompressionTimeInSeconds, decompressedFileInMB);
    }




}
