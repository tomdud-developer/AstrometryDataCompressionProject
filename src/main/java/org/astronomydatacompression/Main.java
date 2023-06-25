package org.astronomydatacompression;


import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.astronomydatacompression.session.Flow;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Thread flowThread = new Thread(new Flow());
        flowThread.start();

    }
}


