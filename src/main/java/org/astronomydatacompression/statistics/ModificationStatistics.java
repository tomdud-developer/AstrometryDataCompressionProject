package org.astronomydatacompression.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.astronomydatacompression.csv.CSVModifier;

import java.lang.reflect.Modifier;
import java.util.List;

@Getter
public class ModificationStatistics {
    private final double modificationTimeInSeconds;
    private final double demodificationTimeInSeconds;
    private final double checkEqualsTimeInSeconds;
    private final List<CSVModifier> modifierList;

    public ModificationStatistics(long modificationTimeInNanoSeconds,
                                  long demodificationTimeInNanoSeconds,
                                  long checkEqualsTimeInNanoSeconds,
                                  List<CSVModifier> modifierList) {
        this.modificationTimeInSeconds = modificationTimeInNanoSeconds / 1_000_000_000.0;
        this.demodificationTimeInSeconds = demodificationTimeInNanoSeconds / 1_000_000_000.0;
        this.checkEqualsTimeInSeconds = checkEqualsTimeInNanoSeconds / 1_000_000_000.0;
        this.modifierList = modifierList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ModificationStatistics{");
        sb.append("modificationTimeInSeconds=").append(modificationTimeInSeconds);
        sb.append(", demodificationTimeInSeconds=").append(demodificationTimeInSeconds);
        sb.append(", checkEqualsTimeInSeconds=").append(checkEqualsTimeInSeconds);
        sb.append(", modifierList=").append(modifierList);
        sb.append('}');
        return sb.toString();
    }
}
