package com.bourlito.factures.scenes.tarif;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Tarif;
import com.bourlito.factures.scenes.IView;
import com.bourlito.factures.scenes.client.ClientDetails;
import com.bourlito.factures.scenes.utils.CScene;
import com.bourlito.factures.utils.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TarifsList implements IView {

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

    @Override
    public CScene getScene() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(event -> stage.setScene(new ClientDetails(stage, client).getScene()));

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10, 0, 0, 10));
        bbar.getButtons().addAll(btnRetour);
        root.setBottom(bbar);

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(Constants.HGAP);
        pane.setVgap(Constants.VGAP);
        pane.setPadding(new Insets(Constants.PADDING));

        int i = 0, j = 0;
        for (Tarif tarif: client.getTarifs()) {

            Button btn = new Button(tarif.getNom());
            btn.setMinWidth(200);
            pane.add(btn, j, i);

            btn.setOnAction(event -> stage.setScene(new TarifsDetails(stage, client, tarif).getScene()));

            if (j == 1){
                i++;
                j = 0;
            }
            else j++;
        }

        root.setCenter(pane);

        return new CScene(root);
    }
}
