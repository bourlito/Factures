package com.bourlito.factures.traitement_v2;

import com.bourlito.factures.dto.*;
import com.bourlito.factures.utils.MotsCles;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.util.ArrayList;
import java.util.List;

public class Traitement {

    private HSSFSheet decompteSheet;
    private String filename;
    private int nFacture;
    private Client client;

    private int totalLigne = 0;
    private double totalHT = 0;
    private int nvDos = 0;


    private List<List<String>> dataLigne;


    public Traitement(HSSFSheet decompteSheet, String filename, int nFacture, Client client) {
        this.decompteSheet = decompteSheet;
        this.filename = filename;
        this.nFacture = nFacture;
        this.client = client;
    }
}
