package com.bourlito.factures.service;

import com.bourlito.factures.dto.Tranche;
import com.bourlito.factures.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class STranche {

    private static STranche INSTANCE = null;

    /**
     * constructeur
     */
    private STranche(){}

    /**
     * cree l'instance si elle n'existe pas
     * @return l'instance de STranche
     */
    public static STranche getInstance(){
        if (INSTANCE == null)
            INSTANCE = new STranche();

        return INSTANCE;
    }

    /**
     * methode de recuperation des tranches par defaut
     * @return la liste des tranches par defaut
     */
    public List<Tranche> getDefaultTranches() {
        List<Tranche> tranches = new ArrayList<>();
        tranches.add(new Tranche(0, Constants.TARIF_DEFAULT));

        return tranches;
    }
}
