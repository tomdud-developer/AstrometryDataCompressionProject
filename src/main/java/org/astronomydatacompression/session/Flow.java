package org.astronomydatacompression.session;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;

import java.util.Scanner;
import java.util.logging.Logger;

public class Flow implements Runnable {
    private static final Logger logger = Logger.getLogger(UserPathFlow.class.getName());

    private final Session session;
    public Flow() {
        session = new Session();
    }

    @Override
    public void run() {
        writeStartInformation();
        session.setWorkingDirectoryPath(PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "session.WorkingDirectoryPath"));
        session.setFileToCompress(PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "session.fileNameToCompress"));;
        writeSessionInformation();

    }

    private void writeStartInformation() {
        System.out.println("Title: Astronomy Data Compression Project");
        System.out.println("Version: V0.2");
        System.out.println("Author: Tomasz Dudzik");
        System.out.println();
    }

    private void writeSessionInformation() {
        System.out.println("Your session ID is " + session.getSESSION_ID());
        System.out.println("Working directory: " + session.getWorkingDirectoryPath());
        System.out.println("File to compress: " + session.getFileToCompress().getPath());
        System.out.println();
    }

}
