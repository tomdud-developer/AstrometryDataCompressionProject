package org.astronomydatacompression.statistics;

import org.astronomydatacompression.compression.CompressMethod;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class StatisticsCSVPrinter implements Printable {

    private final List<SessionStatistics> sessionStatistics;

    public StatisticsCSVPrinter(List<SessionStatistics> sessionStatistics) {
        this.sessionStatistics = sessionStatistics;
    }
    

    @Override
    public File saveToPath(Path path) {
        File file = deleteIfExistsAndCreateNewFile(path);
        writeStatisticsToFile(file);

        return file;
    }

    private void writeStatisticsToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (SessionStatistics sessionStatistic: sessionStatistics) {
                writeSessionStatistic(sessionStatistic, writer);
                writer.write("\n");
                writer.write("\n");
                writer.write("\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeSessionStatistic(SessionStatistics sessionStatistics, BufferedWriter writer) throws IOException {
        writer.write(String.format("### Session ID: %s ###", sessionStatistics.getSESSION_ID()));
        writer.write("\n");
        writer.write("\n");
        writeModificationStatistics(sessionStatistics.getModificationStatistics(), writer);
        writer.write("\n");
        writer.write("\n");
        writeCompressors(sessionStatistics.getCompressMethodList(), writer);
        writer.write("\n");
        writer.write("\n");
        writeCompressionStatistics(sessionStatistics, sessionStatistics.getCompressionStatistics(), writer);
        writer.write("\n");
        writer.write("\n");
        writeDecompressionStatistics(sessionStatistics.getDecompressionStatistics(), writer);
        writer.write("\n");

    }


    private void writeModificationStatistics(ModificationStatistics modificationStatistics, BufferedWriter writer) throws IOException {
        writer.write("### Modification Statistics ###,");
        modificationStatistics.getModifierList().forEach(m -> {
            try {
                writer.write(m.name() + ",");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        writer.write("Modifications time, Reversal time, Check integrity time");
        writer.write("\n");
        writer.write(modificationStatistics.getModificationTimeInSeconds() + ",");
        writer.write(modificationStatistics.getReversalTimeInSeconds() + ",");
        writer.write(modificationStatistics.getCheckEqualsTimeInSeconds() + "");
    }

    private void writeCompressors(List<CompressMethod> compressMethods, BufferedWriter writer) throws IOException {
        compressMethods.forEach(m -> {
            try {
                writer.write(m.name() + ",");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void writeCompressionStatistics(SessionStatistics sessionStatistics, List<CompressionStatistics> compressionStatistics, BufferedWriter writer) throws IOException {
        writer.write("### Compression Statistics ###,");
        writeCompressors(
                compressionStatistics.stream().map(CompressionStatistics::getCompressMethod).toList(),
                writer);
        writer.write("\n");

        StringBuilder stringBuilder = new StringBuilder("CompressionRatio,");
        compressionStatistics.stream().map(CompressionStatistics::getCompressionRatio).forEach(s->stringBuilder.append(s).append(","));

        stringBuilder.append("\nOutputSizeInMB,");
        compressionStatistics.stream().map(CompressionStatistics::getOutputSizeInMB).forEach(s->stringBuilder.append(s).append(","));

        stringBuilder.append("\nCompressionSpeedInMBPS,");
        compressionStatistics.stream().map(CompressionStatistics::getCompressionSpeedInMBPS).forEach(s->stringBuilder.append(s).append(","));

        stringBuilder.append("\nCompressionTimeInSeconds,");
        compressionStatistics.stream().map(CompressionStatistics::getCompressionTimeInSeconds).forEach(s->stringBuilder.append(s).append(","));

        stringBuilder.append("\nGeneral compression ratio,");
        compressionStatistics.stream().map(CompressionStatistics::getOutputSizeInMB).forEach(s->stringBuilder.append(sessionStatistics.getOriginalFileSizeInMB() / s).append(","));

        writer.write(stringBuilder.toString());
    }

    private void writeDecompressionStatistics(List<DecompressionStatistics> decompressionStatistics, BufferedWriter writer) throws IOException {
        writer.write("### Decompression Statistics ###,");
        writeCompressors(
                decompressionStatistics.stream().map(DecompressionStatistics::getCompressMethod).toList(),
                writer);
        writer.write("\n");

        StringBuilder stringBuilder = new StringBuilder("DecompressedFileInMB,");
        decompressionStatistics.stream().map(DecompressionStatistics::getDecompressedFileInMB).forEach(s->stringBuilder.append(s).append(","));

        stringBuilder.append("\nDecompressionSpeedInMBPS,");
        decompressionStatistics.stream().map(DecompressionStatistics::getDecompressionSpeedInMBPS).forEach(s->stringBuilder.append(s).append(","));

        stringBuilder.append("\nCompressionSpeedInMBPS,");
        decompressionStatistics.stream().map(DecompressionStatistics::getDecompressionTimeInSeconds).forEach(s->stringBuilder.append(s).append(","));

        stringBuilder.append("\nDecompressionTimeInSeconds,");
        decompressionStatistics.stream().map(DecompressionStatistics::getDecompressionTimeInSeconds).forEach(s->stringBuilder.append(s).append(","));

        writer.write(stringBuilder.toString());
    }


    protected File deleteIfExistsAndCreateNewFile(Path path) {
        try {
            if (path.toFile().exists())
                Files.delete(path);
            return Files.createFile(path).toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
