package org.astronomydatacompression.datamodifier;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

public class RowsColumnsChanger implements Modifable {

    public File changeRowWithColumns(File file) {

        try (
                InputStream inputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8));
                )
        {

            Path tempFilePath = Files.createTempFile(file.getParentFile().toPath(), "temp_" , null);
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath.toFile()));

            for (int i = 0; i < getColumnsNumber(file); i++) {
                writer.write(readCsvColumnAsALine(file, i).toString());
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    private StringBuilder readCsvColumnAsALine(File file, int column) {
        try (
                InputStream inputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8));
        ) {
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();

            while(line != null) {
                //stringBuilder.append(line.split(",")[column]).append(",");
                stringBuilder.append(getColumnFromLine(line, column)).append(",");
                line = bufferedReader.readLine();
            }
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
            stringBuilder.append("\n");

            return stringBuilder;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private int getColumnsNumber(File file) {
        try (
                InputStream inputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8));
        ) {
            return bufferedReader.readLine().split(",").length;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getColumnFromLine(String line, int columnIndex) {
        int lineLength = line.length();
        int commaCounter = 0;
        int startIndex = 0;
        for (int i = 0; i < line.length(); i++) {
            if(commaCounter == columnIndex) {
                startIndex = i;
                break;
            }

            if(line.charAt(i) == ',')
                commaCounter++;
        }

        int charCounter = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while(startIndex + charCounter < lineLength && line.charAt(startIndex + charCounter) != ',') {
            stringBuilder.append(line.charAt(startIndex + charCounter));
            charCounter++;
        }

        return stringBuilder.toString();
    }
    @Override
    public File makeCopy(File file) {
        return null;
    }

    @Override
    public void deleteCopy(File file) {

    }
}
