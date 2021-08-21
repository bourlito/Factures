package com.bourlito.factures.scenes.tranche;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Tranche;
import com.bourlito.factures.scenes.client.ClientDetails;
import com.bourlito.factures.scenes.utils.CScene;
import com.bourlito.factures.utils.Constants;
import com.bourlito.factures.utils.Keys;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TranchesList {

    private final Stage stage;
    private final Client client;

    private TextField tLibelle;

    /**
     * constructeur
     * @param stage a utiliser
     * @param client associe
     */
    public TranchesList(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;
    }

    /**
     * @return la scene
     */
    public CScene getScene() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(event -> {
            String libelleTranches = tLibelle.getText();
            client.setLibelleTranches(libelleTranches);

            stage.setScene(new ClientDetails(stage, client).getScene());
        });

        Button btnNew = new Button("Nouveau");
        btnNew.setOnAction(e -> stage.setScene(new TranchesDetails(stage, client).getScene()));

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10, 0, 0, 10));
        bbar.getButtons().addAll(btnRetour);//, btnNew);
        root.setBottom(bbar);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);

        Label lLibelle = new Label(Keys.LIBELLE_TRANCHE);

        tLibelle = new TextField();
        tLibelle.setMaxWidth(Constants.MAX_WIDTH);
        tLibelle.setText(client != null ? client.getLibelleTranches() : Constants.LIBELLE_LI);

        hBox.getChildren().addAll(lLibelle, tLibelle);
        root.setTop(hBox);

        // FlowPane pane = new FlowPane();
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setHgap(Constants.HGAP);
        pane.setVgap(Constants.VGAP);
        pane.setPadding(new Insets(Constants.PADDING));

        if (client != null) {
            Label lMin = new Label(Keys.MIN);
            pane.add(lMin,0,0);

            Label lPrix = new Label(Keys.PRIX);
            pane.add(lPrix,0,1);

            int i = 1;

            for (Tranche tranche : client.getTranches()) {
                /*
                Button title = new Button(String.valueOf(tranche.getMin()));
                title.setOnAction(event -> stage.setScene(new TranchesDetails(stage, client, tranche).getScene()));
                pane.getChildren().addAll(title);
                 */

                TextField tMin = new TextField();
                tMin.setMaxWidth(Constants.MAX_WIDTH);
                tMin.setText(String.valueOf(tranche.getMin()));
                pane.add(tMin, 0, i);
                tMin.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) tranche.setMin(Integer.parseInt(tMin.textProperty().getValue()));
                });

                TextField tPrice = new TextField();
                tPrice.setMaxWidth(Constants.MAX_WIDTH);
                tPrice.setText(String.valueOf(tranche.getPrix()));
                pane.add(tPrice, 1, i);
                tPrice.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) tranche.setPrix(Double.parseDouble(tPrice.textProperty().getValue()));
                });

                Button btnDel = new Button("-");
                btnDel.setMaxWidth(40);
                btnDel.setId("btnSupprimer");
                pane.add(btnDel, 2, i);

                btnDel.setOnAction(event -> {
                    client.getTranches().remove(tranche);
                    stage.setScene(getScene());
                });

                i++;
            }

            Button btnVal = new Button("+");
            btnVal.setMaxWidth(40);
            btnVal.setId("btnValider");
            pane.add(btnVal, 2, i);

            btnVal.setOnAction(event -> {
                Tranche tranche = new Tranche();
                client.getTranches().add(tranche);
                stage.setScene(getScene());
            });
        }

        root.setCenter(pane);
        return new CScene(root);
    }
}
