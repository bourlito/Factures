package com.bourlito.factures.dto;

public class Tarif {

    private String colonne;
    private String nom;
    private double prix;

    public Tarif() {
    }

    public Tarif(String colonne, String nom, Double prix) {
        this.colonne = colonne;
        this.nom = nom;
        this.prix = prix;
    }

    public String getColonne() {
        return colonne;
    }

    public void setColonne(String colonne) {
        this.colonne = colonne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }
}
