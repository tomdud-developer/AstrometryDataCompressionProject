package org.astronomydatacompression.csv;

public interface DataTransformer {
    CSV transformID();
    CSV revertTransformID();
    CSV transformNotAvailable();
    CSV revertTransformNotAvailable();
    CSV transformBoolean();
    CSV revertTransformBoolean();
    CSV transformRefEpochs();
    CSV revertTransformRefEpochs();
}
