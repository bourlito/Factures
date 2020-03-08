package com.bourlito.factures.dto;

import org.jetbrains.annotations.NotNull;

public class Tarif implements Comparable<Tarif>{

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

    @Override
    public int compareTo(@NotNull Tarif tarif) {
        return this.getColonne().compareTo(tarif.getColonne());
    }

    public String getColonne() {
        return colonne;
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
