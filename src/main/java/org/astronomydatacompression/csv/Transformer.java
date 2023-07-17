package org.astronomydatacompression.csv;

public class Transformer implements DataTransformer {

    private CSV orgCSV;
    private CSV transformedCSV;

    public final int solution_id_column = 1 - 1;
    public final int astrometric_primary_flag_column = 34 - 1;
    public final int phot_variable_flag_column = 53 - 1;

    public Transformer(CSV orgCSV) {
        this.orgCSV = orgCSV;
        this.transformedCSV = orgCSV.copy();
    }

    @Override
    public CSV transformNotAvailable() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();

        for (int i = 1; i < transformedCSV.getHeight(); i++) {
            if(arr[i][phot_variable_flag_column].equals("NOT_AVAILABLE"))
                arr[i][phot_variable_flag_column] = "?";
        }

        return transformedCSV;
    }

    @Override
    public CSV revertTransformNotAvailable() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();

        for (int i = 1; i < transformedCSV.getHeight(); i++) {
            if(arr[i][phot_variable_flag_column].equals("?"))
                arr[i][phot_variable_flag_column] = "NOT_AVAILABLE";
        }

        return transformedCSV;
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
        }

        return transformedCSV;
    }

    private void checkVerticality(CSV csv) throws RuntimeException {
        if (!csv.isVertically()) throw new RuntimeException("CSV is not vertically");
    }
}
