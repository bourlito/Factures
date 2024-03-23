package com.bourlito.factures.traitement;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Ligne;
import com.bourlito.factures.utils.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;


import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class XLS {

    private final String name;
    private final int numero;
    private final Client client;
    private final Traitement traitement;

    private final HSSFWorkbook wb = new HSSFWorkbook();
    private final HSSFSheet sheet = wb.createSheet("Facture");

    /**
     * constructeur
     *
     * @param name       le nom de la facture
     * @param numero     le numero de facture
     * @param client     le client associe
     * @param traitement le traitement effectue sur la page du decompte
     */
    protected XLS(String name, int numero, Client client, Traitement traitement) {
        this.name = name;
        this.numero = numero;
        this.client = client;
        this.traitement = traitement;
    }

    /**
     * methode de creation de la facture xls
     *
     * @throws IOException si l'ecriture du fichier se passe mal
     */
    public void createXls() throws IOException {

        this.creerFeuille();
        this.creerEntete(true);
        this.creerConditionsReglement();
        this.creerDetails();
        this.creerRecap();

        FileOutputStream out = new FileOutputStream(name);
        wb.write(out);
        out.close();
        wb.close();

    }

    /**
     * methode de creation d'une feuille dans le workbook
     **/
    private void creerFeuille() {
        sheet.setDefaultRowHeightInPoints(15f);
        sheet.setDefaultColumnWidth(10);
        sheet.setColumnWidth(5, Constants.XCL_COL_WIDTH * 5);
        sheet.setColumnWidth(6, Constants.XCL_COL_WIDTH * 16);
        sheet.setColumnWidth(7, Constants.XCL_COL_WIDTH * 12);
    }

    /**
     * methode de creation du header de chaque page
     *
     * @param first booleen pour savoir si on ajoute les conditions de reglement
     */
    private void creerEntete(Boolean first) {
        //info cpe + n°facture
        Row headerRow0 = sheet.createRow(sheet.getLastRowNum() + 1 + (first ? -1 : 0));
        headerRow0.setHeightInPoints(78.8f);
        //ajouterImage(MotsCles.IMG, -1, headerRow0.getRowNum(), -1, 30, 0.9);

        Cell cellCPE = headerRow0.createCell(1);
        CellStyle cellCPEStyle = wb.createCellStyle();
        cellCPEStyle.setWrapText(true);
        cellCPEStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellCPEStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellCPE.setCellStyle(cellCPEStyle);
        cellCPE.setCellValue(Constants.adresseCPE);
        sheet.addMergedRegion(new CellRangeAddress(headerRow0.getRowNum(), headerRow0.getRowNum(), 1, 5));

        Cell cellFact = headerRow0.createCell(6);
        CellStyle cellFactStyle = wb.createCellStyle();
        cellFactStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellFactStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellFactStyle.setFont(createFont((short) 14, true, false));
        cellFact.setCellStyle(cellFactStyle);
        cellFact.setCellValue("FACTURE N°");

        Cell cellNOFact = headerRow0.createCell(7);
        CellStyle cellNOFactStyle = wb.createCellStyle();
        cellNOFactStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellNOFactStyle.setAlignment(HorizontalAlignment.CENTER);
        cellNOFactStyle.setFont(createFont((short) 14, false, false));
        cellNOFact.setCellStyle(cellNOFactStyle);

        cellNOFact.setCellValue(Date.getYear() + "-" + Format.fNbFact().format(numero));

        //separation
        Row headerRow1 = sheet.createRow(sheet.getLastRowNum() + 1);
        headerRow1.setHeightInPoints(6f);

        for (int i = 0; i < 8; i++) {
            Cell cellBleue = headerRow1.createCell(i);
            CellStyle cellBleueStyle = createCellStyle(true, true, true, true);
            cellBleueStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            cellBleueStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellBleue.setCellStyle(cellBleueStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(headerRow1.getRowNum(), headerRow1.getRowNum(), 0, 7));

        sheet.createRow(sheet.getLastRowNum() + 1);

        //partie client
        Row headerRow3 = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cellClient = headerRow3.createCell(0);
        CellStyle cellClientStyle = createCellStyle(true, true, false, false);
        cellClientStyle.setAlignment(HorizontalAlignment.CENTER);
        cellClientStyle.setFont(createFont((short) Constants.FONT_SIZE, true, false));
        cellClient.setCellStyle(cellClientStyle);
        cellClient.setCellValue("Client");

        Cell cellClientNom = headerRow3.createCell(1);
        CellStyle cellClientNomStyle = createCellStyle(false, true, false, false);
        cellClientNomStyle.setFont(createFont((short) Constants.FONT_SIZE, true, false));
        cellClientNom.setCellStyle(cellClientNomStyle);
        cellClientNom.setCellValue("Nom :");

        Cell cellClientNomValue = headerRow3.createCell(2);
        CellStyle cellClientNomValueStyle = createCellStyle(false, true, false, false);
        cellClientNomValueStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellClientNomValue.setCellStyle(cellClientNomValueStyle);
        cellClientNomValue.setCellValue(client.getNom());

        Cell cellClientNomValue1 = headerRow3.createCell(3);
        cellClientNomValue1.setCellStyle(createCellStyle(false, true, false, false));

        Cell cellClientNomValue2 = headerRow3.createCell(4);
        cellClientNomValue2.setCellStyle(createCellStyle(false, true, true, false));
        sheet.addMergedRegion(new CellRangeAddress(headerRow3.getRowNum(), headerRow3.getRowNum(), 2, 4));

        Row headerRow4 = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cellClient1 = headerRow4.createCell(0);
        cellClient1.setCellStyle(createCellStyle(true, false, false, false));

        Cell cellClientAdress = headerRow4.createCell(1);
        CellStyle cellClientAdressStyle = wb.createCellStyle();
        cellClientAdressStyle.setFont(createFont((short) Constants.FONT_SIZE, true, false));
        cellClientAdress.setCellStyle(cellClientAdressStyle);
        cellClientAdress.setCellValue("Adresse :");

        Cell cellClientAdressValue = headerRow4.createCell(2);
        CellStyle cellClientAdressValueStyle = wb.createCellStyle();
        cellClientAdressValueStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellClientAdressValue.setCellStyle(cellClientAdressValueStyle);
        cellClientAdressValue.setCellValue(client.getAdresse());

        Cell cellClientAdressValue2 = headerRow4.createCell(4);
        cellClientAdressValue2.setCellStyle(createCellStyle(false, false, true, false));
        sheet.addMergedRegion(new CellRangeAddress(headerRow4.getRowNum(), headerRow4.getRowNum(), 2, 4));

        Row headerRow5 = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cellClient2 = headerRow5.createCell(0);
        cellClient2.setCellStyle(createCellStyle(true, false, false, true));

        Cell cellClientCP = headerRow5.createCell(1);
        CellStyle cellClientCPStyle = createCellStyle(false, false, false, true);
        cellClientCPStyle.setFont(createFont((short) Constants.FONT_SIZE, true, false));
        cellClientCP.setCellStyle(cellClientCPStyle);
        cellClientCP.setCellValue("CP :");

        Cell cellClientCPValue = headerRow5.createCell(2);
        CellStyle cellClientCPValueStyle = createCellStyle(false, false, false, true);
        cellClientCPValueStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellClientCPValue.setCellStyle(cellClientCPValueStyle);
        cellClientCPValue.setCellValue(client.getCp());

        Cell cellClientVille = headerRow5.createCell(3);
        CellStyle cellClientVilleStyle = createCellStyle(false, false, false, true);
        cellClientVilleStyle.setFont(createFont((short) Constants.FONT_SIZE, true, false));
        cellClientVille.setCellStyle(cellClientVilleStyle);
        cellClientVille.setCellValue("Ville :");

        Cell cellClientVilleValue = headerRow5.createCell(4);
        CellStyle cellClientVilleValueStyle = createCellStyle(false, false, true, true);
        cellClientVilleValueStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellClientVilleValue.setCellStyle(cellClientVilleValueStyle);
        cellClientVilleValue.setCellValue(client.getVille());

        //a regler avant le
        Cell cellDate = headerRow3.createCell(6);
        CellStyle cellDateStyle = createCellStyle(true, true, false, false);
        cellDateStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellDateStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellDate.setCellStyle(cellDateStyle);
        cellDate.setCellValue("Date :");

        Cell cellDateValue = headerRow3.createCell(7);
        CellStyle cellDateValueStyle = createCellStyle(false, true, true, false);
        cellDateValueStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellDateValueStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellDateValue.setCellStyle(cellDateValueStyle);
        cellDateValue.setCellValue(Date.getDate());

        Cell cellDate1 = headerRow4.createCell(6);
        cellDate1.setCellStyle(createCellStyle(true, false, false, false));

        Cell cellDate2 = headerRow4.createCell(7);
        cellDate2.setCellStyle(createCellStyle(false, false, true, false));

        Cell cellReglement = headerRow5.createCell(6);
        CellStyle cellReglementStyle = createCellStyle(true, false, false, true);
        cellReglementStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellReglementStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellReglement.setCellStyle(cellReglementStyle);
        cellReglement.setCellValue("A régler avant le :");

        Cell cellReglementValue = headerRow5.createCell(7);
        CellStyle cellReglementValueStyle = createCellStyle(false, false, true, true);
        cellReglementValueStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellReglementValueStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellReglementValue.setCellStyle(cellReglementValueStyle);
        cellReglementValue.setCellValue(Date.getARegler());

        if (!first)
            sheet.createRow(sheet.getLastRowNum() + 1);
    }

    /**
     * methode de creation des conditions de reglement
     */
    private void creerConditionsReglement() {
        Row cdnRow6 = sheet.createRow(sheet.getLastRowNum() + 1);
        cdnRow6.setHeightInPoints(7.5f);

        Row cdnRow7 = sheet.createRow(sheet.getLastRowNum() + 1);
        cdnRow7.setHeightInPoints(18.8f);

        Cell cellCdn = cdnRow7.createCell(0);
        CellStyle cellCdnStyle = createCellStyle(true, true, false, false);
        cellCdnStyle.setFont(createFont((short) Constants.FONT_SIZE, true, false));
        cellCdn.setCellStyle(cellCdnStyle);
        cellCdn.setCellValue("Conditions de règlement : Virement");

        for (int i = 1; i < 7; i++)
            cdnRow7.createCell(i).setCellStyle(createCellStyle(false, true, false, false));

        Cell cellCdn7 = cdnRow7.createCell(7);
        cellCdn7.setCellStyle(createCellStyle(false, true, true, false));
        sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 7));

        Row cdnRow8 = sheet.createRow(sheet.getLastRowNum() + 1);
        cdnRow8.setHeightInPoints(45f);
        sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 7));
        //ajouterImage(MotsCles.IMGREG, 8, cdnRow8.getRowNum(), 20, -1, 1.0);

        Cell cellCdnVide = cdnRow8.createCell(0);
        cellCdnVide.setCellStyle(createCellStyle(true, false, false, false));

        Cell cellCdnVide2 = cdnRow8.createCell(7);
        cellCdnVide2.setCellStyle(createCellStyle(false, false, true, false));

        Row cdnRow9 = sheet.createRow(sheet.getLastRowNum() + 1);
        cdnRow9.setHeightInPoints((30f));

        Cell cellCdnPhrase = cdnRow9.createCell(0);
        CellStyle cellCdnPhraseStyle = createCellStyle(true, false, false, false);
        cellCdnPhraseStyle.setWrapText(true);
        cellCdnPhraseStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellCdnPhraseStyle.setFont(createFont((short) 9, false, false));
        cellCdnPhrase.setCellStyle(cellCdnPhraseStyle);
        cellCdnPhrase.setCellValue("Clause de réserve de propriété : le vendeur conserve la propriété pleine et entière des produits et services vendus jusqu'au paiement complet du prix, en application de la loi du 12/05/1980. Escompte pour paiement anticipé : néant.");

        Cell cellCdnPhrase1 = cdnRow9.createCell(7);
        cellCdnPhrase1.setCellStyle(createCellStyle(false, false, true, false));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 0, 7));

        Row cdnRow10 = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cellCdnPenal = cdnRow10.createCell(0);
        CellStyle cellCdnPenalStyle = createCellStyle(true, false, false, true);
        cellCdnPenalStyle.setFont(createFont((short) 9, false, false));
        cellCdnPenalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellCdnPenal.setCellStyle(cellCdnPenalStyle);
        cellCdnPenal.setCellValue("Pénalités de retard : taux légal en vigueur.");

        for (int i = 1; i < 6; i++)
            cdnRow10.createCell(i).setCellStyle(createCellStyle(false, false, false, true));

        sheet.addMergedRegion(new CellRangeAddress(10, 10, 0, 5));

        Cell cellCdnPenal6 = cdnRow10.createCell(6);
        CellStyle cellCdnPenal6Style = createCellStyle(false, false, false, true);
        cellCdnPenal6Style.setFont(createFont((short) 10, true, false));
        cellCdnPenal6Style.setVerticalAlignment(VerticalAlignment.CENTER);
        cellCdnPenal6Style.setAlignment(HorizontalAlignment.CENTER);
        cellCdnPenal6.setCellStyle(cellCdnPenal6Style);
        cellCdnPenal6.setCellValue("TVA sur les encaissements");

        Cell cellCdnPenal7 = cdnRow10.createCell(7);
        cellCdnPenal7.setCellStyle(createCellStyle(false, false, true, true));
        sheet.addMergedRegion(new CellRangeAddress(10, 10, 6, 7));
    }

    /**
     * methode de creation des details
     */
    private void creerDetails() {
        //entete de colonnes
        Row enteteRow = sheet.createRow(12);

        this.creerCell(enteteRow, Constants.NUM_COL_DAT_SAI, true, "Date Saisie");
        this.creerCell(enteteRow, Constants.NUM_COL_NOM_DOS_0, false, "Nom dossier");
        enteteRow.createCell(Constants.NUM_COL_NOM_DOS_1).setCellStyle(createCellStyle(false, true, false, true));
        enteteRow.createCell(Constants.NUM_COL_NOM_DOS_2).setCellStyle(createCellStyle(false, true, true, true));
        sheet.addMergedRegion(new CellRangeAddress(12, 12, Constants.NUM_COL_NOM_DOS_0, Constants.NUM_COL_NOM_DOS_2));
        this.creerCell(enteteRow, Constants.NUM_COL_NB_LI_0, false, "Nombre Ligne");
        enteteRow.createCell(Constants.NUM_COL_NB_LI_1).setCellStyle(createCellStyle(false, true, true, true));
        sheet.addMergedRegion(new CellRangeAddress(12, 12, Constants.NUM_COL_NB_LI_0, Constants.NUM_COL_NB_LI_1));
        this.creerCell(enteteRow, Constants.NUM_COL_TRF_LI, true, "Tarif Ligne");
        this.creerCell(enteteRow, Constants.NUM_COL_THT, true, "Total HT");

        List<Ligne> liste = traitement.getListeLignes(Constants.NUM_COL_LI);
        if (liste != null && !liste.isEmpty()) {
            this.creerEnteteType("Lignes");
            this.remplirLigne(liste);
        }

        // +3 car les tarifs commencent à la colonne D dans le decompte
        for (int i=0; i < client.getTarifs().size(); i++)
            this.remplirIfNotEmpty(client.getTarifs().get(i).getNom(), i+3);

        this.remplirIfNotEmpty(Constants.LIBELLE_AF, Constants.NUM_COL_AF);
    }

    /**
     * methode de creation du recap de la facture
     */
    private void creerRecap() {
        List<Integer> rowNum = new ArrayList<>();
        rowNum.add(0);
        for (int i = 35; i < 41; i++)
            rowNum.add(i);

        if (rowNum.contains(sheet.getLastRowNum() % 41))
            creerEntete(false);

        //remarques
        sheet.createRow(sheet.getLastRowNum() + 1);

        Row recapRow = sheet.createRow(sheet.getLastRowNum() + 1);
        Cell cellRemarque = recapRow.createCell(0);
        CellStyle cellRemarqueStyle = wb.createCellStyle();
        cellRemarqueStyle.setFont(createFont((short) Constants.FONT_SIZE, true, true));
        cellRemarque.setCellStyle(cellRemarqueStyle);
        cellRemarque.setCellValue("Remarques");

        Cell cellRemarqueValue = recapRow.createCell(1);
        cellRemarqueValue.setCellStyle(createCellStyle(true, true, false, false));
        cellRemarqueValue.setCellValue(traitement.getNvDosAsString());

        recapRow.createCell(2).setCellStyle(createCellStyle(false, true, true, false));

        Row recapRow2 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow2.createCell(1).setCellStyle(createCellStyle(true, false, false, false));
        recapRow2.createCell(2).setCellStyle(createCellStyle(false, false, true, false));

        Row recapRow3 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow3.createCell(1).setCellStyle(createCellStyle(true, false, false, false));
        recapRow3.createCell(2).setCellStyle(createCellStyle(false, false, true, false));

        Row recapRow4 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow4.createCell(1).setCellStyle(createCellStyle(true, false, false, false));
        recapRow4.createCell(2).setCellStyle(createCellStyle(false, false, true, false));

        Row recapRow5 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow5.createCell(1).setCellStyle(createCellStyle(true, false, false, false));
        recapRow5.createCell(2).setCellStyle(createCellStyle(false, false, true, false));

        Row recapRow6 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow6.createCell(1).setCellStyle(createCellStyle(true, false, false, true));
        recapRow6.createCell(2).setCellStyle(createCellStyle(false, false, true, true));

        //total
        Cell cellTotal = recapRow.createCell(4);
        CellStyle cellTotalStyle = createCellStyle(true, true, false, false);
        cellTotalStyle.setFont(createFont((short) 12, true, false));
        cellTotalStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotal.setCellStyle(cellTotalStyle);
        cellTotal.setCellValue("Total nombre de lignes");

        recapRow.createCell(5).setCellStyle(createCellStyle(false, true, false, false));
        recapRow.createCell(6).setCellStyle(createCellStyle(false, true, false, false));
        sheet.addMergedRegion(new CellRangeAddress(recapRow.getRowNum(), recapRow.getRowNum(), 4, 6));

        Cell cellTotalValue = recapRow.createCell(7);
        CellStyle cellTotalValueStyle = createCellStyle(false, true, true, false);
        cellTotalValueStyle.setFont(createFont((short) 12, true, false));
        cellTotalValueStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalValue.setCellStyle(cellTotalValueStyle);
        cellTotalValue.setCellValue(traitement.getTotalLigneAsString());

        recapRow2.createCell(4).setCellStyle(createCellStyle(true, false, false, false));
        recapRow2.createCell(7).setCellStyle(createCellStyle(false, false, true, false));

        Cell cellTotalHT = recapRow3.createCell(4);
        CellStyle cellTotalHTStyle = createCellStyle(true, false, false, false);
        cellTotalHTStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellTotalHTStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalHT.setCellStyle(cellTotalHTStyle);
        cellTotalHT.setCellValue("TOTAL € HT");

        sheet.addMergedRegion(new CellRangeAddress(recapRow3.getRowNum(), recapRow3.getRowNum(), 4, 6));

        Cell cellTotalHTValue = recapRow3.createCell(7);
        CellStyle cellTotalHTValueStyle = createCellStyle(false, false, true, false);
        cellTotalHTValueStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellTotalHTValueStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalHTValue.setCellStyle(cellTotalHTValueStyle);
        cellTotalHTValue.setCellValue(traitement.getTotalHtAsString() + " €");

        Cell cellTVA = recapRow4.createCell(4);
        CellStyle cellTVAStyle = createCellStyle(true, false, false, false);
        cellTVAStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellTVAStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTVA.setCellStyle(cellTVAStyle);
        cellTVA.setCellValue("TVA");

        sheet.addMergedRegion(new CellRangeAddress(recapRow4.getRowNum(), recapRow4.getRowNum(), 4, 6));

        Cell cellTVAValue = recapRow4.createCell(7);
        CellStyle cellTVAValueStyle = createCellStyle(false, false, true, false);
        cellTVAValueStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellTVAValueStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTVAValue.setCellStyle(cellTVAValueStyle);
        cellTVAValue.setCellValue(traitement.getTotalTvaAsString() + " €");

        Cell cellTotalTTC = recapRow5.createCell(4);
        CellStyle cellTotalTTCStyle = createCellStyle(true, false, false, false);
        cellTotalTTCStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellTotalTTCStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalTTC.setCellStyle(cellTotalTTCStyle);
        cellTotalTTC.setCellValue("TOTAL € TTC");

        sheet.addMergedRegion(new CellRangeAddress(recapRow5.getRowNum(), recapRow5.getRowNum(), 4, 6));

        Cell cellTotalTTCValue = recapRow5.createCell(7);
        CellStyle cellTotalTTCValueStyle = createCellStyle(false, false, true, false);
        cellTotalTTCValueStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellTotalTTCValueStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalTTCValue.setCellStyle(cellTotalTTCValueStyle);
        cellTotalTTCValue.setCellValue(traitement.getTotalTtcAsString() + " €");

        recapRow6.createCell(4).setCellStyle(createCellStyle(true, false, false, true));
        recapRow6.createCell(5).setCellStyle(createCellStyle(false, false, false, true));
        recapRow6.createCell(6).setCellStyle(createCellStyle(false, false, false, true));
        recapRow6.createCell(7).setCellStyle(createCellStyle(false, false, true, true));
    }

    /**
     * methode de remplissage des lignes
     *
     * @param data la liste contenant les lignes
     */
    private void remplirLigne( List<Ligne> data) {
        CellStyle cellStyle = createCellStyle(true, true, true, true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellStyle.setShrinkToFit(true);

        for (Ligne ligne : data) {
            if (sheet.getLastRowNum() == 39 || sheet.getLastRowNum() % 41 == 0)
                creerEntete(false);

            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            this.creerCell(row, Constants.NUM_COL_DAT_SAI, cellStyle, ligne.getDate());
            this.creerCell(row, Constants.NUM_COL_NOM_DOS_0, cellStyle, ligne.getEntreprise());
            for (int i = Constants.NUM_COL_NOM_DOS_1; i <= Constants.NUM_COL_NOM_DOS_2; i++)
                row.createCell(i).setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), Constants.NUM_COL_NOM_DOS_0, Constants.NUM_COL_NOM_DOS_2));
            this.creerCell(row, cellStyle, Constants.NUM_COL_NB_LI_0, ligne.getNbLigne());
            row.createCell(Constants.NUM_COL_NB_LI_1).setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), Constants.NUM_COL_NB_LI_0, Constants.NUM_COL_NB_LI_1));
            this.creerCell(row, cellStyle, Constants.NUM_COL_TRF_LI, ligne.getTarif());
            this.creerCell(row, Math.max(2, BigDecimal.valueOf(ligne.getTarif()).scale()));
        }
    }

    /**
     * methode de remplissage d'une categorie si elle n'est pas vide
     *
     * @param entete libelle de la categorie
     * @param numCol correspondante a la categorie dans le decompte
     */
    private void remplirIfNotEmpty(String entete, int numCol) {
        List<Ligne> liste = traitement.getListeLignes(numCol);
        if (liste != null && !liste.isEmpty()) {
            this.creerEnteteType(entete);
            this.remplirLigne(liste);
        }
    }

    /**
     * methode de creation de la ligne dd'entete d'une categorie (lignes, lettrage, etc...)
     *
     * @param entete libelle de l'entete
     */
    private void creerEnteteType(String entete) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        Cell cell = row.createCell(0);
        CellStyle cellStyle = createCellStyle(true, true, false, true);
        cellStyle.setFont(createFont((short) 12, true, true));
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(entete);

        for (int i = 1; i < 7; i++)
            row.createCell(i).setCellStyle(createCellStyle(false, true, false, true));

        row.createCell(7).setCellStyle(createCellStyle(false, true, true, true));
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 7));
    }

    /**
     * methode de creation d'une font
     *
     * @param size la taille
     * @param bold booleen gras
     * @param blue booleen couleur bleue
     * @return {@link Font}
     */
    
    private Font createFont(Short size,  Boolean bold, Boolean blue) {
        Font font = wb.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints(size);
        if (bold) font.setBold(bold);
        if (blue) font.setColor(IndexedColors.DARK_BLUE.getIndex());

        return font;
    }

    /**
     * methode de creation d'un style de cellule
     *
     * @param left   booleen de bordure gauche
     * @param top    booleen de bordure superieure
     * @param right  booleen de bordure droite
     * @param bottom booleen de bordure inferieure
     * @return {@link CellStyle}
     */
    private CellStyle createCellStyle( Boolean left, Boolean top, Boolean right, Boolean bottom) {
        CellStyle cellStyle = wb.createCellStyle();

        if (left) {
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setLeftBorderColor(IndexedColors.DARK_BLUE.getIndex());
        }

        if (top) {
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setTopBorderColor(IndexedColors.DARK_BLUE.getIndex());
        }

        if (right) {
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setRightBorderColor(IndexedColors.DARK_BLUE.getIndex());
        }

        if (bottom) {
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBottomBorderColor(IndexedColors.DARK_BLUE.getIndex());
        }

        return cellStyle;
    }

    /**
     * methode de creation d'une cellule
     *
     * @param row     la ligne de la cellule
     * @param col     la colonne de la cellule
     * @param right   booleen de bordure droite
     * @param libelle libelle de la cellule
     */
    private void creerCell( Row row, int col, boolean right, String libelle) {
        Cell cell = row.createCell(col);
        CellStyle cellStyle = createCellStyle(true, true, right, true);
        cellStyle.setFont(createFont((short) 12, true, true));
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(libelle);
    }


    /**
     * methode de creation d'une cellule
     * @param row     la ligne de la cellule
     * @param col     la colonne de la cellule
     * @param cellStyle le style de la cellule
     * @param texte la valeur de la cellule (string)
     */
    private void creerCell( Row row, int col, CellStyle cellStyle, String texte) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(texte);
    }

    /**
     * methode de creation d'une cellule
     * @param row       la ligne de la cellule
     * @param cellStyle le style de la cellule
     * @param col       le numero de colonne
     * @param number    la valeur de la cellule (double)
     */
    private void creerCell(Row row, CellStyle cellStyle, int col, double number) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(number);
    }

    /**
     * methode de creation d'une cellule
     * @param row     la ligne de la cellule
     */
    private void creerCell(Row row, int decimals) {
        String formule = Column.getLetterFromInt(Constants.NUM_COL_NB_LI_0) + (row.getRowNum() + 1)
                + "*" + Column.getLetterFromInt(Constants.NUM_COL_TRF_LI) + (row.getRowNum() + 1);

        DataFormat df = wb.createDataFormat();
        CellStyle cellStyle = createCellStyle(true, true, true, true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(createFont((short) Constants.FONT_SIZE, false, false));
        cellStyle.setShrinkToFit(true);
        String format = "0.";
        for (int i = 0; i < decimals; i++) format += "0";
        format += " €";
        cellStyle.setDataFormat(df.getFormat(format));

        Cell cell = row.createCell(Constants.NUM_COL_THT);
        cell.setCellStyle(cellStyle);
        cell.setCellFormula(formule);
    }
}
