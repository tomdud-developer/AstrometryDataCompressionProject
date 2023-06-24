package org.astronomydatacompression.resultspresentation;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JavaFXApplication extends Application {

    @Override public void start(Stage stage) {

        Group root = new Group();
        Scene scene = new Scene(root, 200, 150);
        //scene.setFill(Paint.);

        Circle circle = new Circle(60, 40, 30, Paint.valueOf("orange"));

        Text text = new Text(10, 90, "JavaFX Scene");
        //text.setFill(Color.DARKRED);

        javafx.scene.text.Font font = new javafx.scene.text.Font(20);
        text.setFont(font);

        root.getChildren().add(circle);
        root.getChildren().add(text);
        stage.setScene(scene);
        stage.show();
    }

    public void runner() {
        launch();
    }

}
