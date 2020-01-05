package com.bourlito.factures.scenes;

import com.bourlito.factures.JavaFX;
import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Tarif;
import com.bourlito.factures.dto.Tranche;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.bourlito.factures.service.SClient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ClientList implements IView {

    private Stage stage;
    private SClient sClient = SClient.getInstance();

    public ClientList(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Scene getScene() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button btnRetour = new Button("Retour");
        btnRetour.setOnAction(event -> {
            Scene scene = new Main(stage).getScene();
            scene.getStylesheets().add(JavaFX.STYLE);
            stage.setScene(scene);
        });

        Button btnNew = new Button("Nouveau");
        btnNew.setOnAction(e -> {
            Scene scene = new ClientDetails(stage).getScene();
            scene.getStylesheets().add(JavaFX.STYLE);
            stage.setScene(scene);
        });

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10, 0, 0, 10));
        bbar.getButtons().addAll(btnRetour, btnNew);
        root.setBottom(bbar);

        // clients
        FlowPane pane = new FlowPane();
        pane.setHgap(20);
        pane.setVgap(10);
        pane.setPadding(new Insets(25, 25, 25, 25));

        for (Client client: sClient.getClients()) {
            Button title = new Button(client.getAlias());
            title.setOnAction(event -> {
                Scene scene = new ClientDetails(stage, client).getScene();
                scene.getStylesheets().add(JavaFX.STYLE);
                stage.setScene(scene);
            });
            pane.getChildren().addAll(title);
        }

        root.setCenter(pane);

        return new Scene(root, 800, 500);
    }
}
