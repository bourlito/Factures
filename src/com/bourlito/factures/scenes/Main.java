package com.bourlito.factures.scenes;

import com.bourlito.factures.dto.Entreprise;
import com.bourlito.factures.scenes.client.ClientList;
import com.bourlito.factures.scenes.utils.CScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.bourlito.factures.scenes.utils.Chooser;
import com.bourlito.factures.traitement.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Main implements IView{

    // TODO: retirer fichier adresse et decompte du main
    // TODO: modifier adresse en utilisant db client

    private Stage stage;

    public Main(Stage stage) {
        this.stage = stage;
    }

    @Override
    public CScene getScene() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button btnClients = new Button("Clients");
        btnClients.setOnAction(event -> {
            stage.setScene(new ClientList(stage).getScene());
        });

        ButtonBar bbar = new ButtonBar();
        bbar.setPadding(new Insets(10, 0, 0, 10));
        bbar.getButtons().addAll(btnClients);
        root.setBottom(bbar);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Coucou petite perruche");
        scenetitle.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label labelNFac = new Label("N° de première facture :");
        grid.add(labelNFac, 0, 1);
        TextField nFact = new TextField();
        nFact.setMaxWidth(200);
        grid.add(nFact, 1, 1);

        Button btnXlFile = new Button("Décompte Excel");
        btnXlFile.setMinWidth(200);
        grid.add(btnXlFile, 0, 2);
        Label labelXlFileAffich = new Label();
        grid.add(labelXlFileAffich, 1, 2);

        Button btnFolder = new Button("Dossier de destination");
        btnFolder.setMinWidth(200);
        grid.add(btnFolder, 0, 3);
        Label labelFolderAffich = new Label();
        grid.add(labelFolderAffich, 1, 3);

        Button btnXlAdd = new Button("Fichier d'adresses");
        btnXlAdd.setMinWidth(200);
        grid.add(btnXlAdd, 0, 4);
        Label labelXlAddAffich = new Label();
        grid.add(labelXlAddAffich, 1, 4);

        Button btnValider = new Button("Valider");
        btnValider.setMinWidth(200);
        btnValider.setId("btnValider");
        grid.add(btnValider, 1, 5);

        btnXlFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Décompte excel");
            Chooser.configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                labelXlFileAffich.setText(file.getName());
                Parametres.setDecompte(file.getAbsolutePath());
            }
        });

        btnXlAdd.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Fichier d'adresses");
            Chooser.configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                labelXlAddAffich.setText(file.getName());
                Parametres.setAdresses(file.getAbsolutePath());
            }
        });

        btnFolder.setOnAction(e -> {
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setTitle("Dossier de destination");
                    Chooser.configureFolderChooser(directoryChooser);
                    File selectedDirectory = directoryChooser.showDialog(stage);
                    if (selectedDirectory != null) {
                        labelFolderAffich.setText(selectedDirectory.getName());
                        Parametres.setDestination(selectedDirectory.getAbsolutePath());
                    }
                }
        );

        btnValider.setOnAction(event -> {
            if (nFact.getText().equals("") || nFact.getText() == null || Parametres.getDecompte() == null || Parametres.getAdresses() == null || Parametres.getDestination() == null) {
                scenetitle.setText("Il faut remplir tous les champs !");
                scenetitle.setStyle("-fx-fill: tomato");
                return;
            }

            valider(nFact, stage);
        });

        root.setCenter(grid);

        return new CScene(root);
    }

    private static void valider(TextField nFact, Stage stage){
        List<Entreprise> entreprises = new Mamasita().getAdresseTarif(Parametres.getAdresses());
        Entreprise entreprise = null;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(Parametres.getDecompte()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Mamasita.creerFichierErreur(e.getMessage());
        }
        HSSFWorkbook wb = null;
        try {
            assert fis != null;
            wb = new HSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
            Mamasita.creerFichierErreur(e.getMessage());
        }
        int a = 0;
        int nFacture = a + Integer.parseInt(nFact.getText());
        boolean entOk = false;
        do {
            assert wb != null;
            HSSFSheet sheet = wb.getSheetAt(a);

            for (Entreprise ent : entreprises) {
                if (ent.getAlias().equalsIgnoreCase(sheet.getSheetName())) {
                    entreprise = ent;
                    entOk = true;
                }
            }

            if (!entOk) {
                Mamasita.creerFichierErreur("verifier le nom de la feuille");
                return;
            }

            String libelleFac = Parametres.getDestination() + "\\Facture CPE com.bourlito.factures.traitement " + entreprise.getNomEntreprise() + " " + Date.getLibelle() + " - " + NumFormat.fNbFact().format(nFacture);

            new XCL().creerXCL(wb.getSheetAt(a), libelleFac + ".xls", nFacture, entreprise);
            new PDF().createPdf(wb.getSheetAt(a), libelleFac + ".pdf", nFacture, entreprise);

            a++;
            nFacture++;
        } while (a < wb.getNumberOfSheets());

        stage.close();
    }
}
