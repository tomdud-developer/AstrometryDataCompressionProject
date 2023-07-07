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

            int i = 0;
            String line = bufferedReader.readLine().split(",")[0] + (56==i?"":",");
            while(line != null) {
                writer.write(line);
                line = bufferedReader.readLine().split(",")[0] + (56==i?"":",");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
    @Override
    public File makeCopy(File file) {
        return null;
    }

    @Override
    public void deleteCopy(File file) {

    }
}
