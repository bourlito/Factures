package com.bourlito.factures.traitement_v2;

import com.bourlito.factures.dto.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.util.List;

public class Traitement {

    private final HSSFSheet decompteSheet;
    private final String filename;
    private final int nFacture;
    private final Client client;

    private int totalLigne = 0;
    private double totalHT = 0;
    private int nvDos = 0;


    private List<List<String>> dataLigne;


    /**
     * constructeur
     * @param decompteSheet la feuille de decompte
     * @param filename le nom du fichier a creer
     * @param nFacture le numero de facture
     * @param client le client associe
     */
    public Traitement(HSSFSheet decompteSheet, String filename, int nFacture, Client client) {
        this.decompteSheet = decompteSheet;
        this.filename = filename;
        this.nFacture = nFacture;
        this.client = client;
    }
}
