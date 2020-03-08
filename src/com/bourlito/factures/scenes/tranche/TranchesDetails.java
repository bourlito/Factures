package com.bourlito.factures.scenes.tranche;

import com.bourlito.factures.utils.Constants;
import com.bourlito.factures.utils.Keys;
import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Tranche;
import com.bourlito.factures.scenes.IView;
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

public class TranchesDetails implements IView {

    private final Stage stage;
    private final Client client;
    private Tranche tranche;

    private TextField tMin, tPrix;

    public TranchesDetails(Stage stage, Client client) {
        this.stage = stage;
        this.client = client;
    }

    public TranchesDetails(Stage stage, Client client, Tranche tranche) {
        this.stage = stage;
        this.client = client;
        this.tranche = tranche;
    }

    @Override
    public CScene getScene() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setId("btnSupprimer");
        btnSupprimer.setOnAction(event -> {
            client.getTranches().remove(tranche);

            stage.setScene(new TranchesList(stage, client).getScene());
        });

        Button btnAnnuler = new Button("Annuler");
        btnAnnuler.setOnAction(event -> stage.setScene(new TranchesList(stage, client).getScene()));

        Button btnValider = new Button("Valider");
        btnValider.setId("btnValider");
        btnValider.setOnAction(e -> {
            if (tranche != null){
                client.getTranches().remove(tranche);
            }

            else tranche = new Tranche();

            tranche.setMin(Integer.parseInt(tMin.getText()));
            tranche.setPrix(Double.parseDouble(tPrix.getText()));

            client.getTranches().add(tranche);

            stage.setScene(new TranchesList(stage, client).getScene());
        });

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10, 0, 0, 10));
        if (tranche != null)
            bbar.getButtons().addAll(btnSupprimer, btnAnnuler, btnValider);
        else bbar.getButtons().addAll(btnAnnuler, btnValider);
        root.setBottom(bbar);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(Constants.HGAP);
        grid.setVgap(Constants.VGAP);
        grid.setPadding(new Insets(Constants.PADDING));

        Label lMin = new Label(Keys.MIN);
        grid.add(lMin,0,0);

        tMin = new TextField();
        tMin.setText(tranche != null ? String.valueOf(tranche.getMin()) : "");
        grid.add(tMin,1,0);

        Label lPrix = new Label(Keys.PRIX);
        grid.add(lPrix,0,1);

        tPrix = new TextField();
        tPrix.setText(tranche != null ? String.valueOf(tranche.getPrix()) : "");
        grid.add(tPrix,1,1);

        root.setCenter(grid);

        return new CScene(root);
    }
}
