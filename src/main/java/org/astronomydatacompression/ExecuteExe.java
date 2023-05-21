package org.astronomydatacompression;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecuteExe {
    public static void main(String[] args) {

        String command = "C:\\Users\\tomas\\Downloads\\m03\\M03.exe";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command, "e", "100b", "C:\\Users\\tomas\\Downloads\\m03\\data.txt", "C:\\Users\\tomas\\Downloads\\m03\\compressed2.txt");
            Process process = processBuilder.start();

            // Odczyt danych wyjściowych (stdout)
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Wyjście: " + line);
            }

            // Odczyt danych błędów (stderr)
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println("Błąd: " + line);
            }

            process.waitFor(); // Oczekiwanie na zakończenie procesu

            // Sprawdzanie kodu zakończenia procesu
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("Proces został zakończony pomyślnie.");
            } else {
                System.out.println("Wystąpił błąd podczas wykonywania procesu. Kod błędu: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

