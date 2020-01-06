package com.bourlito.factures.traitement;

import com.bourlito.factures.Erreur;
import com.bourlito.factures.MotsCles;
import com.bourlito.factures.dto.Entreprise;
import com.bourlito.factures.dto.Ligne;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XCL extends Mamasita {

    // TODO: ajouter images
    // TODO: type de cellule (nbLigne -> num, totalHT -> formula)

    private int totalLigne = 0;
    private double totalHT = 0;

    private HSSFWorkbook wb;
    private HSSFSheet sheet;

    public XCL(HSSFSheet sheet, String filename, int nFacture, Entreprise entreprise) {
        super(sheet, filename, nFacture, entreprise);
        this.wb = new HSSFWorkbook();
    }

    public void creerXCL() {
        creerFeuille();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
        try {
            wb.write(out);
            if (out != null)
                out.close();
            wb.close();
        } catch (IOException e) {
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
    }

    private void creerFeuille() {
        sheet = wb.createSheet("Facture");
        sheet.setDefaultRowHeightInPoints(15f);
        sheet.setDefaultColumnWidth(10);
        sheet.setColumnWidth(5, 256 * 5);
        sheet.setColumnWidth(6, 256 * 16);
        sheet.setColumnWidth(7, 256 * 12);
        creerEntete(true);
        creerCdnReg();
        creerDetail();
        creerRecap();
    }

    private void creerEntete(Boolean first) {
        //info cpe + n°facture
        Row headerRow0 = sheet.createRow(sheet.getLastRowNum() + 1 + (first ? -1 : 0));
        headerRow0.setHeightInPoints(78.8f);
        //ajouterImage(MotsCles.IMG, -1, headerRow0.getRowNum(), -1, 30, 0.9);

        Cell cellCPE = headerRow0.createCell(1);
        CellStyle cellCPEStyle = wb.createCellStyle();
        cellCPEStyle.setWrapText(true);
        cellCPEStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellCPEStyle.setFont(setFont((short) 11, false, false));
        cellCPE.setCellStyle(cellCPEStyle); // FIXME
        cellCPE.setCellValue(MotsCles.adresseCPE);
        sheet.addMergedRegion(new CellRangeAddress(headerRow0.getRowNum(), headerRow0.getRowNum(), 1, 5));

        Cell cellFact = headerRow0.createCell(6);
        CellStyle cellFactStyle = wb.createCellStyle();
        cellFactStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellFactStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellFactStyle.setFont(setFont((short) 14, true, false));
        cellFact.setCellStyle(cellFactStyle);
        cellFact.setCellValue("FACTURE N°");

        Cell cellNOFact = headerRow0.createCell(7);
        CellStyle cellNOFactStyle = wb.createCellStyle();
        cellNOFactStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellNOFactStyle.setAlignment(HorizontalAlignment.CENTER);
        cellNOFactStyle.setFont(setFont((short) 14, false, false));
        cellNOFact.setCellStyle(cellNOFactStyle);

        cellNOFact.setCellValue(Date.getYear() + "-" + NumFormat.fNbFact().format(nFacture));

        //separation
        Row headerRow1 = sheet.createRow(sheet.getLastRowNum() + 1);
        headerRow1.setHeightInPoints(6f);

        for (int i = 0; i < 8; i++) {
            Cell cellBleue = headerRow1.createCell(i);
            CellStyle cellBleueStyle = setCellStyle(true, true, true, true);
            cellBleueStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            cellBleueStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellBleue.setCellStyle(cellBleueStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(headerRow1.getRowNum(), headerRow1.getRowNum(), 0, 7));

        sheet.createRow(sheet.getLastRowNum() + 1);

        //partie client
        Row headerRow3 = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cellClient = headerRow3.createCell(0);
        CellStyle cellClientStyle = setCellStyle(true, true, false, false);
        cellClientStyle.setAlignment(HorizontalAlignment.CENTER);
        cellClientStyle.setFont(setFont((short) 11, true, false));
        cellClient.setCellStyle(cellClientStyle);
        cellClient.setCellValue("Client");

        Cell cellClientNom = headerRow3.createCell(1);
        CellStyle cellClientNomStyle = setCellStyle(false, true, false, false);
        cellClientNomStyle.setFont(setFont((short) 11, true, false));
        cellClientNom.setCellStyle(cellClientNomStyle);
        cellClientNom.setCellValue("Nom :");

        Cell cellClientNomValue = headerRow3.createCell(2);
        CellStyle cellClientNomValueStyle = setCellStyle(false, true, false, false);
        cellClientNomValueStyle.setFont(setFont((short) 11, false, false));
        cellClientNomValue.setCellStyle(cellClientNomValueStyle);
        cellClientNomValue.setCellValue(entreprise.getNomEntreprise());

        Cell cellClientNomValue1 = headerRow3.createCell(3);
        cellClientNomValue1.setCellStyle(setCellStyle(false, true, false, false));

        Cell cellClientNomValue2 = headerRow3.createCell(4);
        cellClientNomValue2.setCellStyle(setCellStyle(false, true, true, false));
        sheet.addMergedRegion(new CellRangeAddress(headerRow3.getRowNum(), headerRow3.getRowNum(), 2, 4));

        Row headerRow4 = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cellClient1 = headerRow4.createCell(0);
        cellClient1.setCellStyle(setCellStyle(true, false, false, false));

        Cell cellClientAdress = headerRow4.createCell(1);
        CellStyle cellClientAdressStyle = wb.createCellStyle();
        cellClientAdressStyle.setFont(setFont((short) 11, true, false));
        cellClientAdress.setCellStyle(cellClientAdressStyle);
        cellClientAdress.setCellValue("Adresse :");

        Cell cellClientAdressValue = headerRow4.createCell(2);
        CellStyle cellClientAdressValueStyle = wb.createCellStyle();
        cellClientAdressValueStyle.setFont(setFont((short) 11, false, false));
        cellClientAdressValue.setCellStyle(cellClientAdressValueStyle);
        cellClientAdressValue.setCellValue(entreprise.getAdresse());

        Cell cellClientAdressValue2 = headerRow4.createCell(4);
        cellClientAdressValue2.setCellStyle(setCellStyle(false, false, true, false));
        sheet.addMergedRegion(new CellRangeAddress(headerRow4.getRowNum(), headerRow4.getRowNum(), 2, 4));

        Row headerRow5 = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cellClient2 = headerRow5.createCell(0);
        cellClient2.setCellStyle(setCellStyle(true, false, false, true));

        Cell cellClientCP = headerRow5.createCell(1);
        CellStyle cellClientCPStyle = setCellStyle(false, false, false, true);
        cellClientCPStyle.setFont(setFont((short) 11, true, false));
        cellClientCP.setCellStyle(cellClientCPStyle);
        cellClientCP.setCellValue("CP :");

        Cell cellClientCPValue = headerRow5.createCell(2);
        CellStyle cellClientCPValueStyle = setCellStyle(false, false, false, true);
        cellClientCPValueStyle.setFont(setFont((short) 11, false, false));
        cellClientCPValue.setCellStyle(cellClientCPValueStyle);
        cellClientCPValue.setCellValue(entreprise.getCp().substring(0, 5));

        Cell cellClientVille = headerRow5.createCell(3);
        CellStyle cellClientVilleStyle = setCellStyle(false, false, false, true);
        cellClientVilleStyle.setFont(setFont((short) 11, true, false));
        cellClientVille.setCellStyle(cellClientVilleStyle);
        cellClientVille.setCellValue("Ville :");

        Cell cellClientVilleValue = headerRow5.createCell(4);
        CellStyle cellClientVilleValueStyle = setCellStyle(false, false, true, true);
        cellClientVilleValueStyle.setFont(setFont((short) 11, false, false));
        cellClientVilleValue.setCellStyle(cellClientVilleValueStyle);
        cellClientVilleValue.setCellValue(entreprise.getVille());

        //a regler avant le
        Cell cellDate = headerRow3.createCell(6);
        CellStyle cellDateStyle = setCellStyle(true, true, false, false);
        cellDateStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellDateStyle.setFont(setFont((short) 11, false, false));
        cellDate.setCellStyle(cellDateStyle);
        cellDate.setCellValue("Date :");

        Cell cellDateValue = headerRow3.createCell(7);
        CellStyle cellDateValueStyle = setCellStyle(false, true, true, false);
        cellDateValueStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellDateValueStyle.setFont(setFont((short) 11, false, false));
        cellDateValue.setCellStyle(cellDateValueStyle);
        cellDateValue.setCellValue(Date.getDate());

        Cell cellDate1 = headerRow4.createCell(6);
        cellDate1.setCellStyle(setCellStyle(true, false, false, false));

        Cell cellDate2 = headerRow4.createCell(7);
        cellDate2.setCellStyle(setCellStyle(false, false, true, false));

        Cell cellReglement = headerRow5.createCell(6);
        CellStyle cellReglementStyle = setCellStyle(true, false, false, true);
        cellReglementStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellReglementStyle.setFont(setFont((short) 11, false, false));
        cellReglement.setCellStyle(cellReglementStyle);
        cellReglement.setCellValue("A régler avant le :");

        Cell cellReglementValue = headerRow5.createCell(7);
        CellStyle cellReglementValueStyle = setCellStyle(false, false, true, true);
        cellReglementValueStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellReglementValueStyle.setFont(setFont((short) 11, false, false));
        cellReglementValue.setCellStyle(cellReglementValueStyle);
        cellReglementValue.setCellValue(Date.getARegler());

        if (!first)
            sheet.createRow(sheet.getLastRowNum() + 1);
    }

    private void creerCdnReg() {
        Row cdnRow6 = sheet.createRow(sheet.getLastRowNum() + 1);
        cdnRow6.setHeightInPoints(7.5f);

        Row cdnRow7 = sheet.createRow(sheet.getLastRowNum() + 1);
        cdnRow7.setHeightInPoints(18.8f);

        Cell cellCdn = cdnRow7.createCell(0);
        CellStyle cellCdnStyle = setCellStyle(true, true, false, false);
        cellCdnStyle.setFont(setFont((short) 11, true, false));
        cellCdn.setCellStyle(cellCdnStyle);
        cellCdn.setCellValue("Conditions de règlement : Virement");

        for (int i = 1; i < 7; i++)
            cdnRow7.createCell(i).setCellStyle(setCellStyle(false, true, false, false));

        Cell cellCdn7 = cdnRow7.createCell(7);
        cellCdn7.setCellStyle(setCellStyle(false, true, true, false));
        sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 7));

        Row cdnRow8 = sheet.createRow(sheet.getLastRowNum() + 1);
        cdnRow8.setHeightInPoints(45f);
        sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 7));
        //ajouterImage(MotsCles.IMGREG, 8, cdnRow8.getRowNum(), 20, -1, 1.0);

        Cell cellCdnVide = cdnRow8.createCell(0);
        cellCdnVide.setCellStyle(setCellStyle(true, false, false, false));

        Cell cellCdnVide2 = cdnRow8.createCell(7);
        cellCdnVide2.setCellStyle(setCellStyle(false, false, true, false));

        Row cdnRow9 = sheet.createRow(sheet.getLastRowNum() + 1);
        cdnRow9.setHeightInPoints((30f));

        Cell cellCdnPhrase = cdnRow9.createCell(0);
        CellStyle cellCdnPhraseStyle = setCellStyle(true, false, false, false);
        cellCdnPhraseStyle.setWrapText(true);
        cellCdnPhraseStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellCdnPhraseStyle.setFont(setFont((short) 9, false, false));
        cellCdnPhrase.setCellStyle(cellCdnPhraseStyle);
        cellCdnPhrase.setCellValue("Clause de réserve de propriété : le vendeur conserve la propriété pleine et entière des produits et services vendus jusqu'au paiement complet du prix, en application de la loi du 12/05/1980. Escompte pour paiement anticipé : néant.");

        Cell cellCdnPhrase1 = cdnRow9.createCell(7);
        cellCdnPhrase1.setCellStyle(setCellStyle(false, false, true, false));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 0, 7));

        Row cdnRow10 = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cellCdnPenal = cdnRow10.createCell(0);
        CellStyle cellCdnPenalStyle = setCellStyle(true, false, false, true);
        cellCdnPenalStyle.setFont(setFont((short) 9, false, false));
        cellCdnPenalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellCdnPenal.setCellStyle(cellCdnPenalStyle);
        cellCdnPenal.setCellValue("Pénalités de retard : taux légal en vigueur.");

        for (int i = 1; i < 6; i++)
            cdnRow10.createCell(i).setCellStyle(setCellStyle(false, false, false, true));

        sheet.addMergedRegion(new CellRangeAddress(10, 10, 0, 5));

        Cell cellCdnPenal6 = cdnRow10.createCell(6);
        CellStyle cellCdnPenal6Style = setCellStyle(false, false, false, true);
        cellCdnPenal6Style.setFont(setFont((short) 10, true, false));
        cellCdnPenal6Style.setVerticalAlignment(VerticalAlignment.CENTER);
        cellCdnPenal6Style.setAlignment(HorizontalAlignment.CENTER);
        cellCdnPenal6.setCellStyle(cellCdnPenal6Style);
        cellCdnPenal6.setCellValue("TVA sur les encaissements");

        Cell cellCdnPenal7 = cdnRow10.createCell(7);
        cellCdnPenal7.setCellStyle(setCellStyle(false, false, true, true));
        sheet.addMergedRegion(new CellRangeAddress(10, 10, 6, 7));
    }

    private void creerDetail() {
        //entete de colonnes
        Row detailRow12 = sheet.createRow(12);

        creerCell(detailRow12, MotsCles.NUM_COL_DAT_SAI, true, "Date Saisie");
        creerCell(detailRow12, MotsCles.NUM_COL_NOM_DOS_0, false, "Nom dossier");
        detailRow12.createCell(MotsCles.NUM_COL_NOM_DOS_1).setCellStyle(setCellStyle(false, true, false, true));
        detailRow12.createCell(MotsCles.NUM_COL_NOM_DOS_2).setCellStyle(setCellStyle(false, true, true, true));
        sheet.addMergedRegion(new CellRangeAddress(12, 12, MotsCles.NUM_COL_NOM_DOS_0, MotsCles.NUM_COL_NOM_DOS_2));
        creerCell(detailRow12, MotsCles.NUM_COL_NB_LI_0, false, "Nombre Ligne");
        detailRow12.createCell(MotsCles.NUM_COL_NB_LI_1).setCellStyle(setCellStyle(false, true, true, true));
        sheet.addMergedRegion(new CellRangeAddress(12, 12, MotsCles.NUM_COL_NB_LI_0, MotsCles.NUM_COL_NB_LI_1));
        creerCell(detailRow12, MotsCles.NUM_COL_TRF_LI, true, "Tarif Ligne");
        creerCell(detailRow12, MotsCles.NUM_COL_THT, true, "Total HT");

        //remplissage
        this.parseXls();

        if (isNotEmpty(dataLigne)) {
            creerEnteteType("Lignes");
            remplirLigne(parseLignes(entreprise));
        }

        remplirIfNotEmpty(dataIB, "Import Banque", MotsCles.TARIF_IB);
        remplirIfNotEmpty(data471, "471", MotsCles.TARIF_471);
        remplirIfNotEmpty(dataLe, "Lettrage", MotsCles.TARIF_LE);
        remplirIfNotEmpty(dataSF, "ScanFact", MotsCles.TARIF_SF);
        remplirIfNotEmpty(dataAI, "Attache Image", MotsCles.TARIF_AI);
        remplirIfNotEmpty(dataDP, "Découpe PDF", MotsCles.TARIF_DP);
        remplirIfNotEmpty(dataTVA, "TVA", MotsCles.TARIF_TVA);
        remplirIfNotEmpty(dataREV, "Révision", MotsCles.TARIF_REV);
        remplirIfNotEmpty(dataEI, "Ecritures Importées", MotsCles.TARIF_EI);
        remplirIfNotEmpty(dataLinkup, "Linkup", MotsCles.TARIF_LK);
        remplirIfNotEmpty(dataND, "Nouveaux Dossiers", MotsCles.TARIF_ND);
        remplirIfNotEmpty(dataAF, "Dossiers Spécifiques", MotsCles.TARIF_AF);
    }

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
        cellRemarqueStyle.setFont(setFont((short) 11, true, true));
        cellRemarque.setCellStyle(cellRemarqueStyle);
        cellRemarque.setCellValue("Remarques");

        Cell cellRemarqueValue = recapRow.createCell(1);
        cellRemarqueValue.setCellStyle(setCellStyle(true, true, false, false));
        int nvDos = createLigne(dataND, 16).size();
        cellRemarqueValue.setCellValue(nvDos < 1 ? nvDos + " Nouveau Dossier" : nvDos + " Nouveaux Dossiers");

        recapRow.createCell(2).setCellStyle(setCellStyle(false, true, true, false));

        Row recapRow2 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow2.createCell(1).setCellStyle(setCellStyle(true, false, false, false));
        recapRow2.createCell(2).setCellStyle(setCellStyle(false, false, true, false));

        Row recapRow3 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow3.createCell(1).setCellStyle(setCellStyle(true, false, false, false));
        recapRow3.createCell(2).setCellStyle(setCellStyle(false, false, true, false));

        Row recapRow4 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow4.createCell(1).setCellStyle(setCellStyle(true, false, false, false));
        recapRow4.createCell(2).setCellStyle(setCellStyle(false, false, true, false));

        Row recapRow5 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow5.createCell(1).setCellStyle(setCellStyle(true, false, false, false));
        recapRow5.createCell(2).setCellStyle(setCellStyle(false, false, true, false));

        Row recapRow6 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow6.createCell(1).setCellStyle(setCellStyle(true, false, false, true));
        recapRow6.createCell(2).setCellStyle(setCellStyle(false, false, true, true));

        //total
        Cell cellTotal = recapRow.createCell(4);
        CellStyle cellTotalStyle = setCellStyle(true, true, false, false);
        cellTotalStyle.setFont(setFont((short) 12, true, false));
        cellTotalStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotal.setCellStyle(cellTotalStyle);
        cellTotal.setCellValue("Total nombre de lignes");

        recapRow.createCell(5).setCellStyle(setCellStyle(false, true, false, false));
        recapRow.createCell(6).setCellStyle(setCellStyle(false, true, false, false));
        sheet.addMergedRegion(new CellRangeAddress(recapRow.getRowNum(), recapRow.getRowNum(), 4, 6));

        Cell cellTotalValue = recapRow.createCell(7);
        CellStyle cellTotalValueStyle = setCellStyle(false, true, true, false);
        cellTotalValueStyle.setFont(setFont((short) 12, true, false));
        cellTotalValueStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalValue.setCellStyle(cellTotalValueStyle);
        cellTotalValue.setCellValue(entreprise.getTotalLigne());

        recapRow2.createCell(4).setCellStyle(setCellStyle(true, false, false, false));
        recapRow2.createCell(7).setCellStyle(setCellStyle(false, false, true, false));

        Cell cellTotalHT = recapRow3.createCell(4);
        CellStyle cellTotalHTStyle = setCellStyle(true, false, false, false);
        cellTotalHTStyle.setFont(setFont((short) 11, false, false));
        cellTotalHTStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalHT.setCellStyle(cellTotalHTStyle);
        cellTotalHT.setCellValue("TOTAL € HT");

        sheet.addMergedRegion(new CellRangeAddress(recapRow3.getRowNum(), recapRow3.getRowNum(), 4, 6));

        Cell cellTotalHTValue = recapRow3.createCell(7);
        CellStyle cellTotalHTValueStyle = setCellStyle(false, false, true, false);
        cellTotalHTValueStyle.setFont(setFont((short) 11, false, false));
        cellTotalHTValueStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalHTValue.setCellStyle(cellTotalHTValueStyle);
        cellTotalHTValue.setCellValue(NumFormat.fDouble().format(entreprise.getTotalHT()) + " €");

        Cell cellTVA = recapRow4.createCell(4);
        CellStyle cellTVAStyle = setCellStyle(true, false, false, false);
        cellTVAStyle.setFont(setFont((short) 11, false, false));
        cellTVAStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTVA.setCellStyle(cellTVAStyle);
        cellTVA.setCellValue("TVA");

        sheet.addMergedRegion(new CellRangeAddress(recapRow4.getRowNum(), recapRow4.getRowNum(), 4, 6));

        Cell cellTVAValue = recapRow4.createCell(7);
        CellStyle cellTVAValueStyle = setCellStyle(false, false, true, false);
        cellTVAValueStyle.setFont(setFont((short) 11, false, false));
        cellTVAValueStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTVAValue.setCellStyle(cellTVAValueStyle);
        cellTVAValue.setCellValue(NumFormat.fDouble().format(entreprise.getTotalHT() * 0.2) + " €");

        Cell cellTotalTTC = recapRow5.createCell(4);
        CellStyle cellTotalTTCStyle = setCellStyle(true, false, false, false);
        cellTotalTTCStyle.setFont(setFont((short) 11, false, false));
        cellTotalTTCStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalTTC.setCellStyle(cellTotalTTCStyle);
        cellTotalTTC.setCellValue("TOTAL € TTC");

        sheet.addMergedRegion(new CellRangeAddress(recapRow5.getRowNum(), recapRow5.getRowNum(), 4, 6));

        Cell cellTotalTTCValue = recapRow5.createCell(7);
        CellStyle cellTotalTTCValueStyle = setCellStyle(false, false, true, false);
        cellTotalTTCValueStyle.setFont(setFont((short) 11, false, false));
        cellTotalTTCValueStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalTTCValue.setCellStyle(cellTotalTTCValueStyle);
        cellTotalTTCValue.setCellValue(NumFormat.fDouble().format(entreprise.getTotalHT() * 1.2) + " €");

        recapRow6.createCell(4).setCellStyle(setCellStyle(true, false, false, true));
        recapRow6.createCell(5).setCellStyle(setCellStyle(false, false, false, true));
        recapRow6.createCell(6).setCellStyle(setCellStyle(false, false, false, true));
        recapRow6.createCell(7).setCellStyle(setCellStyle(false, false, true, true));
    }

    private void creerEnteteType(String entete) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        Cell cell = row.createCell(0);
        CellStyle cellStyle = setCellStyle(true, true, false, true);
        cellStyle.setFont(setFont((short) 12, true, true));
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(entete);

        for (int i = 1; i < 7; i++)
            row.createCell(i).setCellStyle(setCellStyle(false, true, false, true));

        row.createCell(7).setCellStyle(setCellStyle(false, true, true, true));
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 7));
    }

    private void remplirLigne(@NotNull List<Ligne> data) {
        CellStyle cellStyle = setCellStyle(true, true, true, true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(setFont((short) 11, false, false));
        cellStyle.setShrinkToFit(true);

        for (Ligne ligne : data) {
            if (sheet.getLastRowNum() == 39 || sheet.getLastRowNum() % 41 == 0)
                creerEntete(false);

            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            creerCell(row, MotsCles.NUM_COL_DAT_SAI, cellStyle, ligne.getDate());
            creerCell(row, MotsCles.NUM_COL_NOM_DOS_0, cellStyle, ligne.getEntreprise());
            for (int i = MotsCles.NUM_COL_NOM_DOS_1; i <= MotsCles.NUM_COL_NOM_DOS_2; i++)
                row.createCell(i).setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), MotsCles.NUM_COL_NOM_DOS_0, MotsCles.NUM_COL_NOM_DOS_2));
            creerCell(row, MotsCles.NUM_COL_NB_LI_0, cellStyle, NumFormat.fEntier().format(ligne.getNbLigne()));
            row.createCell(MotsCles.NUM_COL_NB_LI_1).setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), MotsCles.NUM_COL_NB_LI_0, MotsCles.NUM_COL_NB_LI_1));
            creerCell(row, MotsCles.NUM_COL_TRF_LI, cellStyle, NumFormat.fTriple().format(ligne.getTarif()));
            creerCell(row, MotsCles.NUM_COL_THT, cellStyle, NumFormat.fDouble().format(ligne.getTotal()) + "€");

            totalLigne += ligne.getNbLigne();
            totalHT += ligne.getTotal();
        }

        entreprise.setTotalLigne(totalLigne);
        entreprise.setTotalHT(totalHT);
    }

    private void ajouterImage(String fileName, Integer col2, Integer row1, Integer dx1, Integer dy1, Double resize2) {
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
        byte[] bytes = new byte[0];
        try {
            assert is != null;
            bytes = IOUtils.toByteArray(is);
        } catch (IOException e) {
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
        int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        try {
            is.close();
        } catch (IOException e) {
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }

        CreationHelper helper = wb.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();

        anchor.setCol1(0);
        if (col2 > -1)
            anchor.setCol2(col2);

        anchor.setRow1(row1);

        if (dx1 > -1)
            anchor.setDx1(dx1);

        if (dy1 > -1)
            anchor.setDy1(dy1);

        Picture picture = drawing.createPicture(anchor, pictureIdx);
        picture.resize(1.0, resize2);
    }

    @NotNull
    private Font setFont(Short size, @NotNull Boolean bold, Boolean blue) {
        Font font = wb.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints(size);
        if (bold) font.setBold(bold);
        if (blue) font.setColor(IndexedColors.DARK_BLUE.getIndex());

        return font;
    }

    private CellStyle setCellStyle(@NotNull Boolean left, Boolean top, Boolean right, Boolean bottom) {
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

    private void remplirIfNotEmpty(List<List<String>> data, String entete, int tarif) {
        if (isNotEmpty(data)) {
            creerEnteteType(entete);
            remplirLigne(createLigne(data, tarif));
        }
    }

    private void creerCell(@NotNull Row row, int col, boolean right, String entete) {
        Cell cell = row.createCell(col);
        CellStyle cellStyle = setCellStyle(true, true, right, true);
        cellStyle.setFont(setFont((short) 12, true, true));
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(entete);
    }

    private void creerCell(@NotNull Row row, int col, CellStyle cellStyle, String texte) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(texte);
    }
}
