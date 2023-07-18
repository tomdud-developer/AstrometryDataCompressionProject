package org.astronomydatacompression.csv;

public class Transformer implements DataTransformer {

    private CSV orgCSV;
    private CSV transformedCSV;

    private String solutionID = null;

    public final int solution_id_column = 1 - 1;
    public final int astrometric_primary_flag_column = 34 - 1;
    public final int phot_variable_flag_column = 53 - 1;
    public final int duplicated_source = 40 - 1;

    public Transformer(CSV orgCSV) {
        this.orgCSV = orgCSV;
        this.transformedCSV = orgCSV.copy();
    }

    public void setModifiedCSV(CSV csv) {
        this.transformedCSV = csv;
    }

    @Override
    public CSV transformID() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();
        solutionID = arr[1][0];

        for (int i = 1; i < transformedCSV.getHeight(); i++) {
            arr[i][solution_id_column] = "";
        }

        return transformedCSV;
    }

    @Override
    public CSV revertTransformID() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();

        for (int i = 1; i < transformedCSV.getHeight(); i++) {
            arr[i][solution_id_column] = solutionID;
        }

        return transformedCSV;
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

            if(arr[i][duplicated_source].equals("true"))
                arr[i][duplicated_source] = "1";
            else if(arr[i][duplicated_source].equals("false"))
                arr[i][duplicated_source] = "0";
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

            if(arr[i][duplicated_source].equals("1"))
                arr[i][duplicated_source] = "true";
            else if(arr[i][duplicated_source].equals("0"))
                arr[i][duplicated_source] = "false";
        }

        return transformedCSV;
    }

    private void checkVerticality(CSV csv) throws RuntimeException {
        if (!csv.isVertically()) throw new RuntimeException("CSV is not vertically");
    }
}
