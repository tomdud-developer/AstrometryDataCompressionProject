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
    public static JavaFXApplication INSTANCE = null;
    public static final CountDownLatch latch = new CountDownLatch(1);
    public static JavaFXApplication waitForInstance() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return INSTANCE;
    }

    public static void setINSTANCE(JavaFXApplication javaFXApplication) {
        INSTANCE = javaFXApplication;
        latch.countDown();
    }

    public JavaFXApplication() {
        setINSTANCE(this);
    }

    @Override
    public void start(Stage stage) throws IOException {
        INSTANCE = this;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("presentation.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        controller = fxmlLoader.getController();

        controller.updateStats(null);
        //scene.getStylesheets().add("style.css");
        stage.setTitle("Compressors comparison");
        stage.setScene(scene);
        stage.show();
    }

    public void test() {
        System.out.println("Invoked!!!!!!!!!");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
