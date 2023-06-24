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
    private final long decompressionTimeInNs;
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
        this.decompressionTimeInNs = decompressionTimeInNs;
        this.decompressedFile = decompressedFile;

        try {
            compressedFileInMB = Files.size(compressedFile.toPath()) / 1000.0;
            decompressedFileInMB = Files.size(decompressedFile.toPath()) / 1000.0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
