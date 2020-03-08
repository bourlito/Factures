package com.bourlito.factures.traitement;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.utils.Date;
import com.bourlito.factures.utils.Erreur;
import com.bourlito.factures.utils.NumFormat;
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

        this.write();
    }

    /**
     * methode d'insertion d'une ligne dans le fichier recap
     * @param client le client concerné
     * @param nFact le numero de la facture
     * @param totalHt le total hors taxe
     */
    public void insert(Client client, int nFact, double totalHt){
        System.out.println(sheet.getLastRowNum() +1+" "+client.getNom()+" "+nFact+" "+totalHt);

        DataFormat df = wb.createDataFormat();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(df.getFormat("0.00 €"));

        Row row = sheet.createRow(sheet.getLastRowNum() +1);
        row.createCell(0).setCellValue(Date.getDate());
        row.createCell(1).setCellValue(Date.getYear() + "-" + NumFormat.fNbFact().format(nFact));

        Cell cellHt = row.createCell(2);
        cellHt.setCellValue(totalHt * 1.2);
        cellHt.setCellStyle(cellStyle);

        this.write();
    }
}
