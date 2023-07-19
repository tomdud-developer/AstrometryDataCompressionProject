package org.astronomydatacompression.csv;

public abstract class Transformer implements DataTransformer {
    protected CSV orgCSV;
    protected CSV transformedCSV;

    protected String solutionID = null;
    protected String refEpochs = null;

    public final int solution_id_column = 1 - 1;
    public final int phot_variable_flag_column;
    public final int ref_epochs_column;

    private final String not_available_string;

    public Transformer(int phot_variable_flag_column,
                       String not_available_string,
                       int ref_epochs_column) {
        this.phot_variable_flag_column = phot_variable_flag_column;
        this.not_available_string = not_available_string;
        this.ref_epochs_column = ref_epochs_column;
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

    protected void checkVerticality(CSV csv) throws RuntimeException {
        if (!csv.isVertically()) throw new RuntimeException("CSV is not vertically");
    }

    @Override
    public CSV transformNotAvailable() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();

        for (int i = 1; i < transformedCSV.getHeight(); i++) {
            if(arr[i][phot_variable_flag_column].equals(not_available_string))
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
                arr[i][phot_variable_flag_column] = not_available_string;
        }

        return transformedCSV;
    }

    @Override
    public CSV transformRefEpochs() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();
        refEpochs = arr[1][ref_epochs_column];

        for (int i = 1; i < transformedCSV.getHeight(); i++) {
            arr[i][ref_epochs_column] = "";
        }

        return transformedCSV;
    }

    @Override
    public CSV revertTransformRefEpochs() {
        checkVerticality(transformedCSV);
        String[][] arr = transformedCSV.getArray();

        for (int i = 1; i < transformedCSV.getHeight(); i++) {
            arr[i][ref_epochs_column] = refEpochs;
        }

        return transformedCSV;
    }

}
