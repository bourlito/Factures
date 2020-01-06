package com.bourlito.factures.utils;

import com.bourlito.factures.scenes.utils.CScene;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class Erreur {

    public static void creerFichierErreur(String erreur) {

        erreurWindow(erreur).show();

        try {
            FileWriter writer = new FileWriter(MotsCles.DOSSIER + "erreur.txt");
            writer.write(erreur);
            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static Stage erreurWindow(String erreur){

        BorderPane pane = new BorderPane();
        Label label = new Label(erreur);
        pane.setCenter(label);

        Scene scene = new CScene(pane, 800, 166);

        Stage newWindow = new Stage();
        newWindow.setTitle("Erreur");
        newWindow.setScene(scene);

        return newWindow;
    }
}
