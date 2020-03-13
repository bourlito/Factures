package com.bourlito.factures.traitement;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Ligne;
import com.bourlito.factures.dto.Tranche;
import com.bourlito.factures.utils.Constants;
import com.bourlito.factures.utils.Format;
import com.bourlito.factures.utils.Parseur;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.util.*;

public class Traitement {


    private final Map<Integer, List<Ligne>> map = new HashMap<>();

    private int totalLigne = 0;
    private double totalHT = 0;
    private int nvDos = 0;

    private final Client client;

    /**
     * constructeur
     *
     * @param client        le client associe a la feuille
     * @param decompteSheet la feuille excel a traiter
     */
    public Traitement(Client client, HSSFSheet decompteSheet) {
        this.client = client;

        this.parseXls(decompteSheet);
    }

    /**
     * methode de parsing de la liste data
     *
     * @param decompteSheet la feuille excel a traiter
     */
    private void parseXls(HSSFSheet decompteSheet) {

        List<List<String>> dataLigne = new ArrayList<>();

        for (List<String> data : Parseur.readXls(decompteSheet)) {
            // tri des donnees existantes dans les differentes listes
            for (int index = 2; index < data.size(); index++) {

                // si la cellule etait vide
                if (data.get(index).equals("0.0"))
                    continue;

                // si c'est une cellule de la categorie ligne
                if (index == Constants.NUM_COL_LI)
                    dataLigne.add(Parseur.remplirData(data, Constants.NUM_COL_LI));

                // sinon on ajoute la ligne a la map correspondante
                else {
                    Ligne ligne = createLigne(Parseur.remplirData(data, index), index);
                    map.putIfAbsent(index, new ArrayList<>());
                    map.get(index).add(ligne);
                }
            }
        }

        // on ajoute la liste complete des lignes de categorie ligne une fois complete
        map.putIfAbsent(Constants.NUM_COL_LI, parseLignes(dataLigne));
    }

    /**
     * @param dataLigne la liste de la categorie ligne sous forme de liste de string
     * @return la liste de ligne de la categorie ligne
     */
    public List<Ligne> parseLignes(List<List<String>> dataLigne) {
        List<Ligne> lignes = createListeLigne(dataLigne);
        List<Tranche> tranches = client.getTranches();

        int nbLigne = 0;
        int[] lastNbLigne = new int[tranches.size() - 1];
        int[] lastPos = new int[tranches.size() - 1];

        for (Ligne ligne : lignes) {

            for (int i = 0; i < tranches.size(); i++) {
                Tranche tranche = tranches.get(i);

                if (tranches.size() != i + 1 &&
                        (nbLigne == 0 || tranche.getMin() < nbLigne)
                        && nbLigne <= tranches.get(i + 1).getMin()) {
                    ligne.setTarif(tranche.getPrix());
                    ligne.setTotal(ligne.getTarif() * ligne.getNbLigne());

                    lastNbLigne[i] = nbLigne;
                    lastPos[i] = lignes.indexOf(ligne);
                    break;
                }

                else if (tranches.size() == i + 1 && (nbLigne == 0 || tranche.getMin() < nbLigne)) {
                    ligne.setTarif(tranche.getPrix());
                    ligne.setTotal(ligne.getTarif() * ligne.getNbLigne());
                    break;
                }
            }

            nbLigne += ligne.getNbLigne();
            totalHT += ligne.getTotal();
        }

        // pour chaque tranche on transforme la ligne de transition en deux lignes
        for (int i = 0; i < lastPos.length; i++) {
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

    /**
     * methode de creation des lignes :
     * transforme une liste de string en une ligne
     *
     * @param data   liste de listes d'entree
     * @return une liste de ligne
     */
    public List<Ligne> createListeLigne(List<List<String>> data) {
        Ligne ligne;
        List<Ligne> lignes = new ArrayList<>();

        for (List<String> list : data) {
            if (list.isEmpty())
                continue;

            ligne = new Ligne();
            ligne.setDate(list.get(0));
            ligne.setEntreprise(list.get(1));

            ligne.setNbLigne((int) Double.parseDouble(list.get(2)));
            ligne.setTarif(client.getTranches().get(0).getPrix());

            ligne.setTotal(ligne.getNbLigne() * ligne.getTarif());
            lignes.add(ligne);

            totalLigne += ligne.getNbLigne();
            totalHT += ligne.getTotal();
        }

        return lignes;
    }

    /**
     * methode de creation d'une ligne a partir d'une liste de string
     *
     * @param data   la liste de string
     * @param numCol le numero de colonne correspondant a la categorie de la ligne
     * @return une ligne
     */
    public Ligne createLigne(List<String> data, Integer numCol) {

        Ligne ligne = new Ligne();
        ligne.setDate(data.get(0));
        ligne.setEntreprise(data.get(1));

        if (numCol == Constants.NUM_COL_AF) {
            ligne.setNbLigne(1);
            ligne.setTarif(Double.parseDouble(data.get(2)));

        } else {
            ligne.setNbLigne((int) Double.parseDouble(data.get(2)));
            ligne.setTarif(client.getTarifs().get(numCol - 3).getPrix());
        }

        ligne.setTotal(ligne.getNbLigne() * ligne.getTarif());

        totalLigne += ligne.getNbLigne();

        if (numCol == Constants.NUM_COL_ND)
            nvDos++;

        return ligne;
    }

    /**
     * @return le total hors taxe de la facture
     */
    public double getTotalHT() {
        return totalHT;
    }

    /**
     * methode de verification du totalTTC par rapport au totalHT et totalTVA
     *
     * @return le total ttc correspondant au ht + tva
     */
    public double getTotalTTC() {
        double ht = totalHT;
        double tva = totalHT * Constants.TAUX_TVA;
        double ttc = totalHT * Constants.TAUX_TTC;

        String sht = Format.fDouble(Locale.US).format(ht);
        sht = sht.replaceAll(",", "");
        ht = Double.parseDouble(sht);

        String stva = Format.fDouble(Locale.US).format(tva);
        stva = stva.replaceAll(",", "");
        tva = Double.parseDouble(stva);

        String sttc = Format.fDouble(Locale.US).format(ttc);
        sttc = sttc.replaceAll(",", "");
        ttc = Double.parseDouble(sttc);

        if (ttc != ht + tva)
            ttc = ht + tva;

        return ttc;
    }

    /**
     * @return la phrase contenant le nombre de nouveaux dossiers
     */
    public String getNvDosAsString() {
        return nvDos <= 1 ? nvDos + " Nouveau Dossier" : nvDos + " Nouveaux Dossiers";
    }

    /**
     * @return le total de ligne au bon format
     */
    public String getTotalLigneAsString() {
        return Format.fEntier().format(totalLigne);
    }

    /**
     * @return le total hors taxe au bon format
     */
    public String getTotalHtAsString() {
        return Format.fDouble().format(totalHT);
    }

    /**
     * @return le total tva au bon format
     */
    public String getTotalTvaAsString() {
        return Format.fDouble().format(totalHT * Constants.TAUX_TVA);
    }

    /**
     * @return le total ttc au bon format
     */
    public String getTotalTtcAsString() {
        return Format.fDouble().format(this.getTotalTTC());
    }

    /**
     * methode de recuperation d'une liste dans la map
     *
     * @param numCol le numero de colonne correspondant à la categorie
     * @return la liste de ligne correspondant a la categorie souhaitee
     */
    public List<Ligne> getListeLignes(int numCol) {
        return map.get(numCol);
    }
}
