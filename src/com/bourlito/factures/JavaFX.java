package com.bourlito.factures;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.bourlito.factures.scenes.Main;

public class JavaFX extends Application {

    public static final String STYLE = JavaFX.class.getResource("res/javafx.css").toExternalForm();

    @Override
    public void start(Stage stage) {

        Scene scene = new Main(stage).getScene();
        scene.getStylesheets().add(JavaFX.STYLE);
        stage.setScene(scene);

        stage.setTitle("Factures");
        stage.setScene(scene);
        stage.show();
    }
}