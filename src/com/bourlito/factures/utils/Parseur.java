package com.bourlito.factures.utils;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Parseur {

    /**
     * methode de lecture d'un fichier excel :
     * renvoie chaque ligne sous forme d'une liste de string
     * @param decompteSheet la feuille du decompte
     * @return une liste de listes de strings
     */
    public static List<List<String>> readXls(HSSFSheet decompteSheet) {
        //remise a zero de la liste data
        List<List<String>> data = new ArrayList<>();

        for (Row row : decompteSheet) {
            //saut de la premiere ligne
            if (row.getRowNum() == 0)
                continue;

            List<String> dataLine = new ArrayList<>();
            for (Cell cell : row) {
                try {
                    switch (cell.getColumnIndex()) {
                        //recuperation de la date
                        case 0:
                            dataLine.add(new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue()));
                            break;
                        //recuperation des donnees textes
                        case 1:
                            dataLine.add(cell.getStringCellValue());
                            break;
                        //recuparation des donnees numeriques
                        default:
                            dataLine.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Erreur.creerFichierErreur(e.getMessage()+"\n"
                            + decompteSheet.getSheetName() +" "+ Column.getLetterFromInt(cell.getColumnIndex()) + (cell.getRowIndex() + 1));
                }
            }
            data.add(dataLine);
        }

        return data;
    }

    /**
     * methode de remplissage des listes de donnees :
     * transforme une liste complete en liste specifique
     * @param aData liste de toutes les cellules d'une ligne
     * @param colonne numero de colonne
     * @return liste string specifique a la colonne
     */
    public static List<String> remplirData( List<String> aData, int colonne) {
        List<String> data = new ArrayList<>();

        data.add(aData.get(0));
        data.add(aData.get(1));
        data.add(aData.get(colonne));

        return data;
    }
}
