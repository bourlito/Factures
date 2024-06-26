package com.bourlito.factures.scenes.client;

import com.bourlito.factures.utils.Constants;
import com.bourlito.factures.utils.Keys;
import com.bourlito.factures.dto.Client;
import com.bourlito.factures.scenes.tarif.TarifsList;
import com.bourlito.factures.scenes.tranche.TranchesList;
import com.bourlito.factures.scenes.utils.CScene;
import com.bourlito.factures.service.SClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class ClientDetails {

    private final Stage stage;
    private final Client client;
    private Client tmp;
    private TextField tAlias, tNom, tAdresse, tCp, tVille;

    /**
     * constructeur
     * @param stage a utiliser
     * @param client associe
     */
    public ClientDetails(Stage stage, Client client) {
        this.stage = stage;
        this.client = SClient.getInstance().getClientByAlias(client.getAlias());
        this.tmp = new Client(client);
    }

    /**
     * @return la scene
     */
    public CScene getScene() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setId("btnSupprimer");
        btnSupprimer.setOnAction(event -> {
            SClient.getInstance().deleteClient(client);

            stage.setScene(new ClientList(stage).getScene());
        });

        Button btnAnnuler = new Button("Annuler");
        btnAnnuler.setOnAction(event -> stage.setScene(new ClientList(stage).getScene()));

        Button btnValider = new Button("Valider");
        btnValider.setId("btnValider");
        btnValider.setOnAction(e -> {
            if (client != null)
                SClient.getInstance().deleteClient(client);
            SClient.getInstance().addClient(newClient());

            stage.setScene(new ClientList(stage).getScene());
        });

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10, 0, 0, 10));
        if (tmp != null)
            bbar.getButtons().addAll(btnSupprimer, btnAnnuler, btnValider);
        else bbar.getButtons().addAll(btnAnnuler, btnValider);
        root.setBottom(bbar);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(Constants.HGAP);
        grid.setVgap(Constants.VGAP);
        grid.setPadding(new Insets(Constants.PADDING));

        Label lAlias = new Label(Keys.ALIAS);
        grid.add(lAlias, 0, 0);
        tAlias = new TextField();
        tAlias.setMaxWidth(Constants.MAX_WIDTH);
        tAlias.setText(tmp != null ? tmp.getAlias() : "");
        grid.add(tAlias, 1, 0);

        Label lNom = new Label(Keys.NOM);
        grid.add(lNom, 0, 1);
        tNom = new TextField();
        tNom.setMaxWidth(Constants.MAX_WIDTH);
        tNom.setText(tmp != null ? tmp.getNom() : "");
        grid.add(tNom, 1, 1);

        Label lAdresse = new Label(Keys.ADRESSE);
        grid.add(lAdresse, 0, 2);
        tAdresse = new TextField();
        tAdresse.setMaxWidth(Constants.MAX_WIDTH);
        tAdresse.setText(tmp != null ? tmp.getAdresse() : "");
        grid.add(tAdresse, 1, 2);

        Label lCp = new Label(Keys.CP);
        grid.add(lCp, 0, 3);
        tCp = new TextField();
        tCp.setMaxWidth(Constants.MAX_WIDTH);
        tCp.setText(tmp != null ? tmp.getCp() : "");
        grid.add(tCp, 1, 3);

        Label lVille = new Label(Keys.VILLE);
        grid.add(lVille, 0, 4);
        tVille = new TextField();
        tVille.setMaxWidth(Constants.MAX_WIDTH);
        tVille.setText(tmp != null ? tmp.getVille() : "");
        grid.add(tVille, 1, 4);

        Label lTranches = new Label(Keys.TRANCHES);
        grid.add(lTranches, 0, 5);

        int nbTranches = tmp != null ? tmp.getTranches().size() : 0;
        Button btnTranches = new Button(String.valueOf(nbTranches));
        btnTranches.setMaxWidth(Constants.MAX_WIDTH);
        btnTranches.setOnAction(event -> stage.setScene(new TranchesList(stage, tmp).getScene()));
        grid.add(btnTranches, 1, 5);

        Label lTarifs = new Label(Keys.TARIFS);
        grid.add(lTarifs, 0, 6);

        int nbTarifs = tmp != null ? tmp.getTarifs().size() : 0;
        Button btnTarifs = new Button(String.valueOf(nbTarifs));
        btnTarifs.setMaxWidth(Constants.MAX_WIDTH);
        btnTarifs.setOnAction(event -> stage.setScene(new TarifsList(stage, tmp).getScene()));
        grid.add(btnTarifs, 1, 6);

        root.setCenter(grid);

        return new CScene(root);
    }

    /**
     * methode de remplissage d'un nouveau client
     * @return le client cree
     */
    
    private Client newClient(){

        String alias = tAlias.getText();
        String nom = tNom.getText();
        String adresse = tAdresse.getText();
        String cp = tCp.getText();
        String ville = tVille.getText();

        return new Client(alias, nom, adresse, cp, ville, tmp.getLibelleTranches(), tmp.getTranches(), tmp.getTarifs());
    }
}
