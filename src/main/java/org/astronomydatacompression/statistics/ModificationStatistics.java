package org.astronomydatacompression.statistics;

import lombok.Getter;
import org.astronomydatacompression.csv.CSVModifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Getter
public class ModificationStatistics {
    private final File fileAfterModifiers;
    private final double modificationTimeInSeconds;
    private final double reversalTimeInSeconds;
    private final double checkEqualsTimeInSeconds;
    private final List<CSVModifier> modifierList;

    public ModificationStatistics(File fileAfterModifiers,
                                  long modificationTimeInNanoSeconds,
                                  long reversalTimeInNanoSeconds,
                                  long checkEqualsTimeInNanoSeconds,
                                  List<CSVModifier> modifierList) {
        this.fileAfterModifiers = fileAfterModifiers;
        this.modificationTimeInSeconds = modificationTimeInNanoSeconds / 1_000_000_000.0;
        this.reversalTimeInSeconds = reversalTimeInNanoSeconds / 1_000_000_000.0;
        this.checkEqualsTimeInSeconds = checkEqualsTimeInNanoSeconds / 1_000_000_000.0;
        this.modifierList = modifierList;
    }

    public double getFileSizeAfterModifiers() {
        try {
            return Files.size(fileAfterModifiers.toPath()) / 1000_000.0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ModificationStatistics{");
        sb.append("modificationTimeInSeconds=").append(modificationTimeInSeconds);
        sb.append(", reversalTimeInSeconds=").append(reversalTimeInSeconds);
        sb.append(", checkEqualsTimeInSeconds=").append(checkEqualsTimeInSeconds);
        sb.append(", modifierList=").append(modifierList);
        sb.append('}');
        return sb.toString();
    }
}
