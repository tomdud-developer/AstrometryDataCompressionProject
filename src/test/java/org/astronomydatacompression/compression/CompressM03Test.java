package org.astronomydatacompression.compression;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

class CompressTest {

    @Test
    void compressM03() {
        File file = new File("Workspace\\data.txt");



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
        Compressor compressor = new CompressorPPMD(file, Paths.get(userDirectory,"\\Workspace\\"));

    }

}