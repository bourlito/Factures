package com.bourlito.factures.service;

import com.bourlito.factures.dto.Tarif;
import com.bourlito.factures.utils.Column;
import com.bourlito.factures.utils.Constants;

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

        tarifs.add(new Tarif(Column.getLetterFromInt(Constants.NUM_COL_IB), Constants.LIBELLE_IB, Constants.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(Constants.NUM_COL_471), Constants.LIBELLE_471, Constants.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(Constants.NUM_COL_LE), Constants.LIBELLE_LE, Constants.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(Constants.NUM_COL_SF), Constants.LIBELLE_SF, Constants.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(Constants.NUM_COL_AI), Constants.LIBELLE_AI, Constants.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(Constants.NUM_COL_DP), Constants.LIBELLE_DP, Constants.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(Constants.NUM_COL_TVA), Constants.LIBELLE_TVA, Constants.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(Constants.NUM_COL_REV), Constants.LIBELLE_REV, Constants.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(Constants.NUM_COL_EI), Constants.LIBELLE_EI, Constants.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(Constants.NUM_COL_LK), Constants.LIBELLE_LK, Constants.TARIF_DEFAULT));
        tarifs.add(new Tarif(Column.getLetterFromInt(Constants.NUM_COL_ND), Constants.LIBELLE_ND, Constants.TARIF_DEFAULT));

        return tarifs;
    }
}
