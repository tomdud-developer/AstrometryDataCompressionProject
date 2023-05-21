package org.astronomydatacompression.session;

import org.astronomydatacompression.compression.Compress;
import org.astronomydatacompression.compression.CompressMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Flow implements Runnable {

    private final Scanner scanner;
    private final Session session;

    public Flow() {
        scanner = new Scanner(System.in);
        session = new Session();
    }
    @Override
    public void run() {
        writeStartInformation();
        writeSessionInformation();
        getTheDirectoryInformation();
        getMethodsToUse();
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
        System.out.println("To start data compression write methods signatures (0,1,2,3), to stop write -1");
        String command = scanner.nextLine();
        if(command.equals("-1"))
            System.exit(0);
        List<CompressMethod> methodsList = Arrays.stream(command.split(","))
                .map(Integer::parseInt)
                .map(i ->CompressMethod.values()[i])
                .toList();
        session.setMethodsList(methodsList);
        System.out.println("Methods: ");
        methodsList.forEach((method) -> System.out.print(method.toString() + "  "));
    }
}
