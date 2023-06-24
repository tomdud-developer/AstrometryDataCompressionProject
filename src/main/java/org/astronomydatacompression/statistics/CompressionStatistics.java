package org.astronomydatacompression.statistics;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.util.Date;

@AllArgsConstructor
@Getter
@ToString
public class CompressionStatistics {
    private long compressionTimeInNs;
    //private long deCompressionTimeInNs;
    private double inputSizeInMB;
    //private double outputSizeInMB;
    private File compressedFile;

    public CompressionStatistics(long compressionTimeInNs, long inputSizeInMB) {
        this.compressionTimeInNs = compressionTimeInNs;
        this.inputSizeInMB = inputSizeInMB;
    }


    public double getCompressionSpeedInKBPS() {
        return inputSizeInMB / compressionTimeInNs * 1_000_000L;
    }

    @Override
    public String toString() {
        return String.format(
                """
                Compression Time: %d ns
                Input File Size: %f B
                """,
                compressionTimeInNs, inputSizeInMB);
    }





}
