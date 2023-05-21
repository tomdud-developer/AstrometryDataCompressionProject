package org.astronomydatacompression.compression;

import java.io.File;
import java.util.logging.Level;

public class CompressM03 extends Compress {

    public CompressM03(File file) {
        super(file, CompressMethod.M03);
    }

    @Override
    public File compress(File file) {
        logger.log(Level.INFO, "Start compress method" + getMethod().toString() + "Thread.");
        return null;
    }

    @Override
    public File deCompress(File file) {
        return null;
    }

    @Override
    public void run() {
        compress(getFile());
    }
}
