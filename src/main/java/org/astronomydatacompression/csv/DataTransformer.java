package org.astronomydatacompression.csv;

public interface DataTransformer {
    CSV transformNotAvailable();
    CSV revertTransformNotAvailable();
    CSV transformBoolean();
    CSV revertTransformBoolean();
}
