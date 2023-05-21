package org.astronomydatacompression.compression;

public enum CompressMethod {
    M03(CompressM03.class),
    PPMD(CompressPPMD.class),
    BSC(CompressBSC.class);

    private final Class<? extends Compress> compressClass;

    CompressMethod(Class<? extends Compress> compressClass) {
        this.compressClass = compressClass;
    }

    public Class<? extends Compress> getCompressClass() {
        return compressClass;
    }
}
