package org.astronomydatacompression.session;

import org.astronomydatacompression.compression.CompressMethod;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Session {

    private final String SESSION_ID;
    private Path workingDirectoryPath;
    private File fileToCompress;
    List<CompressMethod> methodsList;

    public Session() {
        SESSION_ID = generateSessionId();
    }
    private String generateSessionId() {
        Random random = new Random(5000);
        String randomLong = String.valueOf(random.nextLong(10000L, 1000000000L));
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        return
                "SESSION_ID_" + randomLong + "_" + date;
    }

    public String getSESSION_ID() {
        return SESSION_ID;
    }

    public Path getWorkingDirectoryPath() {
        return workingDirectoryPath;
    }

    public void setWorkingDirectoryPath(String workingDirectoryPath) {
        try {
            this.workingDirectoryPath = Paths.get(workingDirectoryPath);
        } catch (InvalidPathException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public File getFileToCompress() {
        return fileToCompress;
    }

    public void setFileToCompress(String fileNameToCompress) {
        try {
            Path filePathToCompress = Paths.get(workingDirectoryPath.toString(), fileNameToCompress);
            fileToCompress = new File(filePathToCompress.toUri());
        } catch (InvalidPathException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void setMethodsList(List<CompressMethod> methodsList) {
        this.methodsList = methodsList;
    }

    public List<CompressMethod> getMethodsList() {
        return methodsList;
    }

/*     try {
        for (CompressMethod method : methodsList) {
            method.getCompressClass().getDeclaredConstructor().newInstance(session.getFileToCompress());
        }
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        throw new RuntimeException(e);
    }*/



}
