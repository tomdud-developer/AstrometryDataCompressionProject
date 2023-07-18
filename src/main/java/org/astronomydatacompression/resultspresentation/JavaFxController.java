package org.astronomydatacompression.resultspresentation;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import org.astronomydatacompression.statistics.CompressionStatistics;
import org.astronomydatacompression.statistics.DecompressionStatistics;
import org.astronomydatacompression.statistics.SessionStatistics;
import org.astronomydatacompression.statistics.StatisticsCSVPrinter;

import java.io.File;
import java.util.List;
import java.util.function.Function;

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
    private BarChart<String, Number> modificationAndCompressionTimeBarChart;
    @FXML
    private BarChart<String, Number> reversalAndDecompressionTimeBarChart;
    @FXML
    private BarChart<String, Number> compressionAndDecompressionTimeBarChart;
    @FXML
    private BarChart<String, Number> modificationAndCompressionAndDecompressionAndReversalTimeBarChart;
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
        setUpStatisticsCharts();
    }

    private void setUpInputFileInformationListView() {
        ObservableList<String> informationsAboutFiles = FXCollections.observableArrayList();

        for (SessionStatistics sessionStatistics : sessionStatisticsList) {
            StringBuilder modifiersListStringBuilder = new StringBuilder();
            sessionStatistics.getModificationStatistics().getModifierList().forEach(
                    s -> modifiersListStringBuilder.append(s.getShortName()).append(",")
            );
            informationsAboutFiles.addAll(
                    "Name: " + sessionStatistics.getOriginalFile().getName(),
                    "Extension: " + sessionStatistics.getOriginalFile().getName().split("\\.")[1],
                    "Size: " + sessionStatistics.getOriginalFileSizeInMB() + " MB",
                    "Modifiers: " + modifiersListStringBuilder,
                    "______________________________"
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

    public void setUpStatisticsCharts() {
        updateCompressionChart(compressionTimeBarChart, CompressionStatistics::getCompressionTimeInSeconds);
        updateCompressionChart(compressionSpeedBarChart, CompressionStatistics::getCompressionSpeedInMBPS);
        updateCompressionChart(compressionRatioBarChart, CompressionStatistics::getCompressionRatio);
        updateCompressionChart(compressedFileSizesBarChart, CompressionStatistics::getOutputSizeInMB);
        updateDecompressionChart(decompressionTimeBarChart, DecompressionStatistics::getDecompressionTimeInSeconds);
        updateDecompressionChart(decompressionSpeedBarChart, DecompressionStatistics::getDecompressionSpeedInMBPS);
        updateModificationAndCompressionChart(modificationAndCompressionTimeBarChart);
        updateReversalAndDecompressionChart(reversalAndDecompressionTimeBarChart);
        updateCompressionAndDecompressionTimeBarChart(compressionAndDecompressionTimeBarChart);
        updateModificationAndCompressionAndDecompressionAndReversalTimeBarChart(modificationAndCompressionAndDecompressionAndReversalTimeBarChart);
    }

    public void updateCompressionChart(BarChart<String, Number> barChart, Function<CompressionStatistics, Double> function) {
        for (SessionStatistics sessionStatistics : sessionStatisticsList) {
            XYChart.Series series = new XYChart.Series();
            series.setName(sessionStatistics.getOriginalFile().getName());
            for (CompressionStatistics compressionStatistics : sessionStatistics.getCompressionStatistics()) {
                series.getData().add(new XYChart.Data(compressionStatistics.getCompressMethod().toString(), function.apply(compressionStatistics)));
            }

            barChart.getXAxis().setAnimated(false);
            barChart.getData().add(series);
        }
    }

    public void updateDecompressionChart(BarChart<String, Number> barChart, Function<DecompressionStatistics, Double> function) {
        for (SessionStatistics sessionStatistics : sessionStatisticsList) {
            XYChart.Series series = new XYChart.Series();
            series.setName(sessionStatistics.getOriginalFile().getName());
            for (DecompressionStatistics decompressionStatistics : sessionStatistics.getDecompressionStatistics()) {
                series.getData().add(new XYChart.Data(decompressionStatistics.getCompressMethod().toString(), function.apply(decompressionStatistics)));
            }

            barChart.getXAxis().setAnimated(false);
            barChart.getData().add(series);
        }
    }

    public void updateModificationAndCompressionChart(BarChart<String, Number> barChart) {
        for (SessionStatistics sessionStatistics : sessionStatisticsList) {
            XYChart.Series series = new XYChart.Series();
            series.setName(sessionStatistics.getOriginalFile().getName());
            for (CompressionStatistics compressionStatistics : sessionStatistics.getCompressionStatistics()) {
                series.getData().add(
                        new XYChart.Data(
                                compressionStatistics.getCompressMethod().toString(),
                                compressionStatistics.getCompressionTimeInSeconds() +
                                        sessionStatistics.getModificationStatistics().getModificationTimeInSeconds()
                        )
                );
            }

            barChart.getXAxis().setAnimated(false);
            barChart.getData().add(series);
        }
    }

    public void updateReversalAndDecompressionChart(BarChart<String, Number> barChart) {
        for (SessionStatistics sessionStatistics : sessionStatisticsList) {
            XYChart.Series series = new XYChart.Series();
            series.setName(sessionStatistics.getOriginalFile().getName());
            for (DecompressionStatistics decompressionStatistics : sessionStatistics.getDecompressionStatistics()) {
                series.getData().add(
                        new XYChart.Data(
                                decompressionStatistics.getCompressMethod().toString(),
                                decompressionStatistics.getDecompressionTimeInSeconds() +
                                        sessionStatistics.getModificationStatistics().getReversalTimeInSeconds()
                        )
                );
            }

            barChart.getXAxis().setAnimated(false);
            barChart.getData().add(series);
        }
    }

    public void updateCompressionAndDecompressionTimeBarChart(BarChart<String, Number> barChart) {
        for (SessionStatistics sessionStatistics : sessionStatisticsList) {
            XYChart.Series series = new XYChart.Series();
            series.setName(sessionStatistics.getOriginalFile().getName());
            for (int i = 0; i < sessionStatistics.getCompressionStatistics().size(); i++) {
                series.getData().add(
                        new XYChart.Data(
                                sessionStatistics.getCompressionStatistics().get(i).getCompressMethod().toString(),
                                sessionStatistics.getCompressionStatistics().get(i).getCompressionTimeInSeconds() +
                                sessionStatistics.getDecompressionStatistics().get(i).getDecompressionTimeInSeconds()
                        )
                );
            }

            barChart.getXAxis().setAnimated(false);
            barChart.getData().add(series);
        }
    }

    public void updateModificationAndCompressionAndDecompressionAndReversalTimeBarChart(BarChart<String, Number> barChart) {
        for (SessionStatistics sessionStatistics : sessionStatisticsList) {
            XYChart.Series series = new XYChart.Series();
            series.setName(sessionStatistics.getOriginalFile().getName());
            for (int i = 0; i < sessionStatistics.getCompressionStatistics().size(); i++) {
                series.getData().add(
                        new XYChart.Data(
                                sessionStatistics.getCompressionStatistics().get(i).getCompressMethod().toString(),
                                sessionStatistics.getCompressionStatistics().get(i).getCompressionTimeInSeconds() +
                                sessionStatistics.getDecompressionStatistics().get(i).getDecompressionTimeInSeconds() +
                                        sessionStatistics.getModificationStatistics().getModificationTimeInSeconds() +
                                        sessionStatistics.getModificationStatistics().getReversalTimeInSeconds()

                        )
                );
            }

            barChart.getXAxis().setAnimated(false);
            barChart.getData().add(series);
        }
    }

    public void saveStatisticsButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save statistics");
        fileChooser.setInitialFileName("statistics");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            StatisticsCSVPrinter statisticsCSVPrinter = new StatisticsCSVPrinter(sessionStatisticsList);
            statisticsCSVPrinter.saveToPath(selectedFile.toPath());
        }
    }
}
