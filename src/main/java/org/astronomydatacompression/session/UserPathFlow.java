package org.astronomydatacompression.session;

import org.astronomydatacompression.compression.CompressMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserPathFlow implements Runnable {
    private static final Logger logger = Logger.getLogger(UserPathFlow.class.getName());

    private final Scanner scanner;
    private final Session session;

    public UserPathFlow() {
        scanner = new Scanner(System.in);
        session = new Session();
    }
    @Override
    public void run() {
        writeStartInformation();
        writeSessionInformation();
        getTheDirectoryInformation();
        getMethodsToUse();
        decideToStart();
    }

    private void writeStartInformation() {
        System.out.println("Title: Astronomy Data Compression Project");
        System.out.println("Version: V0.1");
        System.out.println("Author: Tomasz Dudzik");
    }

    private void writeSessionInformation() {
        System.out.println("Your session ID is " + session.getSESSION_ID());
        System.out.println();
    }

    private void getTheDirectoryInformation() {
        System.out.println("Write the path of working directory:");
        String pathName = scanner.nextLine();
        session.setWorkingDirectoryPath(pathName);

        System.out.println("Write the name of file to compress:");
        String fileName = scanner.nextLine();
        session.setFileToCompress(fileName);

        System.out.println("Working directory: " + session.getWorkingDirectoryPath());
        System.out.println("File to compress: " + session.getFileToCompress().getPath());
        System.out.println();
    }

    private void getMethodsToUse() {
        System.out.println("Methods signature: 0.M03 ");
        System.out.println("Before start data compression write methods signatures (0,1,2,3), to start all methods write (all), to stop write (exit)");
        String command = scanner.nextLine();
        List<CompressMethod> methodsList = null;
        if(command.equals("exit")) {
            System.exit(0);
        } else if(command.equals("all")) {
            methodsList = Arrays.stream(CompressMethod.values()).toList();
        } else {
            methodsList = Arrays.stream(command.split(","))
                    .map(Integer::parseInt)
                    .map(i -> CompressMethod.values()[i])
                    .toList();
        }
        session.setMethodsList(methodsList);
        String chosenMethodsInfo = "Chosen Methods: " + methodsList.stream().map(Enum::toString).reduce((info, x) -> info += x).get();
        logger.log(Level.INFO, chosenMethodsInfo);
    }

    private void decideToStart() {
        System.out.println("To start write 'start', to stop 'exit'");
        String command = scanner.nextLine();
        if(command.equals("exit"))
            System.exit(0);

        Thread sessionThread = new Thread(session);
        sessionThread.start();
    }
}
