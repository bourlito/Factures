package com.bourlito.factures.scenes.tarif;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Tarif;
import com.bourlito.factures.scenes.client.ClientDetails;
import com.bourlito.factures.scenes.utils.CScene;
import com.bourlito.factures.utils.Constants;
import com.bourlito.factures.utils.Keys;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TarifsList {

    private final Stage stage;
    private final Client client;

    /**
     * constructeur
     * @param stage a utiliser
     * @param client associe
     */
    public TarifsList(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;
    }

    /**
     * @return la scene
     */
    public CScene getScene() {

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        // Always show vertical scroll bar
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Horizontal scroll bar is only displayed when needed
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(0));

        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(event -> stage.setScene(new ClientDetails(stage, client).getScene()));

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10));
        bbar.getButtons().addAll(btnRetour);
        root.setBottom(bbar);

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setHgap(Constants.HGAP);
        pane.setVgap(Constants.VGAP);
        pane.setPadding(new Insets(Constants.PADDING));

        Label lColonne = new Label(Keys.COLONNE);
        pane.add(lColonne,0,0);

        Label lNom = new Label(Keys.NOM);
        pane.add(lNom,1,0);

        Label lPrix = new Label(Keys.PRIX);
        pane.add(lPrix,2,0);

        int i = 1;

        for (Tarif tarif: client.getTarifs()) {

            Label lCol = new Label(tarif.getColonne());
            pane.add(lCol, 0,i);

            TextField tNom = new TextField();
            tNom.setText(tarif.getNom());
            pane.add(tNom,1,i);
            tNom.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) tarif.setNom(tNom.textProperty().getValue());
            });

            TextField tPrix = new TextField();
            tPrix.setText(String.valueOf(tarif.getPrix()));
            pane.add(tPrix,2,i);
            tPrix.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) tarif.setPrix(Double.parseDouble(tPrix.textProperty().getValue()));
            });

            i++;
        }

        scrollPane.setContent(pane);
        root.setCenter(scrollPane);

        return new CScene(root);
    }
}
