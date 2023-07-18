package org.astronomydatacompression.session;

import org.astronomydatacompression.compression.Compressor;
import org.astronomydatacompression.compression.CompressMethod;
import org.astronomydatacompression.compression.FilesIntegrityChecker;
import org.astronomydatacompression.csv.CSV;
import org.astronomydatacompression.csv.CSVModifier;
import org.astronomydatacompression.csv.Transformer;
import org.astronomydatacompression.properties.PropertiesLoader;
import org.astronomydatacompression.properties.PropertiesType;
import org.astronomydatacompression.statistics.CompressionStatistics;
import org.astronomydatacompression.statistics.DecompressionStatistics;
import org.astronomydatacompression.statistics.ModificationStatistics;
import org.astronomydatacompression.statistics.SessionStatistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Session implements Runnable {

    private final String SESSION_ID;
    private Path workingDirectoryPath;
    private File fileToCompress;
    List<CompressMethod> methodsList;
    private final List<CompressionStatistics> compressionStatistics = new ArrayList<>();
    private final List<DecompressionStatistics> decompressionStatistics = new ArrayList<>();
    private final List<Compressor> compressors = new ArrayList<>();
    private List<CSVModifier> modifiersList;
    private SessionStatistics sessionStatistics;
    private ModificationStatistics modificationStatistics;

    public Session() {
        SESSION_ID = generateSessionId();
        setMethodsList();
        modificationStatistics = new ModificationStatistics(0,0,0, Collections.emptyList());
    }
    private String generateSessionId() {
        Random random = new Random();
        String randomLong = String.valueOf(random.nextLong(10000L, 1000000000L));
        LocalTime currentTime = LocalTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH-mm-ss"));

        return "SESSION_ID_" + randomLong + "_" + formattedTime;
    }

    public String getSESSION_ID() {
        return SESSION_ID;
    }
    
    public void setWorkingDirectoryPath(String workingDirectoryPath) {
        try {
            this.workingDirectoryPath = Paths.get(workingDirectoryPath);
        } catch (InvalidPathException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void setFileToCompress(String fileNameToCompress) {
        try {
            Path filePathToCompress = Paths.get(workingDirectoryPath.toString(), fileNameToCompress);
            fileToCompress = new File(filePathToCompress.toUri());
        } catch (InvalidPathException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void setModifiersList(List<CSVModifier> modifiersList) {
        this.modifiersList = modifiersList;
    }

    private void setMethodsList() {
        List<String> methodsStringList = PropertiesLoader.INSTANCE.getListOfValuesSepratedByComma(PropertiesType.EXTERNAL, "session.methods");
        List<CompressMethod> methodsList = new ArrayList<>();
        for (String methodName: methodsStringList) {
            methodsList.add(CompressMethod.valueOf(methodName));
        }
        this.methodsList = methodsList;
    }
    
    public boolean isShouldBeParallelComputing() {
        return Boolean.getBoolean(PropertiesLoader.INSTANCE.getValueByKey(PropertiesType.EXTERNAL, "session.isShouldBeParallelComputing"));
    }
    
    @Override
    public void run() {
        System.out.println(String.format("Session %s was started.", SESSION_ID));
        createWorkingDirectory();
        copyFileToSessionDirectory();
        if(!modifiersList.isEmpty())
            modifyFileByModifiers();

        System.out.println("Create Compression Threads.");
        List<Thread> threads = new ArrayList<>();

        try {
            for (CompressMethod compressMethod : methodsList) {
                Compressor compressor = compressMethod.getCompressClass().getDeclaredConstructor(File.class, Path.class).newInstance(fileToCompress, workingDirectoryPath);
                compressors.add(compressor);
                Thread compressThread = new Thread(compressor);
                compressThread.start();
                threads.add(compressThread);
                if(!isShouldBeParallelComputing())compressThread.join();
            }

            if(isShouldBeParallelComputing()) for (Thread thread : threads) thread.join();

            System.out.println("All threads have ended. Collect statistics.");
            collectStatisticsFromCompressors();
            checkFilesIntegrity();

            generateSessionStatistics();

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void modifyFileByModifiers() {
        try {
            CSV orgCSV = CSV.loadFromFile(fileToCompress);

            Transformer transformer = new Transformer(orgCSV);
            long modifyStartTime = System.nanoTime();
            CSV modifiedCSV = applyModifiersChain(orgCSV, transformer);
            long modifyTime = System.nanoTime() - modifyStartTime;

            fileToCompress = createNewFileAfterModifiers(orgCSV, modifiedCSV);

            long reversalStartTime = System.nanoTime();
            CSV reversalCSV = applyReversalsChain(modifiedCSV, transformer);
            long reversalTime = System.nanoTime() - reversalStartTime;

            long checkEqualsStartTime = System.nanoTime();
            if(!orgCSV.equals(reversalCSV))
                throw new RuntimeException("CSV after reversals is not the same with original csv");
            long checkEqualsTime = System.nanoTime() - checkEqualsStartTime;

            this.modificationStatistics = new ModificationStatistics(modifyTime, reversalTime, checkEqualsTime, modifiersList);

            System.out.println(modificationStatistics);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private CSV applyModifiersChain(CSV orgCSV, Transformer transformer) {
        CSV csv = orgCSV;

        if(modifiersList.contains(CSVModifier.TRANSFORM_BOOLEANS))
            csv = transformer.transformBoolean();
        if(modifiersList.contains(CSVModifier.TRANSFORM_NOT_AVAILABLE))
            csv = transformer.transformNotAvailable();
        if(modifiersList.contains(CSVModifier.TRANSFORM_SOLUTION_ID))
            csv = transformer.transformID();
        if(modifiersList.contains(CSVModifier.TRANSFORM_REF_EPOCHS))
            csv = transformer.transformRefEpochs();
        if(modifiersList.contains(CSVModifier.TRANSPOSE))
            csv = csv.transpose();

        return csv;
    }

    private File createNewFileAfterModifiers(CSV orgCSV, CSV modifiedCSV) {
        StringBuilder newFileName = new StringBuilder();
        for (CSVModifier modifier : modifiersList)
            newFileName.append(modifier.getShortName()).append("_");

        newFileName.append(orgCSV.getFile().getName());
        File savedFile = modifiedCSV.saveToFile(
                Paths.get(orgCSV.getFile().getParentFile().getPath(), newFileName.toString()));
        modifiedCSV.setFile(savedFile);

        return savedFile;
    }

    private CSV applyReversalsChain(CSV modifiedCSV, Transformer transformer) {
        CSV csv = modifiedCSV;
        transformer.setModifiedCSV(modifiedCSV);

        if(modifiersList.contains(CSVModifier.TRANSPOSE)) {
            csv = csv.transpose();
            transformer.setModifiedCSV(csv);
        }
        if(modifiersList.contains(CSVModifier.TRANSFORM_BOOLEANS))
            csv = transformer.revertTransformBoolean();
        if(modifiersList.contains(CSVModifier.TRANSFORM_NOT_AVAILABLE))
            csv = transformer.revertTransformNotAvailable();
        if(modifiersList.contains(CSVModifier.TRANSFORM_SOLUTION_ID))
            csv = transformer.revertTransformID();
        if(modifiersList.contains(CSVModifier.TRANSFORM_REF_EPOCHS))
            csv = transformer.revertTransformRefEpochs();

        return csv;
    }

    private void copyFileToSessionDirectory() {
        Path copiedFilePath = null;
        try {
            copiedFilePath = Files.copy(fileToCompress.toPath(), Path.of(workingDirectoryPath.toString(), fileToCompress.getName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileToCompress = copiedFilePath.toFile();
    }

    private void generateSessionStatistics() {
         sessionStatistics = new SessionStatistics(
                 SESSION_ID,
                 fileToCompress,
                 modificationStatistics,
                 compressionStatistics,
                 decompressionStatistics,
                 compressors.stream().map(Compressor::getMethod).toList()
         );
    }


    private void collectStatisticsFromCompressors() {
        compressors.forEach(
                compressor -> {
                    compressionStatistics.add(compressor.getCompressionStatistics());
                    decompressionStatistics.add(compressor.getDecompressionStatistics());
                }
        );
        compressionStatistics.forEach(System.out::println);
        decompressionStatistics.forEach(System.out::println);
    }

    private void checkFilesIntegrity() {
        System.out.println("### Check integrity of files ###");
        for (DecompressionStatistics decompressionStats: decompressionStatistics) {
            try {
                boolean isTheSame = FilesIntegrityChecker.compareByMemoryMappedFiles(fileToCompress.toPath(), decompressionStats.getDecompressedFile().toPath());
                System.out.printf("For method %s files are %s%n", decompressionStats.getCompressMethod(), isTheSame ? "THE SAME" : "NOT THE SAME");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void createWorkingDirectory() {
        String folderName = getSESSION_ID();
        try {
            Path folderPath = Paths.get(workingDirectoryPath.toString(), folderName);
            Files.createDirectory(folderPath);
            System.out.println("Created new folder with name " + folderPath);
            workingDirectoryPath = folderPath;

            folderPath = Paths.get(workingDirectoryPath.toString(), "logs_" + Compressor.Operation.COMPRESSION);
            Files.createDirectory(folderPath);

            folderPath = Paths.get(workingDirectoryPath.toString(), "logs_" + Compressor.Operation.DECOMPRESSION);
            Files.createDirectory(folderPath);
        } catch (Exception e) {
            System.out.println("Folder creation error. " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return String.format("""
                        ### Session informations ###
                        Your session ID is %s
                        Working directory: %s
                        File for compressor: %s
                        Chosen Methods: %s
                        """,
                SESSION_ID, workingDirectoryPath, fileToCompress.getPath(),
                methodsList.stream().map(Enum::toString).reduce((info, x) -> info += (x + " ") ).get()
        );
    }

    public SessionStatistics getSessionStatistics() {
        return sessionStatistics;
    }
}
