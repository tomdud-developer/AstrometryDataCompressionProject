package org.astronomydatacompression.resultspresentation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.astronomydatacompression.statistics.CompressionStatistics;

public class JavaFxController {
    @FXML
    private Label algorithmLabel;
    @FXML
    private Label compressedSizeLabel;
    @FXML
    private Label originalSizeLabel;

    private CompressionStatistics compressionStatistics;

    public void initialize() {
    }

    public void updateStats(CompressionStatistics compressionStatistics) {

    }
}
