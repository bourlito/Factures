package com.bourlito.factures.traitement;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.utils.Date;
import com.bourlito.factures.utils.Erreur;
import com.bourlito.factures.utils.Format;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class Recap {

    private final HSSFWorkbook wb;
    private final String filename;
    private HSSFSheet sheet;

    /**
     * constructeur du recapitulatif
     * @param filename le nom du fichier de recap
     */
    public Recap(String filename){
        this.wb = new HSSFWorkbook();
        this.filename = filename;

        this.createSheet("Recap");
    }


    /**
     * methode d'ecriture dans un workbook
     * le cree s'il n'existe pas
     */
    private void write(){
        try {
            FileOutputStream out = new FileOutputStream(filename);
            wb.write(out);
            out.close();
            wb.close();
        } catch (IOException e){
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * methode de creation d'une feuille dans le workbook
     * @param name le nom de la feuille
     */
    private void createSheet(String name){
        sheet = wb.createSheet(name);
        sheet.setDefaultRowHeightInPoints(15f);
        sheet.setDefaultColumnWidth(10);
        sheet.setColumnWidth(3, 256*61);

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("DATE");
        row.createCell(1).setCellValue("PIECE");
        row.createCell(2).setCellValue("COMPTE");
        row.createCell(3).setCellValue("LIBELLE");
        row.createCell(4).setCellValue("DEBIT");
        row.createCell(5).setCellValue("CREDIT");

        DataFormat df = wb.createDataFormat();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(df.getFormat("0.00"));

        sheet.setDefaultColumnStyle(4, cellStyle);
        sheet.setDefaultColumnStyle(5, cellStyle);

        this.write();
    }

    /**
     * methode d'insertion d'une ligne dans le fichier recap
     * @param client le client concerné
     * @param nFact le numero de la facture
     * @param totalHt le total hors taxe
     */
    public void insert(Client client, int nFact, double totalHt){
        DataFormat df = wb.createDataFormat();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(df.getFormat("0.00 €"));

        this.createRow(nFact, client, "C01" + client.getAlias(), Format.getTotalTTC(totalHt), 4);
        this.createRow(nFact, client, "706000", totalHt, 5);
        this.createRow(nFact, client, "445710", totalHt * 0.2, 5);

        this.write();
    }

    /**
     * methode de creation d'une ligne
     * @param nFact le numero de facture
     * @param client le client associe
     * @param compte le numero de compte
     * @param value la valeur du debit ou du credit
     * @param col la colonne de debit ou de credit
     */
    private void createRow(int nFact, Client client, String compte, double value, int col){
        Row row = sheet.createRow(sheet.getLastRowNum() +1);
        row.createCell(0).setCellValue(Date.getDate());
        row.createCell(1).setCellValue(Date.getYear() + "-" + Format.fNbFact().format(nFact));
        row.createCell(2).setCellValue(compte);
        row.createCell(3).setCellValue(client.getNom() + " " + Date.getLibelle());
        row.createCell(col).setCellValue(value);
    }
}
