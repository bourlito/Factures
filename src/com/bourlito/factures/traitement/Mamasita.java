package com.bourlito.factures.traitement;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Ligne;
import com.bourlito.factures.dto.Tranche;
import com.bourlito.factures.utils.Column;
import com.bourlito.factures.utils.Erreur;
import com.bourlito.factures.utils.MotsCles;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class Mamasita {

    // TODO: changer en singleton et faire tout le traitement ici, reserver PDF et XCL pour les visuels

    final List<List<String>> dataLigne = new ArrayList<>();
    private final List<List<String>> dataIB = new ArrayList<>();
    private final List<List<String>> data471 = new ArrayList<>();
    private final List<List<String>> dataLe = new ArrayList<>();
    private final List<List<String>> dataSF = new ArrayList<>();
    private final List<List<String>> dataAI = new ArrayList<>();
    private final List<List<String>> dataDP = new ArrayList<>();
    private final List<List<String>> dataTVA = new ArrayList<>();
    private final List<List<String>> dataREV = new ArrayList<>();
    private final List<List<String>> dataEI = new ArrayList<>();
    private final List<List<String>> dataLinkup = new ArrayList<>();
    private final List<List<String>> dataND = new ArrayList<>();
    private final List<List<String>> dataAF = new ArrayList<>();

    private static final List<List<String>> address = new ArrayList<>();

    private HSSFSheet decompteSheet;
    String filename;
    int nFacture;
    Client client;

    int totalLigne = 0;
    double totalHT = 0;
    int nvDos = 0;

    Mamasita(HSSFSheet sheet, String filename, int nFacture, Client client) {
        this.decompteSheet = sheet;
        this.filename = filename;
        this.nFacture = nFacture;
        this.client = client;

        this.parseXls();
    }

    /**
     * methode de creation des lignes :
     * transforme une liste de string en une ligne
     * @param data liste de listes d'entree
     * @param numCol correspondant a la donnee
     * @return une liste de ligne
     */
    List<Ligne> createLigne(List<List<String>> data, Integer numCol) {
        Ligne ligne;
        List<Ligne> lignes = new ArrayList<>();

        for (List<String> list : data) {
            if (!list.isEmpty()) {
                ligne = new Ligne();
                ligne.setDate(list.get(0));
                ligne.setEntreprise(list.get(1));

                if (numCol == MotsCles.NUM_COL_LI){
                    ligne.setNbLigne((int) Double.parseDouble(list.get(2)));
                    ligne.setTarif(client.getTranches().get(0).getPrix());
                }

                else if (numCol == MotsCles.NUM_COL_AF){
                    ligne.setNbLigne(1);
                    ligne.setTarif(Double.parseDouble(list.get(2)));
                }

                else {
                    ligne.setNbLigne((int) Double.parseDouble(list.get(2)));
                    ligne.setTarif(client.getTarifs().get(numCol -3).getPrix());
                }

                ligne.setTotal(ligne.getNbLigne() * ligne.getTarif());
                lignes.add(ligne);

                totalLigne += ligne.getNbLigne();

                if (numCol != MotsCles.NUM_COL_LI)
                    totalHT += ligne.getTotal();

                if (numCol == MotsCles.NUM_COL_ND)
                    nvDos++;
            }
        }

        return lignes;
    }

    List<Ligne> parseLignes(){
        List<Ligne> lignes = createLigne(dataLigne, MotsCles.NUM_COL_LI);
        List<Tranche> tranches = client.getTranches();
        int nbLigne = 0;
        int[] lastNbLigne = new int[tranches.size() -1];
        int[] lastPos = new int[tranches.size() -1];

        for (Ligne ligne : lignes){

            for (int i = 0; i < tranches.size(); i++){
                Tranche tranche = tranches.get(i);

                if (tranches.size() != i+1 &&
                        (nbLigne == 0 || tranche.getMin() < nbLigne)
                        && nbLigne <= tranches.get(i+1).getMin()) {
                    ligne.setTarif(tranche.getPrix());
                    ligne.setTotal(ligne.getTarif() * ligne.getNbLigne());

                    lastNbLigne[i] = nbLigne;
                    lastPos[i] = lignes.indexOf(ligne);
                    break;
                }

                else if (tranches.size() == i+1 && (nbLigne == 0 || tranche.getMin() < nbLigne)){
                    ligne.setTarif(tranche.getPrix());
                    ligne.setTotal(ligne.getTarif() * ligne.getNbLigne());
                    break;
                }
            }

            nbLigne += ligne.getNbLigne();
            totalHT += ligne.getTotal();
        }

        for (int i = 0; i < lastPos.length; i++){
            if (lastPos[i] + i + 1 == lignes.size()) break;

            Ligne nouv = new Ligne();
            Ligne init = lignes.get(lastPos[i] + i);
            totalHT -= init.getTotal();

            nouv.setDate(init.getDate());
            nouv.setEntreprise(init.getEntreprise());
            nouv.setNbLigne(tranches.get(i + 1).getMin() - lastNbLigne[i]);
            nouv.setTarif(tranches.get(i).getPrix());
            nouv.setTotal(nouv.getNbLigne() * nouv.getTarif());
            totalHT += nouv.getTotal();

            init.setNbLigne(init.getNbLigne() - nouv.getNbLigne());
            init.setTarif(tranches.get(i + 1).getPrix());
            init.setTotal(init.getNbLigne() * init.getTarif());
            totalHT += init.getTotal();

            lignes.add(lastPos[i] + i, nouv);
        }

        return lignes;
    }

    static Boolean isNotEmpty(@NotNull List<List<String>> data) {
        for (List<String> aData : data) {
            for (String field : aData) {
                if (field != null) {
                    return true;
                }
            }
        }
        return false;
    }

    void remplirAllIfNotEmpty(){
        remplirIfNotEmpty(dataIB, client.getTarifs().get(MotsCles.NUM_COL_IB -3).getNom(), MotsCles.NUM_COL_IB);
        remplirIfNotEmpty(data471, client.getTarifs().get(MotsCles.NUM_COL_471 -3).getNom(), MotsCles.NUM_COL_471);
        remplirIfNotEmpty(dataLe, client.getTarifs().get(MotsCles.NUM_COL_LE -3).getNom(), MotsCles.NUM_COL_LE);
        remplirIfNotEmpty(dataSF, client.getTarifs().get(MotsCles.NUM_COL_SF -3).getNom(), MotsCles.NUM_COL_SF);
        remplirIfNotEmpty(dataAI, client.getTarifs().get(MotsCles.NUM_COL_AI -3).getNom(), MotsCles.NUM_COL_AI);
        remplirIfNotEmpty(dataDP, client.getTarifs().get(MotsCles.NUM_COL_DP -3).getNom(), MotsCles.NUM_COL_DP);
        remplirIfNotEmpty(dataTVA, client.getTarifs().get(MotsCles.NUM_COL_TVA -3).getNom(), MotsCles.NUM_COL_TVA);
        remplirIfNotEmpty(dataREV, client.getTarifs().get(MotsCles.NUM_COL_REV -3).getNom(), MotsCles.NUM_COL_REV);
        remplirIfNotEmpty(dataEI, client.getTarifs().get(MotsCles.NUM_COL_EI -3).getNom(), MotsCles.NUM_COL_EI);
        remplirIfNotEmpty(dataLinkup, client.getTarifs().get(MotsCles.NUM_COL_LK -3).getNom(), MotsCles.NUM_COL_LK);
        remplirIfNotEmpty(dataND, client.getTarifs().get(MotsCles.NUM_COL_ND -3).getNom(), MotsCles.NUM_COL_ND);
        remplirIfNotEmpty(dataAF, client.getTarifs().get(MotsCles.NUM_COL_AF -3).getNom(), MotsCles.NUM_COL_AF);
    }

    abstract void remplirIfNotEmpty(List<List<String>> data, String libelle, int numCol);

    /**
     * methode de parsing de la liste data
     */
    private void parseXls() {
        List<List<String>> data = this.readXls(decompteSheet);

        //initialisation des listes de donnees
        List<String> dataLineLigne, dataLineIB, dataLine471, dataLineLe, dataLineSF, dataLineAI,
                dataLineDP, dataLineTVA, dataLineREV, dataLineEI, dataLineLinkup, dataLineND, dataLineAF;

        for (List<String> aData : data) {

            //tri des donnees existantes dans les differentes listes
            for (int index = 2; index < aData.size(); index++) {
                if (!aData.get(index).equals("0.0")) {
                    switch (index) {
                        case MotsCles.NUM_COL_LI:
                            dataLineLigne = remplirData(aData, MotsCles.NUM_COL_LI);
                            dataLigne.add(dataLineLigne);
                            break;
                        case MotsCles.NUM_COL_IB:
                            dataLineIB = remplirData(aData, MotsCles.NUM_COL_IB);
                            dataIB.add(dataLineIB);
                            break;
                        case MotsCles.NUM_COL_471:
                            dataLine471 = remplirData(aData, MotsCles.NUM_COL_471);
                            data471.add(dataLine471);
                            break;
                        case MotsCles.NUM_COL_LE:
                            dataLineLe = remplirData(aData, MotsCles.NUM_COL_LE);
                            dataLe.add(dataLineLe);
                            break;
                        case MotsCles.NUM_COL_SF:
                            dataLineSF = remplirData(aData, MotsCles.NUM_COL_SF);
                            dataSF.add(dataLineSF);
                            break;
                        case MotsCles.NUM_COL_AI:
                            dataLineAI = remplirData(aData, MotsCles.NUM_COL_AI);
                            dataAI.add(dataLineAI);
                            break;
                        case MotsCles.NUM_COL_DP:
                            dataLineDP = remplirData(aData, MotsCles.NUM_COL_DP);
                            dataDP.add(dataLineDP);
                            break;
                        case MotsCles.NUM_COL_TVA:
                            dataLineTVA = remplirData(aData, MotsCles.NUM_COL_TVA);
                            dataTVA.add(dataLineTVA);
                            break;
                        case MotsCles.NUM_COL_REV:
                            dataLineREV = remplirData(aData, MotsCles.NUM_COL_REV);
                            dataREV.add(dataLineREV);
                            break;
                        case MotsCles.NUM_COL_EI:
                            dataLineEI = remplirData(aData, MotsCles.NUM_COL_EI);
                            dataEI.add(dataLineEI);
                            break;
                        case MotsCles.NUM_COL_LK:
                            dataLineLinkup = remplirData(aData, MotsCles.NUM_COL_LK);
                            dataLinkup.add(dataLineLinkup);
                            break;
                        case MotsCles.NUM_COL_ND:
                            dataLineND = remplirData(aData, MotsCles.NUM_COL_ND);
                            dataND.add(dataLineND);
                            break;
                        case MotsCles.NUM_COL_AF:
                            dataLineAF = remplirData(aData, MotsCles.NUM_COL_AF);
                            dataAF.add(dataLineAF);
                            break;
                    }
                }
            }
        }
    }

    /**
     * methode de lecture d'un fichier excel :
     * chaque ligne correspond a une une liste de strings, ou chaque string est ue cellule
     * @return une liste de listes de strings
     * @param decompteSheet la feuille du decompte
     */
    private List<List<String>> readXls(HSSFSheet decompteSheet) {
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
    private List<String> remplirData(@NotNull List<String> aData, int colonne) {
        List<String> data = new ArrayList<>();

        data.add(aData.get(0));
        data.add(aData.get(1));
        data.add(aData.get(colonne));

        return data;
    }
}
