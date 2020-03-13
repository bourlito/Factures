package com.bourlito.factures.scenes.tarif;

import com.bourlito.factures.utils.Constants;
import com.bourlito.factures.utils.Keys;
import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Tarif;
import com.bourlito.factures.scenes.utils.CScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TarifsDetails {

    private final Stage stage;
    private final Client client;
    private Tarif tarif;

    private TextField tNom;
    private TextField tPrix;

    /**
     * constructeur
     * @param stage a utiliser
     * @param client associe
     * @param tarif a utiliser
     */
    public TarifsDetails(Stage stage, Client client, Tarif tarif) {
        this.stage = stage;
        this.client = client;
        this.tarif = tarif;
    }

    /**
     * @return la scene
     */
    public CScene getScene() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button btnAnnuler = new Button("Annuler");
        btnAnnuler.setOnAction(event -> stage.setScene(new TarifsList(stage, client).getScene()));

        Button btnValider = new Button("Valider");
        btnValider.setId("btnValider");
        btnValider.setOnAction(e -> {
            if (tarif != null){
                client.getTarifs().remove(tarif);
            }

            else tarif = new Tarif();

            tarif.setNom(tNom.getText());
            tarif.setPrix(Double.parseDouble(tPrix.getText()));

            client.getTarifs().add(tarif);

            stage.setScene(new TarifsList(stage, client).getScene());
        });

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10, 0, 0, 10));
        bbar.getButtons().addAll(btnAnnuler, btnValider);
        root.setBottom(bbar);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(Constants.HGAP);
        grid.setVgap(Constants.VGAP);
        grid.setPadding(new Insets(Constants.PADDING));

        Label lColonne = new Label(Keys.COLONNE);
        grid.add(lColonne,0,0);

        Label lCol = new Label(tarif.getColonne());
        grid.add(lCol,1,0);

        Label lNom = new Label(Keys.NOM);
        grid.add(lNom,0,1);

        tNom = new TextField();
        tNom.setText(tarif.getNom());
        grid.add(tNom,1,1);

        Label lPrix = new Label(Keys.PRIX);
        grid.add(lPrix,0,2);

        tPrix = new TextField();
        tPrix.setText(String.valueOf(tarif.getPrix()));
        grid.add(tPrix,1,2);

        root.setCenter(grid);

        return new CScene(root);
    }
}
