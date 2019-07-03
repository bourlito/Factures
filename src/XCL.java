import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;

import java.io.*;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

class XCL extends Mamasita implements MotsCles {
    private int totalLigne = 0;
    private double totalHT = 0;

    void creerXCL(HSSFSheet sheet, String filename, int nFacture, Entreprise entreprise) {
        Workbook wb = new HSSFWorkbook();
        creerFeuille(wb, sheet, entreprise, nFacture);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            creerFichierErreur(e);
            e.printStackTrace();
        }
        try {
            wb.write(out);
            if (out != null)
                out.close();
            wb.close();
        } catch (IOException e) {
            creerFichierErreur(e);
            e.printStackTrace();
        }
    }

    private void creerFeuille(Workbook wb, HSSFSheet sheet1, Entreprise entreprise, int nFacture) {
        Sheet sheet = wb.createSheet("Facture");
        sheet.setDefaultRowHeightInPoints(15f);
        sheet.setDefaultColumnWidth(10);
        sheet.setColumnWidth(5, 256 * 5);
        sheet.setColumnWidth(6, 256 * 16);
        sheet.setColumnWidth(7, 256 * 12);
        creerEntete(wb, sheet, entreprise, nFacture, true);
        creerCdnReg(wb, sheet);
        creerDetail(wb, sheet, entreprise, sheet1, nFacture);
        creerRecap(wb, sheet, entreprise, nFacture);
    }

    private void creerEntete(Workbook wb, Sheet sheet, Entreprise entreprise, int nFacture, Boolean first) {
        //info cpe + n°facture
        Row headerRow0 = sheet.createRow(sheet.getLastRowNum() + 1 + (first ? -1 : 0));
        headerRow0.setHeightInPoints(78.8f);
        ajouterImage(wb, sheet, IMG, 0,-1,headerRow0.getRowNum(),-1,30,1.0,0.9);

        Cell cellCPE = headerRow0.createCell(1);
        CellStyle cellCPEStyle = wb.createCellStyle();
        cellCPEStyle.setWrapText(true);
        cellCPEStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellCPEStyle.setFont(setFont(wb, (short) 11, false, false));
        cellCPE.setCellStyle(cellCPEStyle);
        cellCPE.setCellValue(adresseCPE);
        sheet.addMergedRegion(new CellRangeAddress(headerRow0.getRowNum(), headerRow0.getRowNum(), 1, 5));

        Cell cellFact = headerRow0.createCell(6);
        CellStyle cellFactStyle = wb.createCellStyle();
        cellFactStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellFactStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellFactStyle.setFont(setFont(wb, (short) 14, true, false));
        cellFact.setCellStyle(cellFactStyle);
        cellFact.setCellValue("FACTURE N°");

        Cell cellNOFact = headerRow0.createCell(7);
        CellStyle cellNOFactStyle = wb.createCellStyle();
        cellNOFactStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellNOFactStyle.setAlignment(HorizontalAlignment.CENTER);
        cellNOFactStyle.setFont(setFont(wb, (short) 14, false, false));
        cellNOFact.setCellStyle(cellNOFactStyle);
        NumberFormat nbFactFormat = NumberFormat.getInstance(Locale.FRANCE);
        nbFactFormat.setMinimumIntegerDigits(3);
        cellNOFact.setCellValue(Calendar.getInstance().get(Calendar.YEAR) + "-" + nbFactFormat.format(nFacture));

        //separation
        Row headerRow1 = sheet.createRow(sheet.getLastRowNum() + 1);
        headerRow1.setHeightInPoints(6f);

        for (int i = 0; i < 8; i++) {
            Cell cellBleue = headerRow1.createCell(i);
            CellStyle cellBleueStyle = setCellStyle(wb, true, true, true, true);
            cellBleueStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            cellBleueStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellBleue.setCellStyle(cellBleueStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(headerRow1.getRowNum(), headerRow1.getRowNum(), 0, 7));

        sheet.createRow(sheet.getLastRowNum() + 1);

        //partie client
        Row headerRow3 = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cellClient = headerRow3.createCell(0);
        CellStyle cellClientStyle = setCellStyle(wb, true, true, false, false);
        cellClientStyle.setAlignment(HorizontalAlignment.CENTER);
        cellClientStyle.setFont(setFont(wb, (short) 11, true, false));
        cellClient.setCellStyle(cellClientStyle);
        cellClient.setCellValue("Client");

        Cell cellClientNom = headerRow3.createCell(1);
        CellStyle cellClientNomStyle = setCellStyle(wb, false, true, false, false);
        cellClientNomStyle.setFont(setFont(wb, (short) 11, true, false));
        cellClientNom.setCellStyle(cellClientNomStyle);
        cellClientNom.setCellValue("Nom :");

        Cell cellClientNomValue = headerRow3.createCell(2);
        CellStyle cellClientNomValueStyle = setCellStyle(wb, false, true, false, false);
        cellClientNomValueStyle.setFont(setFont(wb, (short) 11, false, false));
        cellClientNomValue.setCellStyle(cellClientNomValueStyle);
        cellClientNomValue.setCellValue(entreprise.getNomEntreprise());

        Cell cellClientNomValue1 = headerRow3.createCell(3);
        cellClientNomValue1.setCellStyle(setCellStyle(wb, false, true, false, false));

        Cell cellClientNomValue2 = headerRow3.createCell(4);
        cellClientNomValue2.setCellStyle(setCellStyle(wb, false, true, true, false));
        sheet.addMergedRegion(new CellRangeAddress(headerRow3.getRowNum(), headerRow3.getRowNum(), 2, 4));

        Row headerRow4 = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cellClient1 = headerRow4.createCell(0);
        cellClient1.setCellStyle(setCellStyle(wb, true, false, false, false));

        Cell cellClientAdress = headerRow4.createCell(1);
        CellStyle cellClientAdressStyle = wb.createCellStyle();
        cellClientAdressStyle.setFont(setFont(wb, (short) 11, true, false));
        cellClientAdress.setCellStyle(cellClientAdressStyle);
        cellClientAdress.setCellValue("Adresse :");

        Cell cellClientAdressValue = headerRow4.createCell(2);
        CellStyle cellClientAdressValueStyle = wb.createCellStyle();
        cellClientAdressValueStyle.setFont(setFont(wb, (short) 11, false, false));
        cellClientAdressValue.setCellStyle(cellClientAdressValueStyle);
        cellClientAdressValue.setCellValue(entreprise.getAdresse());

        Cell cellClientAdressValue2 = headerRow4.createCell(4);
        cellClientAdressValue2.setCellStyle(setCellStyle(wb, false, false, true, false));
        sheet.addMergedRegion(new CellRangeAddress(headerRow4.getRowNum(), headerRow4.getRowNum(), 2, 4));

        Row headerRow5 = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cellClient2 = headerRow5.createCell(0);
        cellClient2.setCellStyle(setCellStyle(wb, true, false, false, true));

        Cell cellClientCP = headerRow5.createCell(1);
        CellStyle cellClientCPStyle = setCellStyle(wb, false, false, false, true);
        cellClientCPStyle.setFont(setFont(wb, (short) 11, true, false));
        cellClientCP.setCellStyle(cellClientCPStyle);
        cellClientCP.setCellValue("CP :");

        Cell cellClientCPValue = headerRow5.createCell(2);
        CellStyle cellClientCPValueStyle = setCellStyle(wb, false, false, false, true);
        cellClientCPValueStyle.setFont(setFont(wb, (short) 11, false, false));
        cellClientCPValue.setCellStyle(cellClientCPValueStyle);
        cellClientCPValue.setCellValue(entreprise.getCp().substring(0, 5));

        Cell cellClientVille = headerRow5.createCell(3);
        CellStyle cellClientVilleStyle = setCellStyle(wb, false, false, false, true);
        cellClientVilleStyle.setFont(setFont(wb, (short) 11, true, false));
        cellClientVille.setCellStyle(cellClientVilleStyle);
        cellClientVille.setCellValue("Ville :");

        Cell cellClientVilleValue = headerRow5.createCell(4);
        CellStyle cellClientVilleValueStyle = setCellStyle(wb, false, false, true, true);
        cellClientVilleValueStyle.setFont(setFont(wb, (short) 11, false, false));
        cellClientVilleValue.setCellStyle(cellClientVilleValueStyle);
        cellClientVilleValue.setCellValue(entreprise.getVille());

        //a regler avant le
        Cell cellDate = headerRow3.createCell(6);
        CellStyle cellDateStyle = setCellStyle(wb, true, true, false, false);
        cellDateStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellDateStyle.setFont(setFont(wb, (short) 11, false, false));
        cellDate.setCellStyle(cellDateStyle);
        cellDate.setCellValue("Date :");

        Cell cellDateValue = headerRow3.createCell(7);
        CellStyle cellDateValueStyle = setCellStyle(wb, false, true, true, false);
        cellDateValueStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellDateValueStyle.setFont(setFont(wb, (short) 11, false, false));
        cellDateValue.setCellStyle(cellDateValueStyle);
        cellDateValue.setCellValue(setDate().get(0));

        Cell cellDate1 = headerRow4.createCell(6);
        cellDate1.setCellStyle(setCellStyle(wb, true, false, false, false));

        Cell cellDate2 = headerRow4.createCell(7);
        cellDate2.setCellStyle(setCellStyle(wb, false, false, true, false));

        Cell cellReglement = headerRow5.createCell(6);
        CellStyle cellReglementStyle = setCellStyle(wb, true, false, false, true);
        cellReglementStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellReglementStyle.setFont(setFont(wb, (short) 11, false, false));
        cellReglement.setCellStyle(cellReglementStyle);
        cellReglement.setCellValue("A régler avant le :");

        Cell cellReglementValue = headerRow5.createCell(7);
        CellStyle cellReglementValueStyle = setCellStyle(wb, false, false, true, true);
        cellReglementValueStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellReglementValueStyle.setFont(setFont(wb, (short) 11, false, false));
        cellReglementValue.setCellStyle(cellReglementValueStyle);
        cellReglementValue.setCellValue(setDate().get(1));

        if (!first)
            sheet.createRow(sheet.getLastRowNum() + 1);
    }

    private void creerCdnReg(Workbook wb, Sheet sheet) {
        Row cdnRow6 = sheet.createRow(sheet.getLastRowNum() + 1);
        cdnRow6.setHeightInPoints(7.5f);

        Row cdnRow7 = sheet.createRow(sheet.getLastRowNum() + 1);
        cdnRow7.setHeightInPoints(18.8f);

        Cell cellCdn = cdnRow7.createCell(0);
        CellStyle cellCdnStyle = setCellStyle(wb, true, true, false, false);
        cellCdnStyle.setFont(setFont(wb, (short) 11, true, false));
        cellCdn.setCellStyle(cellCdnStyle);
        cellCdn.setCellValue("Conditions de règlement : Virement");

        for (int i = 1; i < 7; i++)
            cdnRow7.createCell(i).setCellStyle(setCellStyle(wb, false, true, false, false));

        Cell cellCdn7 = cdnRow7.createCell(7);
        cellCdn7.setCellStyle(setCellStyle(wb, false, true, true, false));
        sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 7));

        Row cdnRow8 = sheet.createRow(sheet.getLastRowNum() + 1);
        cdnRow8.setHeightInPoints(45f);
        sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 7));
        ajouterImage(wb,sheet,IMGREG,0,8,cdnRow8.getRowNum(),20,-1,1.0,1.0);

        Cell cellCdnVide = cdnRow8.createCell(0);
        cellCdnVide.setCellStyle(setCellStyle(wb, true, false, false, false));

        Cell cellCdnVide2 = cdnRow8.createCell(7);
        cellCdnVide2.setCellStyle(setCellStyle(wb, false, false, true, false));

        Row cdnRow9 = sheet.createRow(sheet.getLastRowNum() + 1);
        cdnRow9.setHeightInPoints((30f));

        Cell cellCdnPhrase = cdnRow9.createCell(0);
        CellStyle cellCdnPhraseStyle = setCellStyle(wb, true, false, false, false);
        cellCdnPhraseStyle.setWrapText(true);
        cellCdnPhraseStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellCdnPhraseStyle.setFont(setFont(wb, (short) 9, false, false));
        cellCdnPhrase.setCellStyle(cellCdnPhraseStyle);
        cellCdnPhrase.setCellValue("Clause de réserve de propriété : le vendeur conserve la propriété pleine et entière des produits et services vendus jusqu'au paiement complet du prix, en application de la loi du 12/05/1980. Escompte pour paiement anticipé : néant.");

        Cell cellCdnPhrase1 = cdnRow9.createCell(7);
        cellCdnPhrase1.setCellStyle(setCellStyle(wb, false, false, true, false));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 0, 7));

        Row cdnRow10 = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell cellCdnPenal = cdnRow10.createCell(0);
        CellStyle cellCdnPenalStyle = setCellStyle(wb, true, false, false, true);
        cellCdnPenalStyle.setFont(setFont(wb, (short) 9, false, false));
        cellCdnPenalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellCdnPenal.setCellStyle(cellCdnPenalStyle);
        cellCdnPenal.setCellValue("Pénalités de retard : taux légal en vigueur.");

        for (int i = 1; i < 6; i++)
            cdnRow10.createCell(i).setCellStyle(setCellStyle(wb, false, false, false, true));

        sheet.addMergedRegion(new CellRangeAddress(10, 10, 0, 5));

        Cell cellCdnPenal6 = cdnRow10.createCell(6);
        CellStyle cellCdnPenal6Style = setCellStyle(wb, false, false, false, true);
        cellCdnPenal6Style.setFont(setFont(wb, (short) 10, true, false));
        cellCdnPenal6Style.setVerticalAlignment(VerticalAlignment.CENTER);
        cellCdnPenal6Style.setAlignment(HorizontalAlignment.CENTER);
        cellCdnPenal6.setCellStyle(cellCdnPenal6Style);
        cellCdnPenal6.setCellValue("TVA sur les encaissements");

        Cell cellCdnPenal7 = cdnRow10.createCell(7);
        cellCdnPenal7.setCellStyle(setCellStyle(wb, false, false, true, true));
        sheet.addMergedRegion(new CellRangeAddress(10, 10, 6, 7));
    }

    private void creerDetail(Workbook wb, Sheet sheet, Entreprise entreprise, HSSFSheet sheet1, int nFacture) {
        //entete de colonnes
        Row detailRow12 = sheet.createRow(12);

        Cell cellDate = detailRow12.createCell(0);
        CellStyle cellDateStyle = setCellStyle(wb, true, true, true, true);
        cellDateStyle.setFont(setFont(wb, (short) 12, true, true));
        cellDateStyle.setAlignment(HorizontalAlignment.CENTER);
        cellDate.setCellStyle(cellDateStyle);
        cellDate.setCellValue("Date Saisie");

        Cell cellNomDossier = detailRow12.createCell(1);
        CellStyle cellNomDossierStyle = setCellStyle(wb, true, true, false, true);
        cellNomDossierStyle.setFont(setFont(wb, (short) 12, true, true));
        cellNomDossierStyle.setAlignment(HorizontalAlignment.CENTER);
        cellNomDossier.setCellStyle(cellNomDossierStyle);
        cellNomDossier.setCellValue("Nom dossier");

        detailRow12.createCell(2).setCellStyle(setCellStyle(wb, false, true, false, true));
        detailRow12.createCell(3).setCellStyle(setCellStyle(wb, false, true, true, true));
        sheet.addMergedRegion(new CellRangeAddress(12, 12, 1, 3));

        Cell cellTarif = detailRow12.createCell(4);
        CellStyle cellTarifStyle = setCellStyle(wb, true, true, false, true);
        cellTarifStyle.setFont(setFont(wb, (short) 12, true, true));
        cellTarifStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTarif.setCellStyle(cellTarifStyle);
        cellTarif.setCellValue("Nombre Lignes");

        detailRow12.createCell(5).setCellStyle(setCellStyle(wb, false, true, true, true));
        sheet.addMergedRegion(new CellRangeAddress(12, 12, 4, 5));

        Cell cellNombreLigne = detailRow12.createCell(6);
        CellStyle cellNombreLigneStyle = setCellStyle(wb, true, true, true, true);
        cellNombreLigneStyle.setFont(setFont(wb, (short) 12, true, true));
        cellNombreLigneStyle.setAlignment(HorizontalAlignment.CENTER);
        cellNombreLigne.setCellStyle(cellNombreLigneStyle);
        cellNombreLigne.setCellValue("Tarif Ligne");

        Cell cellTotalHT = detailRow12.createCell(7);
        CellStyle cellTotalHTStyle = setCellStyle(wb, true, true, true, true);
        cellTotalHTStyle.setFont(setFont(wb, (short) 12, true, true));
        cellTotalHTStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalHT.setCellStyle(cellTotalHTStyle);
        cellTotalHT.setCellValue("Total HT");

        //remplissage
        readXls(sheet1);
        parseXls();

        if (isNotEmpty(dataLigne)) {
            creerEnteteType(wb, sheet, "Lignes");
            remplirLigne(wb, sheet, parseLignes(entreprise), entreprise, nFacture);
        }

        remplirIfNotEmpty(dataIB, wb, sheet, "Import Banque", TARIF_IB, entreprise, nFacture);
        remplirIfNotEmpty(data471, wb, sheet, "471", TARIF_471, entreprise, nFacture);
        remplirIfNotEmpty(dataLe, wb, sheet, "Lettrage", TARIF_LE, entreprise, nFacture);
        remplirIfNotEmpty(dataSF, wb, sheet, "ScanFact", TARIF_SF, entreprise, nFacture);
        remplirIfNotEmpty(dataAI, wb, sheet, "Attache Image", TARIF_AI, entreprise, nFacture);
        remplirIfNotEmpty(dataDP, wb, sheet, "Découpe PDF", TARIF_DP, entreprise, nFacture);
        remplirIfNotEmpty(dataTVA, wb, sheet, "TVA", TARIF_TVA, entreprise, nFacture);
        remplirIfNotEmpty(dataREV, wb, sheet, "Révision", TARIF_REV, entreprise, nFacture);
        remplirIfNotEmpty(dataEI, wb, sheet, "Ecritures Importées", TARIF_EI, entreprise, nFacture);
        remplirIfNotEmpty(dataLinkup, wb, sheet, "Linkup", TARIF_LK, entreprise, nFacture);
        remplirIfNotEmpty(dataND, wb, sheet, "Nouveaux Dossiers", TARIF_ND, entreprise, nFacture);
        remplirIfNotEmpty(dataAF, wb, sheet, "Dossiers Spécifiques", TARIF_AF, entreprise, nFacture);
    }

    private void creerRecap(Workbook wb, Sheet sheet, Entreprise entreprise, int nFacture) {
        List<Integer> rowNum = new ArrayList<>();
        rowNum.add(0);
        for (int i = 35; i < 41; i++)
            rowNum.add(i);

        if (rowNum.contains(sheet.getLastRowNum() % 41))
            creerEntete(wb, sheet, entreprise, nFacture, false);

        NumberFormat formatDouble = NumberFormat.getInstance(Locale.FRANCE);
        formatDouble.setMaximumFractionDigits(2);
        formatDouble.setMinimumFractionDigits(2);
        formatDouble.setRoundingMode(RoundingMode.HALF_UP);

        //remarques
        sheet.createRow(sheet.getLastRowNum() + 1);

        Row recapRow = sheet.createRow(sheet.getLastRowNum() + 1);
        Cell cellRemarque = recapRow.createCell(0);
        CellStyle cellRemarqueStyle = wb.createCellStyle();
        cellRemarqueStyle.setFont(setFont(wb, (short) 11, true, true));
        cellRemarque.setCellStyle(cellRemarqueStyle);
        cellRemarque.setCellValue("Remarques");

        Cell cellRemarqueValue = recapRow.createCell(1);
        cellRemarqueValue.setCellStyle(setCellStyle(wb, true, true, false, false));
        int nvDos = createLigne(dataND, 16, entreprise).size();
        cellRemarqueValue.setCellValue(nvDos < 1 ? nvDos + " Nouveau Dossier" : nvDos + " Nouveaux Dossiers");

        recapRow.createCell(2).setCellStyle(setCellStyle(wb, false, true, true, false));

        Row recapRow2 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow2.createCell(1).setCellStyle(setCellStyle(wb, true, false, false, false));
        recapRow2.createCell(2).setCellStyle(setCellStyle(wb, false, false, true, false));

        Row recapRow3 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow3.createCell(1).setCellStyle(setCellStyle(wb, true, false, false, false));
        recapRow3.createCell(2).setCellStyle(setCellStyle(wb, false, false, true, false));

        Row recapRow4 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow4.createCell(1).setCellStyle(setCellStyle(wb, true, false, false, false));
        recapRow4.createCell(2).setCellStyle(setCellStyle(wb, false, false, true, false));

        Row recapRow5 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow5.createCell(1).setCellStyle(setCellStyle(wb, true, false, false, false));
        recapRow5.createCell(2).setCellStyle(setCellStyle(wb, false, false, true, false));

        Row recapRow6 = sheet.createRow(sheet.getLastRowNum() + 1);
        recapRow6.createCell(1).setCellStyle(setCellStyle(wb, true, false, false, true));
        recapRow6.createCell(2).setCellStyle(setCellStyle(wb, false, false, true, true));

        //total
        Cell cellTotal = recapRow.createCell(4);
        CellStyle cellTotalStyle = setCellStyle(wb, true, true, false, false);
        cellTotalStyle.setFont(setFont(wb, (short) 12, true, false));
        cellTotalStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotal.setCellStyle(cellTotalStyle);
        cellTotal.setCellValue("Total nombre de lignes");

        recapRow.createCell(5).setCellStyle(setCellStyle(wb, false, true, false, false));
        recapRow.createCell(6).setCellStyle(setCellStyle(wb, false, true, false, false));
        sheet.addMergedRegion(new CellRangeAddress(recapRow.getRowNum(), recapRow.getRowNum(), 4, 6));

        Cell cellTotalValue = recapRow.createCell(7);
        CellStyle cellTotalValueStyle = setCellStyle(wb, false, true, true, false);
        cellTotalValueStyle.setFont(setFont(wb, (short) 12, true, false));
        cellTotalValueStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalValue.setCellStyle(cellTotalValueStyle);
        cellTotalValue.setCellValue(entreprise.getTotalLigne());

        recapRow2.createCell(4).setCellStyle(setCellStyle(wb, true, false, false, false));
        recapRow2.createCell(7).setCellStyle(setCellStyle(wb, false, false, true, false));

        Cell cellTotalHT = recapRow3.createCell(4);
        CellStyle cellTotalHTStyle = setCellStyle(wb, true, false, false, false);
        cellTotalHTStyle.setFont(setFont(wb, (short) 11, false, false));
        cellTotalHTStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalHT.setCellStyle(cellTotalHTStyle);
        cellTotalHT.setCellValue("TOTAL € HT");

        sheet.addMergedRegion(new CellRangeAddress(recapRow3.getRowNum(), recapRow3.getRowNum(), 4, 6));

        Cell cellTotalHTValue = recapRow3.createCell(7);
        CellStyle cellTotalHTValueStyle = setCellStyle(wb, false, false, true, false);
        cellTotalHTValueStyle.setFont(setFont(wb, (short) 11, false, false));
        cellTotalHTValueStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalHTValue.setCellStyle(cellTotalHTValueStyle);
        cellTotalHTValue.setCellValue(formatDouble.format(entreprise.getTotalHT()) + " €");

        Cell cellTVA = recapRow4.createCell(4);
        CellStyle cellTVAStyle = setCellStyle(wb, true, false, false, false);
        cellTVAStyle.setFont(setFont(wb, (short) 11, false, false));
        cellTVAStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTVA.setCellStyle(cellTVAStyle);
        cellTVA.setCellValue("TVA");

        sheet.addMergedRegion(new CellRangeAddress(recapRow4.getRowNum(), recapRow4.getRowNum(), 4, 6));

        Cell cellTVAValue = recapRow4.createCell(7);
        CellStyle cellTVAValueStyle = setCellStyle(wb, false, false, true, false);
        cellTVAValueStyle.setFont(setFont(wb, (short) 11, false, false));
        cellTVAValueStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTVAValue.setCellStyle(cellTVAValueStyle);
        cellTVAValue.setCellValue(formatDouble.format(entreprise.getTotalHT() * 0.2) + " €");

        Cell cellTotalTTC = recapRow5.createCell(4);
        CellStyle cellTotalTTCStyle = setCellStyle(wb, true, false, false, false);
        cellTotalTTCStyle.setFont(setFont(wb, (short) 11, false, false));
        cellTotalTTCStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalTTC.setCellStyle(cellTotalTTCStyle);
        cellTotalTTC.setCellValue("TOTAL € TTC");

        sheet.addMergedRegion(new CellRangeAddress(recapRow5.getRowNum(), recapRow5.getRowNum(), 4, 6));

        Cell cellTotalTTCValue = recapRow5.createCell(7);
        CellStyle cellTotalTTCValueStyle = setCellStyle(wb, false, false, true, false);
        cellTotalTTCValueStyle.setFont(setFont(wb, (short) 11, false, false));
        cellTotalTTCValueStyle.setAlignment(HorizontalAlignment.CENTER);
        cellTotalTTCValue.setCellStyle(cellTotalTTCValueStyle);
        cellTotalTTCValue.setCellValue(formatDouble.format(entreprise.getTotalHT() * 1.2) + " €");

        recapRow6.createCell(4).setCellStyle(setCellStyle(wb, true, false, false, true));
        recapRow6.createCell(5).setCellStyle(setCellStyle(wb, false, false, false, true));
        recapRow6.createCell(6).setCellStyle(setCellStyle(wb, false, false, false, true));
        recapRow6.createCell(7).setCellStyle(setCellStyle(wb, false, false, true, true));
    }

    private void creerEnteteType(Workbook wb, Sheet sheet, String entete) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        Cell cell = row.createCell(0);
        CellStyle cellStyle = setCellStyle(wb, true, true, false, true);
        cellStyle.setFont(setFont(wb, (short) 12, true, true));
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(entete);

        for (int i = 1; i < 7; i++)
            row.createCell(i).setCellStyle(setCellStyle(wb, false, true, false, true));

        row.createCell(7).setCellStyle(setCellStyle(wb, false, true, true, true));
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 7));
    }

    private void remplirLigne(Workbook wb, Sheet sheet, List<Ligne> data, Entreprise entreprise, int nFacture) {
        NumberFormat formatDouble = NumberFormat.getInstance(Locale.FRANCE);
        formatDouble.setMaximumFractionDigits(2);
        formatDouble.setMinimumFractionDigits(2);
        formatDouble.setRoundingMode(RoundingMode.HALF_UP);

        NumberFormat formatTriple = NumberFormat.getInstance(Locale.FRANCE);
        formatTriple.setMinimumFractionDigits(2);

        NumberFormat formatEntier = NumberFormat.getInstance(Locale.FRANCE);
        formatEntier.setMaximumFractionDigits(0);

        for (Ligne ligne : data) {
            if (sheet.getLastRowNum() == 39 || sheet.getLastRowNum() % 41 == 0)
                creerEntete(wb, sheet, entreprise, nFacture, false);

            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            CellStyle cellStyle = setCellStyle(wb, true, true, true, true);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setFont(setFont(wb, (short) 11, false, false));
            cellStyle.setShrinkToFit(true);

            Cell cellDate = row.createCell(0);
            cellDate.setCellStyle(cellStyle);
            cellDate.setCellValue(ligne.getDate());

            Cell cellDossier = row.createCell(1);
            cellDossier.setCellStyle(cellStyle);
            cellDossier.setCellValue(ligne.getEntreprise());

            for (int i = 2; i < 4; i++)
                row.createCell(i).setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 3));

            Cell cellTarif = row.createCell(4);
            cellTarif.setCellStyle(cellStyle);
            cellTarif.setCellValue(formatEntier.format(ligne.getNbLigne()));

            row.createCell(5).setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 4, 5));

            Cell cellNbLigne = row.createCell(6);
            cellNbLigne.setCellStyle(cellStyle);
            cellNbLigne.setCellValue(formatTriple.format(ligne.getTarif()));

            Cell cellTotal = row.createCell(7);
            cellTotal.setCellStyle(cellStyle);
            cellTotal.setCellValue(formatDouble.format(ligne.getTotal()) + "€");

            totalLigne += ligne.getNbLigne();
            totalHT += ligne.getTotal();
        }

        entreprise.setTotalLigne(totalLigne);
        entreprise.setTotalHT(totalHT);
    }

    private void ajouterImage(Workbook wb, Sheet sheet, String fileName, Integer col1, Integer col2, Integer row1, Integer dx1, Integer dy1, Double resize1, Double resize2) {
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            creerFichierErreur(e);
            e.printStackTrace();
        }
        byte[] bytes = new byte[0];
        try {
            bytes = IOUtils.toByteArray(is);
        } catch (IOException e) {
            creerFichierErreur(e);
            e.printStackTrace();
        }
        int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        try {
            is.close();
        } catch (IOException e) {
            creerFichierErreur(e);
            e.printStackTrace();
        }

        CreationHelper helper = wb.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();

        anchor.setCol1(col1);
        if (col2 > -1)
            anchor.setCol2(col2);

        anchor.setRow1(row1);

        if (dx1 > -1)
            anchor.setDx1(dx1);

        if (dy1 > -1)
            anchor.setDy1(dy1);

        Picture picture = drawing.createPicture(anchor, pictureIdx);
        picture.resize(resize1, resize2);
    }

    private ArrayList<String> setDate() {
        ArrayList<String> listDate = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        switch (Calendar.getInstance().get(Calendar.MONTH)) {
            case 0:
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
                sb.append("31/");
                break;
            case 2:
                sb.append("28/");
                break;
            default:
                sb.append("30/");
                break;
        }

        if (Calendar.getInstance().get(Calendar.MONTH) == 0) {
            sb.append("12/");
            sb.append(Calendar.getInstance().get(Calendar.YEAR) - 1);
        } else {
            sb.append(Calendar.getInstance().get(Calendar.MONTH));
            sb.append("/");
            sb.append(Calendar.getInstance().get(Calendar.YEAR));
        }
        listDate.add(sb.toString());

        sb = new StringBuilder();
        sb.append("20/");
        sb.append(Calendar.getInstance().get(Calendar.MONTH) + 1);
        sb.append("/");
        sb.append(Calendar.getInstance().get(Calendar.YEAR));
        listDate.add(sb.toString());

        return listDate;
    }

    private Font setFont(Workbook wb, Short size, Boolean bold, Boolean blue) {
        Font font = wb.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints(size);
        if (bold) font.setBold(bold);
        if (blue) font.setColor(IndexedColors.DARK_BLUE.getIndex());

        return font;
    }

    private CellStyle setCellStyle(Workbook wb, Boolean left, Boolean top, Boolean right, Boolean bottom) {
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

    private void remplirIfNotEmpty(List<List<String>> data, Workbook wb, Sheet sheet, String entete, int tarif, Entreprise entreprise, int nFacture) {
        if (isNotEmpty(data)) {
            creerEnteteType(wb, sheet, entete);
            remplirLigne(wb, sheet, createLigne(data, tarif, entreprise), entreprise, nFacture);
        }
    }
}
