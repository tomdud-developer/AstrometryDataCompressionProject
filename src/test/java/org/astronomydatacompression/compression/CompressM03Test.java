package org.astronomydatacompression.compression;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CompressTest {

    @Test
    void compressM03() {
        File file = new File("Workspace\\data.txt");
        Compress compress = new CompressM03(file, Paths.get("Workspace\\"));
        File compressedFile = compress.compress();


/*        try {
            Files.delete(compressedFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    @Test
    void compressPPMD() {
        String userDirectory = FileSystems.getDefault()
                .getPath("")
                .toAbsolutePath()
                .toString();
        File file = new File(userDirectory + "\\Workspace\\data.txt");
        Compress compress = new CompressPPMD(file, Paths.get(userDirectory,"\\Workspace\\"));
        compress.compress();
    }

}