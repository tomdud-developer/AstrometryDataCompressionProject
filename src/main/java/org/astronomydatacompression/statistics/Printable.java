package org.astronomydatacompression.statistics;

import java.io.File;
import java.nio.file.Path;

public interface Printable {
    public File saveToPath(Path path);
}
