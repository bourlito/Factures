package com.bourlito.factures.scenes;

import com.bourlito.factures.JavaFX;
import com.bourlito.factures.Keys;
import com.bourlito.factures.dto.Client;
import com.bourlito.factures.service.SClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ClientDetails implements IView {

    private Stage stage;
    private Client client;
    private TextField tAlias, tNom, tAdresse, tCp, tVille;

    public ClientDetails(Stage stage) {
        this.stage = stage;
    }

    public ClientDetails(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;
    }

    @Override
    public Scene getScene() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setId("btnSupprimer");
        btnSupprimer.setOnAction(event -> {
            SClient.getInstance().deleteClient(client);

            Scene scene = new ClientList(stage).getScene();
            scene.getStylesheets().add(JavaFX.STYLE);
            stage.setScene(scene);
        });

        Button btnAnnuler = new Button("Annuler");
        btnAnnuler.setOnAction(event -> {
            Scene scene = new ClientList(stage).getScene();
            scene.getStylesheets().add(JavaFX.STYLE);
            stage.setScene(scene);
        });

        Button btnValider = new Button("Valider");
        btnValider.setId("btnValider");
        btnValider.setOnAction(e -> {
            if (client != null)
                SClient.getInstance().deleteClient(client);
            SClient.getInstance().addClient(newClient());

            Scene scene = new ClientList(stage).getScene();
            scene.getStylesheets().add(JavaFX.STYLE);
            stage.setScene(scene);
        });

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10, 0, 0, 10));
        if (client != null)
            bbar.getButtons().addAll(btnSupprimer, btnAnnuler, btnValider);
        else bbar.getButtons().addAll(btnAnnuler, btnValider);
        root.setBottom(bbar);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label lAlias = new Label(Keys.ALIAS);
        grid.add(lAlias, 0, 0);
        tAlias = new TextField();
        tAlias.setMaxWidth(200);
        tAlias.setText(client != null ? client.getAlias() : "");
        grid.add(tAlias, 1, 0);

        Label lNom = new Label(Keys.NOM);
        grid.add(lNom, 0, 1);
        tNom = new TextField();
        tNom.setMaxWidth(200);
        tNom.setText(client != null ? client.getNom() : "");
        grid.add(tNom, 1, 1);

        Label lAdresse = new Label(Keys.ADRESSE);
        grid.add(lAdresse, 0, 2);
        tAdresse = new TextField();
        tAdresse.setMaxWidth(200);
        tAdresse.setText(client != null ? client.getAdresse() : "");
        grid.add(tAdresse, 1, 2);

        Label lCp = new Label(Keys.CP);
        grid.add(lCp, 0, 3);
        tCp = new TextField();
        tCp.setMaxWidth(200);
        tCp.setText(client != null ? client.getCp() : "");
        grid.add(tCp, 1, 3);

        Label lVille = new Label(Keys.VILLE);
        grid.add(lVille, 0, 4);
        tVille = new TextField();
        tVille.setMaxWidth(200);
        tVille.setText(client != null ? client.getVille() : "");
        grid.add(tVille, 1, 4);

        Button btnTranches = new Button("Tranches");
        btnTranches.setMinWidth(200);
        grid.add(btnTranches, 0, 5);

        Button btnTarifs = new Button("Tarifs");
        btnTarifs.setMinWidth(200);
        grid.add(btnTarifs, 1, 5);

        root.setCenter(grid);

        return new Scene(root, 800, 500);
    }

    @NotNull
    private Client newClient(){

        String alias = tAlias.getText();
        String nom = tNom.getText();
        String adresse = tAdresse.getText();
        String cp = tCp.getText();
        String ville = tVille.getText();

        return new Client(alias, nom, adresse, cp, ville, "", new ArrayList<>(), new ArrayList<>());
    }
}
