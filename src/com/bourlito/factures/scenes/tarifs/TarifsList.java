package com.bourlito.factures.scenes.tarifs;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Tarif;
import com.bourlito.factures.scenes.IView;
import com.bourlito.factures.scenes.client.ClientDetails;
import com.bourlito.factures.scenes.utils.CScene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class TarifsList implements IView {

    private Stage stage;
    private Client client;

    public TarifsList(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;
    }

    @Override
    public CScene getScene() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(event -> {
            stage.setScene(new ClientDetails(stage, client).getScene());
        });

        Button btnNew = new Button("Nouveau");
        btnNew.setOnAction(e -> {
            stage.setScene(new TarifsDetails(stage, client).getScene());
        });

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10, 0, 0, 10));
        bbar.getButtons().addAll(btnRetour, btnNew);
        root.setBottom(bbar);

        if (client != null){
            FlowPane pane = new FlowPane();
            pane.setHgap(20);
            pane.setVgap(10);
            pane.setPadding(new Insets(25, 25, 25, 25));

            for (Tarif tarif: client.getTarifs()) {
                Button title = new Button(tarif.getNom());
                title.setOnAction(event -> {
                    stage.setScene(new TarifsDetails(stage, client, tarif).getScene());
                });
                pane.getChildren().addAll(title);
            }

            root.setCenter(pane);
        }

        return new CScene(root);
    }
}
