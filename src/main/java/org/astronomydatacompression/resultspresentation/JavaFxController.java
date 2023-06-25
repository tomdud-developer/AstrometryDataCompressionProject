package org.astronomydatacompression.resultspresentation;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import org.astronomydatacompression.statistics.CompressionStatistics;
import org.astronomydatacompression.statistics.DecompressionStatistics;
import org.astronomydatacompression.statistics.SessionStatistics;

import java.util.List;

public class JavaFxController {
    @FXML
    private BarChart<String, Number> compressionTimeBarChart;
    @FXML
    private BarChart<String, Number> compressionRatioBarChart;
    @FXML
    private BarChart<String, Number> compressedFileSizesBarChart;
    @FXML
    private BarChart<String, Number> compressionSpeedBarChart;
    @FXML
    private BarChart<String, Number> decompressionTimeBarChart;
    @FXML
    private BarChart<String, Number> decompressionSpeedBarChart;
    @FXML
    private ListView<String> inputFileInformationListView;
    @FXML
    private ListView<String> compressorsInformationListView;


    private List<SessionStatistics> sessionStatisticsList;

    public void initialize() {
        JavaFXApplication.latch.countDown();
    }

    public void updateStats(List<SessionStatistics> sessionStatisticsList) {
        this.sessionStatisticsList = sessionStatisticsList;
        Platform.runLater(this::setUpGUIElements);
    }

    public void setUpGUIElements() {
        setUpInputFileInformationListView();
        setUpCompressorsInformationListView();

        setUpCompressionTimeChart();
/*        setUpCompressionSpeedChart();
        setUpCompressedFileSizesBarChart();
        setUpCompressionRatioBarChart();
        setUpDecompressionTimeChart();
        setUpDecompressionSpeedChart();*/
    }

    private void setUpInputFileInformationListView() {
        ObservableList<String> informationsAboutFiles = FXCollections.observableArrayList();

        for (SessionStatistics sessionStatistics : sessionStatisticsList) {
            informationsAboutFiles.addAll(
                    "Name: " + sessionStatistics.getOriginalFile().getName(),
                    "Extension: " + sessionStatistics.getOriginalFile().getName().split("\\.")[1],
                    "Size: " + sessionStatistics.getOriginalFileSizeInMB() + " MB"
            );
        }

        inputFileInformationListView.setItems(informationsAboutFiles);
    }

    private void setUpCompressorsInformationListView() {
        ObservableList<String> compressorsNames = FXCollections.observableArrayList(
            sessionStatisticsList.get(0).getCompressMethodList().stream().map(Enum::toString).toList()
        );
        compressorsInformationListView.setItems(compressorsNames);
    }

    public void setUpCompressionTimeChart() {
        for (SessionStatistics sessionStatistics : sessionStatisticsList) {
            XYChart.Series series = new XYChart.Series();
            series.setName(sessionStatistics.getOriginalFile().getName());
            for (CompressionStatistics compressionStatistics : sessionStatistics.getCompressionStatistics()) {
                series.getData().add(new XYChart.Data(compressionStatistics.getCompressMethod().toString(), compressionStatistics.getCompressionTimeInSeconds()));
            }

            compressionTimeBarChart.getXAxis().setAnimated(false);
            compressionTimeBarChart.getData().add(series);
        }
    }
/*
    public void setUpCompressionSpeedChart() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Compression speed");

        for (CompressionStatistics compressionStatistics : sessionStatistics.getCompressionStatistics()) {
            series.getData().add(new XYChart.Data(compressionStatistics.getCompressMethod().toString(), compressionStatistics.getCompressionSpeedInMBPS()));
        }
        compressionSpeedBarChart.getXAxis().setAnimated(false);
        compressionSpeedBarChart.getData().addAll(series);
    }

    public void setUpCompressionRatioBarChart() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Compression ratio");
        for (CompressionStatistics compressionStatistics : compressionStatisticsList) {
            series.getData().add(new XYChart.Data(compressionStatistics.getCompressMethod().toString(), compressionStatistics.getCompressionRatio()));
        }
        compressionRatioBarChart.getXAxis().setAnimated(false);
        compressionRatioBarChart.getData().addAll(series);
    }

    public void setUpCompressedFileSizesBarChart() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Compressed files sizes");
        for (CompressionStatistics compressionStatistics : compressionStatisticsList) {
            series.getData().add(new XYChart.Data(compressionStatistics.getCompressMethod().toString(), compressionStatistics.getOutputSizeInMB()));
        }
        compressedFileSizesBarChart.getXAxis().setAnimated(false);
        compressedFileSizesBarChart.getData().addAll(series);
    }

    public void setUpDecompressionTimeChart() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Decompression time");
        for (DecompressionStatistics decompressionStatistics : decompressionStatisticsList) {
            series.getData().add(new XYChart.Data(decompressionStatistics.getCompressMethod().toString(), decompressionStatistics.getDecompressionTimeInSeconds()));
        }

        decompressionTimeBarChart.getXAxis().setAnimated(false);
        decompressionTimeBarChart.getData().addAll(series);
    }

    public void setUpDecompressionSpeedChart() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Decompression speed");
        for (DecompressionStatistics decompressionStatistics : decompressionStatisticsList) {
            series.getData().add(new XYChart.Data(decompressionStatistics.getCompressMethod().toString(), decompressionStatistics.getDecompressionSpeedInMBPS()));
        }

        decompressionSpeedBarChart.getXAxis().setAnimated(false);
        decompressionSpeedBarChart.getData().addAll(series);
    }*/
}
