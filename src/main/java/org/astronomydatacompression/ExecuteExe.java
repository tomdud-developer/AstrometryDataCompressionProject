package org.astronomydatacompression;

import java.io.IOException;

public class ExecuteExe {
    public static void main(String[] args) {
        try {
            String command = "C:\\Users\\tomas\\Downloads\\m03\\M03.exe e 100b C:\\Users\\tomas\\Downloads\\m03\\data.txt C:\\Users\\tomas\\Downloads\\m03\\compressed2.txt";
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();


            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("Proces został zakończony pomyślnie.");
            } else {
                System.out.println("Wystąpił błąd podczas wykonywania procesu. Kod błędu: " + exitCode);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}

