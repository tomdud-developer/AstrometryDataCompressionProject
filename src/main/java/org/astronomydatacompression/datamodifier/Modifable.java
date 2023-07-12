package org.astronomydatacompression.datamodifier;

import java.io.File;

public interface Modifable {
    File makeCopy(File file);
    void deleteCopy(File file);
}
