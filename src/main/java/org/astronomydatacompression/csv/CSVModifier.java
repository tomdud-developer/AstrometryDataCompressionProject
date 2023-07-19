package org.astronomydatacompression.csv;

public enum CSVModifier {
    TRANSPOSE,
    TRANSFORM_BOOLEANS,
    TRANSFORM_NOT_AVAILABLE,
    TRANSFORM_SOLUTION_ID,
    TRANSFORM_REF_EPOCHS,
    TRANSFORM_DR3_ALL;

    public String getShortName() {
        if(this == TRANSPOSE) return "TS";
        if(this == TRANSFORM_BOOLEANS) return "TB";
        if(this == TRANSFORM_NOT_AVAILABLE) return "TNA";
        if(this == TRANSFORM_SOLUTION_ID) return "TID";
        if(this == TRANSFORM_REF_EPOCHS) return "TRE";
        if(this == TRANSFORM_DR3_ALL) return "DR3_ALL";

        throw new IllegalStateException();
    }
}
