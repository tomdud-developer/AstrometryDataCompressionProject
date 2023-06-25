package org.astronomydatacompression.resultspresentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.astronomydatacompression.statistics.SessionStatistics;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class JavaFXApplication extends Application {

    private JavaFxController controller;
    public static JavaFXApplication INSTANCE = null;
    public static final CountDownLatch latch = new CountDownLatch(3);
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
        latch.countDown();

        scene.getStylesheets().add("style.css");
        stage.setTitle("Compressors comparison");
        stage.setScene(scene);
        stage.show();
    }

    public void sendStatistcsToShow(List<SessionStatistics> sessionStatisticsList) {
        controller.updateStats(sessionStatisticsList);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
