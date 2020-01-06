package com.bourlito.factures.scenes.tarif;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Tarif;
import com.bourlito.factures.scenes.IView;
import com.bourlito.factures.scenes.client.ClientDetails;
import com.bourlito.factures.scenes.utils.CScene;
import com.bourlito.factures.service.STarif;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

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

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10, 0, 0, 10));
        bbar.getButtons().addAll(btnRetour);
        root.setBottom(bbar);

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(20);
        pane.setVgap(10);
        pane.setPadding(new Insets(25, 25, 25, 25));

        int i = 0, j = 0;
        for (Tarif tarif: client.getTarifs()) {

            Button btn = new Button(tarif.getNom());
            btn.setMinWidth(200);
            pane.add(btn, j, i);

            btn.setOnAction(event -> {
                stage.setScene(new TarifsDetails(stage, client, tarif).getScene());
            });

            if (j == 2){
                i++;
                j = 0;
            }
            else j++;
        }

        root.setCenter(pane);

        return new CScene(root);
    }
}
