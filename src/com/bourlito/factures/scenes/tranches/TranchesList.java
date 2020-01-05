package com.bourlito.factures.scenes.tranches;

import com.bourlito.factures.Keys;
import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Tranche;
import com.bourlito.factures.scenes.IView;
import com.bourlito.factures.scenes.client.ClientDetails;
import com.bourlito.factures.scenes.tarifs.TarifsDetails;
import com.bourlito.factures.scenes.utils.CScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TranchesList implements IView {

    private Stage stage;
    private Client client;

    private TextField tLibelle;

    public TranchesList(Stage stage, Client client) {
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
            stage.setScene(new TranchesDetails(stage, client).getScene());
        });

        Button btnValider = new Button("Valider");
        btnValider.setId("btnValider");
        btnValider.setOnAction(e -> {
            String libelleTranches = tLibelle.getText();
            client.setLibelleTranches(libelleTranches);

            stage.setScene(new ClientDetails(stage, client).getScene());
        });

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10, 0, 0, 10));
        bbar.getButtons().addAll(btnRetour, btnNew, btnValider);
        root.setBottom(bbar);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);

        Label lLibelle = new Label(Keys.LIBELLE_TRANCHE);

        tLibelle = new TextField();
        tLibelle.setMaxWidth(200);
        tLibelle.setText(client != null ? client.getLibelleTranches() : "");

        hBox.getChildren().addAll(lLibelle, tLibelle);
        root.setTop(hBox);

        if (client != null){
            FlowPane pane = new FlowPane();
            pane.setHgap(20);
            pane.setVgap(10);
            pane.setPadding(new Insets(25, 25, 25, 25));

            for (Tranche tranche: client.getTranches()) {
                Button title = new Button(String.valueOf(tranche.getMin()));
                title.setOnAction(event -> {
                    stage.setScene(new TranchesDetails(stage, client, tranche).getScene());
                });
                pane.getChildren().addAll(title);
            }

            root.setCenter(pane);
        }

        return new CScene(root);
    }
}
