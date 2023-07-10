package org.astronomydatacompression.csv;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Setter
public class CSV implements Transpositionable, Saveable {
    private File file;
    private String[][] array;
    private int width;
    private int height;
    private boolean isVertically;

    private CSV() { }

    public static CSV loadFromFile(File file) throws FileNotFoundException {
        if(!file.exists() && file.isDirectory())
            throw new FileNotFoundException("File not found");

        CSV csv = new CSV();
        csv.setFile(file);
        csv.initializeArrayLengthWidth();
        csv.readCSV();

        return csv;
    }

    private void initializeArrayLengthWidth() {
        try (
                InputStream inputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8));
        ) {
            String line = bufferedReader.readLine();
            isVertically = checkVerticality(line);
            width = line.split(",").length;
            height = 0;

            while(line != null) {
                height++;
                line = bufferedReader.readLine();
            }

            array = new String[height][width];

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean checkVerticality(String firstLine) {
        int commaCounter = 0;
        int i = 0;
        while(commaCounter < 1) {
            if(firstLine.charAt(i) == ',') {
                commaCounter++;
            } else {
                i++;
            }
        }
        char secondColumnFirstCharacter = firstLine.charAt(i + 1);
        return Character.isLetter(secondColumnFirstCharacter);
    }

    private void readCSV() {
        try (
                InputStream inputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8));
        ) {
            String line = bufferedReader.readLine();

            int row = 0;
            while(line != null) {
                array[row] = line.split(",");
                line = bufferedReader.readLine();
                row++;
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public File saveToFile() {
        try {
            Path tempFilePath = Files.createTempFile(file.getParentFile().toPath(), null , null);
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath.toFile()));

            for (int row = 0; row < height; row++) {
                writeLine(writer, row);
                if(row + 1 < height) writer.write("\n");
            }

            writer.close();

            return tempFilePath.toFile();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void writeLine(BufferedWriter writer, int row) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < width; i++) {
            stringBuffer.append(array[row][i]);
            stringBuffer.append(",");
        }

        stringBuffer.deleteCharAt(stringBuffer.lastIndexOf(","));


        writer.write(stringBuffer.toString());
    }

    @Override
    public CSV transpose(CSV csv) {
        return null;
    }


}
