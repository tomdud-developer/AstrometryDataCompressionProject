package org.astronomydatacompression.resultspresentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.astronomydatacompression.compression.CompressMethod;
import org.astronomydatacompression.statistics.CompressionStatistics;
import org.astronomydatacompression.statistics.SessionStatistics;

public class JavaFxController {
    @FXML
    private Label label1;
    @FXML
    private BarChart<String, Number> compressionTimeBarChart;
    @FXML
    private ListView<String> inputFileInformationListView;

    @FXML
    private ListView<String> compressorsInformationListView;

    private CompressionStatistics compressionStatistics;

    private SessionStatistics sessionStatistics;

    public void initialize() {

        JavaFXApplication.latch.countDown();
    }

    public void updateStats(SessionStatistics sessionStatistics) {
        this.sessionStatistics = sessionStatistics;

        setUpInputFileInformationListView();
        setCompressionTimeChart();
        setUpCompressorsInformationListView();

    }

    private void setUpInputFileInformationListView() {
        ObservableList<String> informationsAboutFile = FXCollections.observableArrayList(
                "Name: " + sessionStatistics.getOriginalFile().getName(),
                "Extension: " + sessionStatistics.getOriginalFile().getName().split("\\.")[1],
                "Size: " + sessionStatistics.getOriginalFileSizeInMB() + " MB"
        );
        inputFileInformationListView.setItems(informationsAboutFile);
    }

    private void setUpCompressorsInformationListView() {
        ObservableList<String> compressorsNames = FXCollections.observableArrayList(
            sessionStatistics.getCompressionStatistics().stream().map(CompressionStatistics::getCompressMethod).map(Enum::toString).toList()
        );
        compressorsInformationListView.setItems(compressorsNames);
    }

    public void setCompressionTimeChart() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Compression time");
        for (CompressionStatistics compressionStatistics : sessionStatistics.getCompressionStatistics()) {
            series.getData().add(new XYChart.Data(compressionStatistics.getCompressMethod().toString(), compressionStatistics.getCompressionTimeInSeconds()));
        }
        compressionTimeBarChart.getData().addAll(series);
    }
}
