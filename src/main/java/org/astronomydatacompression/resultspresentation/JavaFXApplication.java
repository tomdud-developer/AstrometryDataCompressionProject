package org.astronomydatacompression.resultspresentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.astronomydatacompression.compression.CompressMethod;
import org.astronomydatacompression.statistics.CompressionStatistics;
import org.astronomydatacompression.statistics.SessionStatistics;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class JavaFXApplication extends Application {

    private SessionStatistics sessionStatistics;
    private JavaFxController controller;

    @Override
    public void start(Stage stage) throws IOException {
/*        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Compression time");
        xAxis.setLabel("Compressor");
        yAxis.setLabel("Time, s");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Compression time");
        // for (CompressionStatistics compressionStatistics : sessionStatistics.getCompressionStatistics()) {
        //   series1.getData().add(new XYChart.Data(compressionStatistics.getCompressMethod(), compressionStatistics.getCompressionTimeInSeconds()));
        // }

        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series1);
        stage.setScene(scene);
        stage.setTitle("Compressors comparison");
        stage.show();*/
        FXMLLoader loader = new FXMLLoader(getClass().getResource("presentation.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.updateStats(new CompressionStatistics(CompressMethod.M03, new File("notes.txt"), 12L,  new File("notes.txt")));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
