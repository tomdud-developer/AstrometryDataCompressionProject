package org.astronomydatacompression.csv;

public class DR1Transformer extends Transformer {

    public final int astrometric_primary_flag_column = 34 - 1;
    public final int duplicated_source_column = 40 - 1;

    public DR1Transformer(CSV orgCSV) {
        super(53 - 1, "NOT_AVAILABLE", 4 - 1);
        this.orgCSV = orgCSV;
        this.transformedCSV = orgCSV.copy();
    }


    @Override
    public CSV transformBoolean() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();

        for (int i = 1; i < transformedCSV.getHeight(); i++) {
            if(arr[i][astrometric_primary_flag_column].equals("true"))
                arr[i][astrometric_primary_flag_column] = "1";
            else if(arr[i][astrometric_primary_flag_column].equals("false"))
                arr[i][astrometric_primary_flag_column] = "0";

            if(arr[i][duplicated_source_column].equals("true"))
                arr[i][duplicated_source_column] = "1";
            else if(arr[i][duplicated_source_column].equals("false"))
                arr[i][duplicated_source_column] = "0";
        }

        return transformedCSV;
    }

    @Override
    public CSV revertTransformBoolean() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();

        for (int i = 1; i < transformedCSV.getHeight(); i++) {
            if(arr[i][astrometric_primary_flag_column].equals("1"))
                arr[i][astrometric_primary_flag_column] = "true";
            else if(arr[i][astrometric_primary_flag_column].equals("0"))
                arr[i][astrometric_primary_flag_column] = "false";

            if(arr[i][duplicated_source_column].equals("1"))
                arr[i][duplicated_source_column] = "true";
            else if(arr[i][duplicated_source_column].equals("0"))
                arr[i][duplicated_source_column] = "false";
        }

        return transformedCSV;
    }




}
