package org.astronomydatacompression.session;

import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Flow implements Runnable {
    private static final Logger logger = Logger.getLogger(Flow.class.getName());

    private final Session session;
    public Flow() {
        session = new Session();
    }

    @Override
    public void run() {
        writeStartInformation();

        System.out.println("Start session configuration");
        session.setWorkingDirectoryPath(PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "session.WorkingDirectoryPath"));
        session.setFileToCompress(PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "session.fileNameToCompress"));;

        System.out.println(session);

        System.out.println("Run session Thread");
        Thread sessionThread = new Thread(session);
        sessionThread.start();
    }

    private void writeStartInformation() {
        System.out.println("Title: Astronomy Data Compression Project");
        System.out.println("Version: V0.2");
        System.out.println("Author: Tomasz Dudzik");
        System.out.println();
    }


}
