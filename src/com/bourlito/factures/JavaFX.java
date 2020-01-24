package com.bourlito.factures;

import com.bourlito.factures.scenes.Main;
import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFX extends Application {

    public static final String STYLE = JavaFX.class.getResource("res/javafx.css").toExternalForm();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Factures");
        stage.setScene(new Main(stage).getScene());
        stage.show();
    }
}