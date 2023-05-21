package org.astronomydatacompression.session;

import org.astronomydatacompression.compression.Compress;
import org.astronomydatacompression.compression.CompressMethod;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Session implements Runnable {

    private static final Logger logger = Logger.getLogger(Session.class.getName());

    private final String SESSION_ID;
    private Path workingDirectoryPath;
    private File fileToCompress;
    List<CompressMethod> methodsList;

    public Session() {
        SESSION_ID = generateSessionId();
    }
    private String generateSessionId() {
        Random random = new Random();
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

    @Override
    public void run() {
        logger.log(Level.INFO, "Session was started.");
        createWorkingDirectory();
        logger.log(Level.INFO, "Create Compression Threads.");
        try {
            for (CompressMethod compressMethod : methodsList) {
                Compress compress = compressMethod.getCompressClass().getDeclaredConstructor(File.class, Path.class).newInstance(fileToCompress, workingDirectoryPath);
                Thread compressThread = new Thread(compress);
                compressThread.start();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void createWorkingDirectory() {
        String folderName = getSESSION_ID();
        try {
            Path folderPath = Paths.get(workingDirectoryPath.toString(), folderName);
            Files.createDirectory(folderPath);
            logger.log(Level.INFO, "Created new folder with name " + folderPath);
            workingDirectoryPath = folderPath;
        } catch (Exception e) {
            System.out.println("Folder creation error. " + e.getMessage());
        }
    }

}
