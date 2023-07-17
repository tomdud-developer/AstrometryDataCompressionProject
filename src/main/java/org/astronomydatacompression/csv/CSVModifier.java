package org.astronomydatacompression.csv;

public enum CSVModifier {
    TRANSPOSE, TRANSFORM_BOOLEANS, TRANSFORM_NOT_AVAILABLE;

    public String getShortName() {
        if(this == TRANSFORM_BOOLEANS) return "TB";
        if(this == TRANSFORM_NOT_AVAILABLE) return "TNA";
        if(this == TRANSPOSE) return "TS";

        throw new IllegalStateException();
    }
}
