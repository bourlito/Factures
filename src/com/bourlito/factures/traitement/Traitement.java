package com.bourlito.factures.traitement;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Ligne;
import com.bourlito.factures.dto.Tranche;
import com.bourlito.factures.utils.Constants;
import com.bourlito.factures.utils.Format;
import com.bourlito.factures.utils.Parseur;
import javafx.util.Pair;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.util.*;

public class Traitement {

    private final Map<Integer, List<Ligne>> map = new HashMap<>();

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

        // on ajoute la liste des lignes de categorie ligne une fois complete
        if (!dataLigne.isEmpty())
            map.putIfAbsent(Constants.NUM_COL_LI, parseLignes(dataLigne));
    }

    /**
     * @param dataLigne la liste de la categorie ligne sous forme de liste de string
     * @return la liste de ligne de la categorie ligne
     */
    public List<Ligne> parseLignes(List<List<String>> dataLigne) {
        // init params
        List<Ligne> lignes = createListeLigne(dataLigne);
        List<Tranche> tranches = client.getTranches();
        Map<Integer, Pair<Integer, Ligne>> bascules = new HashMap<Integer, Pair<Integer, Ligne>>();
        int nbLigne = 0;

        // recuperation de la tranche
        Tranche tranche = tranches.get(0);

        // pour chaque ligne
        for (Ligne ligne : lignes) {
            // maj  du prix
            ligne.setTarif(tranche.getPrix());

            // on augmente le total de ligne
            nbLigne += ligne.getNbLigne();

            // changment de tranche
            while (tranche.hasNext() && tranche.getNext().getMin() < nbLigne) {
                tranche = tranche.getNext();

                // gestion plusieurs tranches sur meme ligne
                int nbLignesSup = nbLigne - tranche.getMin();
                if (tranche.hasNext()) nbLignesSup = Math.min(nbLignesSup, tranche.getNext().getMin() - tranche.getMin());
                // ajout de la bascule
                bascules.put(bascules.size(), new Pair<>(nbLignesSup, ligne));
            }
        }

        // reinit tranche
        tranche = tranches.get(0);
        // pour chaque tranche detectee
        for (Pair<Integer, Ligne> pair : bascules.values()) {
            // reinit tranche
            tranche = tranche.getNext();
            // creation de la nouvelle ligne
            Ligne nouv = new Ligne();
            // recup derniere ligne
            Ligne init = pair.getValue();

            // init nouvelle ligne
            nouv.setDate(init.getDate());
            nouv.setEntreprise(init.getEntreprise());
            nouv.setNbLigne(pair.getKey());
            nouv.setTarif(tranche.getPrix());

            // maj derniere ligne
            init.setNbLigne(init.getNbLigne() - nouv.getNbLigne());

            // recuperation du bon index (plusieurs tranches sur une ligne)
            int idx = lignes.indexOf(init);
            while (idx + 1 < lignes.size() && // integrite
                    nouv.getTarif() < lignes.get(idx + 1).getTarif()) { // meme tarif
                idx++;
            }
            // ajout de la nouvelle ligne a la liste des lignes
            lignes.add(idx +1, nouv);
        }

        // on retourne les lignes
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

//            ligne.setTotal(ligne.getNbLigne() * ligne.getTarif());
            lignes.add(ligne);
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
            ligne.setNbLigne(Double.parseDouble(data.get(2)));
            ligne.setTarif(client.getTarifs().get(numCol - 3).getPrix());
        }

//        ligne.setTotal(ligne.getNbLigne() * ligne.getTarif());

        return ligne;
    }

    /**
     * @return le nombre total de lignes
     */
    public int getTotalLigne() {
        int totalLigne = 0;

        for (List<Ligne> liste : map.values()){
            for (Ligne ligne : liste){
                totalLigne += ligne.getNbLigne();
            }
        }

        return totalLigne;
    }

    /**
     * @return le total hors taxe de la facture
     */
    public double getTotalHT() {
        double totalHT = 0;

        for (List<Ligne> liste : map.values()){
            for (Ligne ligne : liste){
                totalHT += ligne.getTotal();
            }
        }

        return totalHT;
    }

    /**
     * methode de verification du totalTTC par rapport au totalHT et totalTVA
     *
     * @return le total ttc correspondant au ht + tva
     */
    public double getTotalTTC() {
        double ht = this.getTotalHT();
        double tva = ht * Constants.TAUX_TVA;
        double ttc = ht * Constants.TAUX_TTC;

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

        int nvDos = map.get(Constants.NUM_COL_ND) != null ? map.get(Constants.NUM_COL_ND).size() : 0;

        return nvDos <= 1 ? nvDos + " Nouveau Dossier" : nvDos + " Nouveaux Dossiers";
    }

    /**
     * @return le total de ligne au bon format
     */
    public String getTotalLigneAsString() {
        return Format.fEntier().format(this.getTotalLigne());
    }

    /**
     * @return le total hors taxe au bon format
     */
    public String getTotalHtAsString() {
        return Format.fDouble().format(this.getTotalHT());
    }

    /**
     * @return le total tva au bon format
     */
    public String getTotalTvaAsString() {
        return Format.fDouble().format(this.getTotalHT() * Constants.TAUX_TVA);
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
