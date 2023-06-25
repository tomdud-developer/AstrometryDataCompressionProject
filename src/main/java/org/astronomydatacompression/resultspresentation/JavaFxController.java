package org.astronomydatacompression.resultspresentation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.astronomydatacompression.statistics.CompressionStatistics;

public class JavaFxController {
    @FXML
    private Label label1;


    private CompressionStatistics compressionStatistics;

    public void initialize() {
    }

    public void updateStats(CompressionStatistics compressionStatistics) {
        label1.setText("Invoked");
    }
}
