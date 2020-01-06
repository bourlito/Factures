package com.bourlito.factures.traitement_v2;

public interface Output {

    void create();
    void creerEntete();
    void creerCGU();
    void creerDetails();
    void creerRecap();
}
