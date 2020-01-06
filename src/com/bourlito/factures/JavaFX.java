package com.bourlito.factures;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.service.SClient;
import javafx.application.Application;
import javafx.stage.Stage;
import com.bourlito.factures.scenes.Main;

public class JavaFX extends Application {

    public static final String STYLE = JavaFX.class.getResource("res/javafx.css").toExternalForm();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Factures");
        stage.setScene(new Main(stage).getScene());
        stage.show();
    }
}