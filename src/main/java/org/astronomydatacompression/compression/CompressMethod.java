package org.astronomydatacompression.compression;

public enum CompressMethod {
    M03(CompressorM03.class),
    PPMD(CompressorPPMD.class),
    BSC(CompressorBSC.class),
    GZIP(CompressorGZIP.class);

    private final Class<? extends Compressor> compressClass;

    CompressMethod(Class<? extends Compressor> compressClass) {
        this.compressClass = compressClass;
    }

    public Class<? extends Compressor> getCompressClass() {
        return compressClass;
    }
}
