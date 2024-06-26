package com.bourlito.factures.dto;

/**
 * objet ligne
 */
public class Ligne {
    private String date;
    private String entreprise;
    private double nbLigne;
    private double tarif;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    public double getNbLigne() {
        return nbLigne;
    }

    public void setNbLigne(double nbLigne) {
        this.nbLigne = nbLigne;
    }

    public double getTarif() {
        return tarif;
    }

    public void setTarif(double tarif) {
        this.tarif = tarif;
    }

    public double getTotal() {
        return nbLigne * tarif;
    }
}