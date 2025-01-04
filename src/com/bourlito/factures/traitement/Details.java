package com.bourlito.factures.traitement;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import com.bourlito.factures.dto.Client;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class Details {

    private HSSFSheet sheet;
    private File outputFolder;
    private final Client client;

    public Details(HSSFSheet sheet, File outputFolder, Client client) {
        this.sheet = sheet;
        this.outputFolder = outputFolder;
        this.client = client;
    }

    /**
     * methode de creation d'un wb excel a partir d'une feuille
     * 
     * @param sheet        la feuille a traiter
     * @param outputFolder le dossier ou enregistrer
     */
    public void createDetails() {
        // Create a new Excel workbook for each sheet
        HSSFWorkbook newWorkbook = new HSSFWorkbook();
        newWorkbook.createSheet(client.getAlias());

        // Copy the sheet to the new workbook
        HSSFSheet newSheet = newWorkbook.getSheet(client.getAlias());
        for (int j = 0; j <= sheet.getLastRowNum(); j++) {
            HSSFRow sourceRow = sheet.getRow(j);
            HSSFRow newRow = newSheet.createRow(j);
            if (sourceRow != null) {
                int l = 0;
                double total = 0;

                for (int k = 0; k < sourceRow.getLastCellNum(); k++) {
                    HSSFCell sourceCell = sourceRow.getCell(k);
                    if (sourceCell != null) {
                        if (k > 1) l = 2 + 3*(k-2);
                        else l = k;
                        HSSFCell newCell = newRow.createCell(l);
                        // date
                        if (j != 0 && k == 0) {
                            newCell.setCellValue(
                                    new SimpleDateFormat("dd/MM/yyyy").format(sourceCell.getDateCellValue()));
                        } else {
                            switch (sourceCell.getCellType()) {
                                default:
                                    break;
                                case STRING:
                                    newCell.setCellValue(sourceCell.getStringCellValue());
                                    break;
                                case NUMERIC:
                                    newCell.setCellValue(sourceCell.getNumericCellValue());
                                    break;
                                case BOOLEAN:
                                    newCell.setCellValue(sourceCell.getBooleanCellValue());
                                    break;
                                case FORMULA:
                                    newCell.setCellFormula(sourceCell.getCellFormula());
                                    break;
                            }

                            if (j==0 && k>2) {
                                try {
                                    newCell.setCellValue(client.getTarifs().get(k -3).getNom());
                                } catch (Exception e) {}
                            }

                            if (j!=0 && k>1 && sourceCell.getCellType() == CellType.NUMERIC) {
                                try {
                                    // tarif
                                    HSSFCell newCellT = newRow.createCell(l +1);
                                    if (k==2) {
                                        newCellT.setCellValue(client.getTranches().get(0).getPrix());
                                    }
                                    else {
                                        newCellT.setCellValue(client.getTarifs().get(k -3).getPrix());
                                    }

                                    // total
                                    double tot = newCell.getNumericCellValue() * newCellT.getNumericCellValue();
                                    total += tot;
                                    HSSFCell newCellTo = newRow.createCell(l +2);
                                    newCellTo.setCellValue(tot);

                                } 
                                catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                    }
                }
                
                if (j != 0) {
                    HSSFCell sourceCell = newRow.createCell(l+1);
                    sourceCell.setCellValue(total);
                }
            }
        }

        // Save the new workbook as a separate Excel file
        String outputFile = Paths.get(outputFolder.getPath(), client.getNom() + ".xls").toString();
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(outputFile))) {
            newWorkbook.write(outputStream);
            newWorkbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
