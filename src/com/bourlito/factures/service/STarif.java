package com.bourlito.factures.service;

import com.bourlito.factures.dto.Tarif;
import com.bourlito.factures.utils.Column;
import com.bourlito.factures.utils.MotsCles;

import java.util.ArrayList;
import java.util.List;

public class STarif {

    private static STarif INSTANCE = null;

    private STarif(){}

    public static STarif getInstance(){
        if (INSTANCE == null)
            INSTANCE = new STarif();

        return INSTANCE;
    }

    public List<Tarif> getDefaultTarifs() {
        List<Tarif> tarifs = new ArrayList<>();

        tarifs.add(new Tarif(Column.getLetterFromInt(MotsCles.NUM_COL_IB), MotsCles.LIBELLE_IB, MotsCles.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(MotsCles.NUM_COL_471), MotsCles.LIBELLE_471, MotsCles.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(MotsCles.NUM_COL_LE), MotsCles.LIBELLE_LE, MotsCles.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(MotsCles.NUM_COL_SF), MotsCles.LIBELLE_SF, MotsCles.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(MotsCles.NUM_COL_AI), MotsCles.LIBELLE_AI, MotsCles.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(MotsCles.NUM_COL_DP), MotsCles.LIBELLE_DP, MotsCles.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(MotsCles.NUM_COL_TVA), MotsCles.LIBELLE_TVA, MotsCles.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(MotsCles.NUM_COL_REV), MotsCles.LIBELLE_REV, MotsCles.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(MotsCles.NUM_COL_EI), MotsCles.LIBELLE_EI, MotsCles.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(MotsCles.NUM_COL_LK), MotsCles.LIBELLE_LK, MotsCles.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(MotsCles.NUM_COL_ND), MotsCles.LIBELLE_ND, MotsCles.TARIF_DEFAULT));

        return tarifs;
    }
}
