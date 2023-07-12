package org.astronomydatacompression.session;

import javafx.application.Application;
import org.astronomydatacompression.csv.CSVModifier;
import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;
import org.astronomydatacompression.resultspresentation.JavaFXApplication;
import org.astronomydatacompression.statistics.SessionStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Flow implements Runnable {

    private List<Session> sessions;
    public Flow() {
        sessions = new ArrayList<>();
    }

    @Override
    public void run() {
        writeStartInformation();
        createSessions();

        for (Thread sessionThread : createSessionThreads()) {
            sessionThread.start();
            try {
                System.out.println("Run new session Thread");
                sessionThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        List<SessionStatistics> sessionStatisticsList = retrieveSessionStatistics();
        runJavaFXApplication(sessionStatisticsList);
    }

    public void createSessions() {
        System.out.println("Start sessions configuration");
        List<String> fileNameToCompressList = PropertiesLoader.INSTANCE.getListOfValuesDefinedInArray(PropertiesType.EXTERNAL, "session.fileNameToCompress");
        List<List<String>> modifiersForEachSession = PropertiesLoader.INSTANCE.getListOfValuesSeparatedByCommaDefinedInArray(PropertiesType.EXTERNAL, "session.fileToCompressModifiers");


        for (int i = 0; i < fileNameToCompressList.size(); i++) {
            Session session = new Session();
            session.setWorkingDirectoryPath(PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "session.WorkingDirectoryPath"));
            session.setFileToCompress(fileNameToCompressList.get(i));
            session.setModifiersList(
                    modifiersForEachSession.get(i).stream().map(CSVModifier::valueOf).toList()
            );
            sessions.add(session);
            System.out.println(session);
        }
    }

    public List<Thread> createSessionThreads() {
        List<Thread> threads = new ArrayList<>();
        sessions.forEach(session -> {
            Thread sessionThread = new Thread(session);
            threads.add(sessionThread);
        });

        return threads;
    }

    private List<SessionStatistics> retrieveSessionStatistics() {
        return sessions.stream().map(Session::getSessionStatistics).toList();
    }
    private void runJavaFXApplication(List<SessionStatistics> sessionStatisticsList) {
        new Thread(() -> Application.launch(JavaFXApplication.class)).start();
        JavaFXApplication javaFXApplication = JavaFXApplication.waitForInstance();
        javaFXApplication.sendStatistcsToShow(sessionStatisticsList);
    }

    private void writeStartInformation() {
        System.out.println("Title: Astronomy Data Compression Project");
        System.out.println("Version: V0.3");
        System.out.println("Author: Tomasz Dudzik");
        System.out.println();
    }


}
