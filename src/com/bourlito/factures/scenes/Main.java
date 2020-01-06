package com.bourlito.factures.scenes;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.scenes.client.ClientList;
import com.bourlito.factures.scenes.utils.CScene;
import com.bourlito.factures.scenes.utils.Chooser;
import com.bourlito.factures.service.SClient;
import com.bourlito.factures.traitement.PDF;
import com.bourlito.factures.traitement.XCL;
import com.bourlito.factures.utils.Date;
import com.bourlito.factures.utils.Erreur;
import com.bourlito.factures.utils.NumFormat;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main implements IView{

    // TODO: modifier adresse en utilisant db client

    private Stage stage;
    private static File destination;
    private static String nFact = "";
    private TextField tNum;

    public Main(Stage stage) {
        this.stage = stage;
    }

    @Override
    public CScene getScene() {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Coucou petite perruche");
        scenetitle.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label lNum = new Label("N° de première facture :");
        grid.add(lNum, 0, 1);
        tNum = new TextField();
        tNum.setMaxWidth(200);
        tNum.setText(nFact);
        tNum.textProperty().addListener((observable, oldValue, newValue) -> {
            nFact = newValue;
        });
        grid.add(tNum, 1, 1);

        Button btnDest = new Button("Dossier de destination");
        btnDest.setMinWidth(200);
        grid.add(btnDest, 0, 2);
        Label lDest = new Label();
        lDest.setText(destination != null ? destination.getName() : "");
        grid.add(lDest, 1, 2);

        Button btnClients = new Button("Clients");
        btnClients.setMinWidth(200);
        grid.add(btnClients, 0, 3);
        btnClients.setOnAction(event -> {
            stage.setScene(new ClientList(stage).getScene());
        });

        Button btnValider = new Button("Valider");
        btnValider.setId("btnValider");
        btnValider.setMinWidth(200);
        grid.add(btnValider, 1, 3);

        btnDest.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Dossier de destination");
            Chooser.configureFolderChooser(directoryChooser);
            File selectedDirectory = directoryChooser.showDialog(stage);
            if (selectedDirectory != null) {
                lDest.setText(selectedDirectory.getName());
                destination = selectedDirectory;
            }
        });

        btnValider.setOnAction(event -> {
            if (tNum.getText().equals("") || tNum.getText() == null || destination != null) {
                scenetitle.setText("Il faut remplir tous les champs !");
                scenetitle.setStyle("-fx-fill: tomato");
                return;
            }

            this.valider();
        });

        return new CScene(grid);
    }

    private void valider(){

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(destination.getAbsolutePath() + "\\decompte.xls"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Erreur.creerFichierErreur(e.getMessage());
        }
        HSSFWorkbook wb = null;
        try {
            assert fis != null;
            wb = new HSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
            Erreur.creerFichierErreur(e.getMessage());
        }
        int a = 0;
        int nFacture = a + Integer.parseInt(tNum.getText());
        do {
            assert wb != null;
            HSSFSheet sheet = wb.getSheetAt(a);

            Client client = SClient.getInstance().getClientByAlias(sheet.getSheetName());

            if (client == null) {
                Erreur.creerFichierErreur(sheet.getSheetName() + " ne correspond à aucun client de la base de données.");
                return;
            }

            String libelleFac = destination.getAbsolutePath() + "\\Facture CPE traitement " + client.getNom() + " " + Date.getLibelle() + " - " + NumFormat.fNbFact().format(nFacture);

            new XCL(wb.getSheetAt(a), libelleFac + ".xls", nFacture, client).creerXCL();
            new PDF(wb.getSheetAt(a), libelleFac + ".pdf", nFacture, client).createPdf();

            a++;
            nFacture++;
        } while (a < wb.getNumberOfSheets());

        finishWindow().show();
    }

    private Stage finishWindow(){
        Stage newWindow = new Stage();
        newWindow.setTitle("Terminer");

        Label label = new Label("Factures terminées.");
        Button btnTerminer = new Button("Terminer");
        btnTerminer.setId("btnValider");
        btnTerminer.setMinWidth(200);

        btnTerminer.setOnAction(event -> {
            newWindow.close();
            stage.close();
        });

        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);
        box.getChildren().addAll(label, btnTerminer);

        BorderPane pane = new BorderPane();
        pane.setCenter(box);

        Scene scene = new CScene(pane, stage.getWidth(), stage.getHeight()/3);

        newWindow.setScene(scene);

        return newWindow;
    }
}
