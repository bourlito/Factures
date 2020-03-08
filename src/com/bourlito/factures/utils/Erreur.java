package com.bourlito.factures.utils;

import com.bourlito.factures.scenes.utils.CScene;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class Erreur {

    /**
     * methode de creation d'un fichier d'erreur
     * @param erreur le message d'erreur comprehensible par un humain
     */
    public static void creerFichierErreur(String erreur) {

        erreurWindow(erreur).show();

        try {
            FileWriter writer = new FileWriter(Constants.DOSSIER + "erreur.txt");
            writer.write(erreur);
            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * methode de creation d'une fenetre contenant l'erreur qui s'est produite
     * @param erreur le message d'erreur
     * @return la fenetre creee
     */
    private static Stage erreurWindow(String erreur){

        BorderPane pane = new BorderPane();
        Label label = new Label(erreur);
        pane.setCenter(label);

        Scene scene = new CScene(pane, Constants.PANE_WIDTH, Constants.PANE_HEIGHT_TIERS);

        Stage newWindow = new Stage();
        newWindow.setTitle("Erreur");
        newWindow.setScene(scene);

        return newWindow;
    }
}
