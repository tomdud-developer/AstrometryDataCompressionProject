package org.astronomydatacompression.csv;

import java.util.HashMap;
import java.util.Map;

public class DR3Transformer extends Transformer {

    private final int designation_column = 2 - 1;
    private String designationString;
    private final int libname_gspphot_column = 152 - 1;
    private Map<String, String> gspphotDictionary;

    public DR3Transformer(CSV orgCSV) {
        super(112 - 1, "\"NOT_AVAILABLE\"", 5-1);
        this.orgCSV = orgCSV;
        this.transformedCSV = orgCSV.copy();

        gspphotDictionary = new HashMap<>();
        gspphotDictionary.put("\"MARCS\"", "M");
        gspphotDictionary.put("\"PHOENIX\"", "P");
        gspphotDictionary.put("\"OB\"", "O");

        gspphotDictionary.put("M", "\"MARCS\"");
        gspphotDictionary.put("P", "\"PHOENIX\"");
        gspphotDictionary.put("O", "\"OB\"");
    }

    public CSV applyAllTransforms() {
        transformNull();
        transformID();
        transformBoolean();
        transformNotAvailable();
        transformRefEpochs();
        transformDesignation();
        transformGspphotByDictionary();

        return transformedCSV;
    }

    public CSV applyAllRevertTransforms() {
        revertTransformID();
        revertTransformBoolean();
        revertTransformNotAvailable();
        revertTransformRefEpochs();
        revertTransformDesignation();
        revertTransformNull();
        transformGspphotByDictionary();

        return transformedCSV;
    }

    @Override
    public CSV transformBoolean() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();

        for (int i = 1; i < transformedCSV.getHeight(); i++)
            for (int j = 0; j < transformedCSV.getWidth(); j++) {
                if(arr[i][j].equals("\"True\""))
                    arr[i][j] = "&";
                else if(arr[i][j].equals("\"False\""))
                    arr[i][j] = "~";
            }

        return transformedCSV;
    }

    @Override
    public CSV revertTransformBoolean() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();

        for (int i = 1; i < transformedCSV.getHeight(); i++)
            for (int j = 0; j < transformedCSV.getWidth(); j++) {
                if(arr[i][j].equals("&"))
                    arr[i][j] = "\"True\"";
                else if(arr[i][j].equals("~"))
                    arr[i][j] = "\"False\"";
            }

        return transformedCSV;
    }


    public CSV transformDesignation() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();
        designationString = arr[1][designation_column].split(" ")[0] + " " + arr[1][designation_column].split(" ")[1];

        for (int i = 1; i < transformedCSV.getHeight(); i++) {
            arr[i][designation_column] = "";
        }

        return transformedCSV;
    }

    public CSV revertTransformDesignation() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();

        for (int i = 1; i < transformedCSV.getHeight(); i++) {
            arr[i][designation_column] = designationString + " " + arr[i][2] + "\"";
        }

        return transformedCSV;
    }

    public CSV transformNull() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();

        for (int i = 1; i < transformedCSV.getHeight(); i++)
            for (int j = 0; j < transformedCSV.getWidth(); j++) {
                if(arr[i][j].equals("null"))
                    arr[i][j] = "";
            }

        return transformedCSV;
    }

    public CSV revertTransformNull() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();

        for (int i = 1; i < transformedCSV.getHeight(); i++)
            for (int j = 0; j < transformedCSV.getWidth(); j++) {
                if(arr[i][j].isEmpty())
                    arr[i][j] = "null";
            }

        return transformedCSV;
    }

    public CSV transformGspphotByDictionary() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();

        for (int i = 1; i < transformedCSV.getHeight(); i++) {
            String str = gspphotDictionary.get(arr[i][libname_gspphot_column]);
            if(str != null) {
                arr[i][libname_gspphot_column] = str;
            }
        }

        return transformedCSV;
    }





}
