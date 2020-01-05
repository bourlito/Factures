package com.bourlito.factures.scenes.client;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.scenes.IView;
import com.bourlito.factures.scenes.Main;
import com.bourlito.factures.scenes.utils.CScene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.bourlito.factures.service.SClient;

public class ClientList implements IView {

    private Stage stage;
    private SClient sClient = SClient.getInstance();

    public ClientList(Stage stage) {
        this.stage = stage;
    }

    @Override
    public CScene getScene() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(event -> {
            stage.setScene(new Main(stage).getScene());
        });

        Button btnNew = new Button("Nouveau");
        btnNew.setOnAction(e -> {
            stage.setScene(new ClientDetails(stage).getScene());
        });

        Button btnExport = new Button("Exporter");
        btnExport.setOnAction(event -> {
            sClient.exportDB();
        });

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10, 0, 0, 10));
        bbar.getButtons().addAll(btnRetour, btnNew, btnExport);
        root.setBottom(bbar);

        // clients
        FlowPane pane = new FlowPane();
        pane.setHgap(20);
        pane.setVgap(10);
        pane.setPadding(new Insets(25, 25, 25, 25));

        for (Client client: sClient.getClients()) {
            Button title = new Button(client.getAlias());
            title.setOnAction(event -> {
                stage.setScene(new ClientDetails(stage, client).getScene());
            });
            pane.getChildren().addAll(title);
        }

        root.setCenter(pane);

        return new CScene(root);
    }
}
