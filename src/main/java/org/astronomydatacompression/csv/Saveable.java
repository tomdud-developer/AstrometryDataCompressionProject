package org.astronomydatacompression.csv;

import java.io.File;
import java.nio.file.Path;

public interface Saveable {
    File saveToFile(Path path);
}
