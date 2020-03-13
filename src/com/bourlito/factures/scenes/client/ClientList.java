package com.bourlito.factures.scenes.client;

import com.bourlito.factures.utils.Constants;
import com.bourlito.factures.dto.Client;
import com.bourlito.factures.scenes.Main;
import com.bourlito.factures.scenes.utils.CScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.bourlito.factures.service.SClient;

public class ClientList {

    private final Stage stage;
    private final SClient sClient = SClient.getInstance();

    /**
     * constructeur
     * @param stage a utiliser
     */
    public ClientList(Stage stage) {
        this.stage = stage;
    }

    /**
     * @return la scene
     */
    public CScene getScene() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(event -> stage.setScene(new Main(stage).getScene()));

        Button btnNew = new Button("Nouveau");
        btnNew.setOnAction(e -> stage.setScene(new ClientDetails(stage, new Client()).getScene()));

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10, 0, 0, 10));
        bbar.getButtons().addAll(btnRetour, btnNew);
        root.setBottom(bbar);

        // clients
        FlowPane pane = new FlowPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(Constants.HGAP);
        pane.setVgap(Constants.VGAP);
        pane.setPadding(new Insets(Constants.PADDING));

        for (Client client: sClient.getClients()) {
            Button title = new Button(client.getAlias());
            title.setOnAction(event -> stage.setScene(new ClientDetails(stage, client).getScene()));
            pane.getChildren().addAll(title);
        }

        root.setCenter(pane);

        return new CScene(root);
    }
}
