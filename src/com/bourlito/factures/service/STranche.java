package com.bourlito.factures.service;

import com.bourlito.factures.dto.Tarif;
import com.bourlito.factures.dto.Tranche;
import com.bourlito.factures.utils.Column;
import com.bourlito.factures.utils.MotsCles;

import java.util.ArrayList;
import java.util.List;

public class STranche {

    private static STranche INSTANCE = null;

    private STranche(){}

    public static STranche getInstance(){
        if (INSTANCE == null)
            INSTANCE = new STranche();

        return INSTANCE;
    }

    public List<Tranche> getDefaultTranches() {
        List<Tranche> tranches = new ArrayList<>();
        tranches.add(new Tranche(0, MotsCles.TARIF_DEFAULT));

        return tranches;
    }
}
