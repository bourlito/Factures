package com.bourlito.factures.scenes;

import com.bourlito.factures.utils.Erreur;
import com.bourlito.factures.utils.Parametres;
import com.bourlito.factures.dto.Entreprise;
import com.bourlito.factures.scenes.client.ClientList;
import com.bourlito.factures.scenes.utils.CScene;
import com.bourlito.factures.utils.Date;
import com.bourlito.factures.utils.NumFormat;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

        Button btnFolder = new Button("Dossier de destination");
        btnFolder.setMinWidth(200);
        grid.add(btnFolder, 0, 2);
        Label labelFolderAffich = new Label();
        grid.add(labelFolderAffich, 1, 2);

        Button btnXlAdd = new Button("Fichier d'adresses");
        btnXlAdd.setMinWidth(200);
        grid.add(btnXlAdd, 0, 3);
        Label labelXlAddAffich = new Label();
        grid.add(labelXlAddAffich, 1, 3);

        Button btnClients = new Button("Clients");
        btnClients.setMinWidth(200);
        grid.add(btnClients, 0, 4);
        btnClients.setOnAction(event -> {
            stage.setScene(new ClientList(stage).getScene());
        });

        Button btnValider = new Button("Valider");
        btnValider.setId("btnValider");
        btnValider.setMinWidth(200);
        grid.add(btnValider, 1, 4);

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
        });

        btnValider.setOnAction(event -> {
            if (nFact.getText().equals("") || nFact.getText() == null || Parametres.getAdresses() == null || Parametres.getDestination() == null) {
                scenetitle.setText("Il faut remplir tous les champs !");
                scenetitle.setStyle("-fx-fill: tomato");
                return;
            }

            valider(nFact, stage);
        });

        return new CScene(grid);
    }

    private static void valider(TextField nFact, Stage stage){
        List<Entreprise> entreprises = Mamasita.getAdresseTarif(Parametres.getAdresses());
        Entreprise entreprise = null;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(Parametres.getDestination() + "\\decompte.xls"));
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
                Erreur.creerFichierErreur("verifier le nom de la feuille");
                return;
            }

            String libelleFac = Parametres.getDestination() + "\\Facture CPE traitement " + entreprise.getNomEntreprise() + " " + Date.getLibelle() + " - " + NumFormat.fNbFact().format(nFacture);

            new XCL(wb.getSheetAt(a), libelleFac + ".xls", nFacture, entreprise).creerXCL();
            new PDF(wb.getSheetAt(a), libelleFac + ".pdf", nFacture, entreprise).createPdf();

            a++;
            nFacture++;
        } while (a < wb.getNumberOfSheets());

        stage.close();
    }
}
