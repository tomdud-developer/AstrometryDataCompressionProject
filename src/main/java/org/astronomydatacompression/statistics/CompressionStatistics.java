package org.astronomydatacompression.statistics;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class CompressionStatistics {
    private long compressionTimeInNs;
    private long deCompressionTimeInNs;
    private double inputSizeInMB;
    private double outputSizeInMB;


    public double getCompressionRatio() {
        return inputSizeInMB / outputSizeInMB;
    }

    public double getCompressionSpeedInKBPS() {
        return inputSizeInMB / compressionTimeInNs * 1_000_000L;
    }



}
