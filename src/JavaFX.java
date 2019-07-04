import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class JavaFX extends Application implements MotsCles {
    private static Calendar calendar = Calendar.getInstance();
    private String ENTREE = null;
    private String ENTADD = null;
    private String RESULT = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(setGrid(primaryStage), 800, 500);
        scene.getStylesheets().add(JavaFX.class.getResource("javafx.css").toExternalForm());

        primaryStage.setTitle("Factures");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane setGrid(Stage stage) {
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
            configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                labelXlFileAffich.setText(file.getName());
                ENTREE = file.getAbsolutePath();
            }
        });

        btnXlAdd.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Fichier d'adresses");
            configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                labelXlAddAffich.setText(file.getName());
                ENTADD = file.getAbsolutePath();
            }
        });

        btnFolder.setOnAction(e -> {
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setTitle("Dossier de destination");
                    configureFolderChooser(directoryChooser);
                    File selectedDirectory = directoryChooser.showDialog(stage);
                    if (selectedDirectory != null) {
                        labelFolderAffich.setText(selectedDirectory.getName());
                        RESULT = selectedDirectory.getAbsolutePath();
                    }
                }
        );

        btnValider.setOnAction(event -> {
            if (nFact.getText().equals("") || nFact.getText() == null || ENTREE == null || ENTADD == null || RESULT == null) {
                scenetitle.setText("Il faut remplir tous les champs !");
                scenetitle.setStyle("-fx-fill: tomato");
                return;
            }

            List<Entreprise> entreprises = new Mamasita().getAdresseTarif(ENTADD);
            Entreprise entreprise = null;

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(new File(ENTREE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                new PDF().creerFichierErreur(e);
            }
            HSSFWorkbook wb = null;
            try {
                assert fis != null;
                wb = new HSSFWorkbook(fis);
            } catch (IOException e) {
                e.printStackTrace();
                new PDF().creerFichierErreur(e);
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
                    new PDF().creerFichierErreur();
                    return;
                }

                NumberFormat nbFactFormat = NumberFormat.getInstance(Locale.FRANCE);
                nbFactFormat.setMinimumIntegerDigits(2);
                String libelleFac = RESULT + "\\Facture CPE traitement " + entreprise.getNomEntreprise() + " " + nbFactFormat.format(calendar.get(Calendar.MONTH)) + (calendar.get(Calendar.YEAR) - 2000);

                new XCL().creerXCL(wb.getSheetAt(a), libelleFac + ".xls", nFacture, entreprise);
                new PDF().createPdf(wb.getSheetAt(a), libelleFac + ".pdf", nFacture, entreprise);

                a++;
                nFacture++;
            } while (a < wb.getNumberOfSheets());
            stage.close();
        });

        return grid;
    }

    private void configureFileChooser(FileChooser fileChooser) {
        File folder = new File(DOSSIER);
        if (folder.isDirectory()) {
            fileChooser.setInitialDirectory(folder);
        }

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLS", "*.xls")
        );
    }

    private void configureFolderChooser(DirectoryChooser directoryChooser) {
        File folder = new File(DOSSIER);
        if (folder.isDirectory()) {
            directoryChooser.setInitialDirectory(folder);
        }
    }
}