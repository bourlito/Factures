package com.bourlito.factures.dto;

import java.util.List;

/**
 * objet entreprise
 */
public class Entreprise {
    private String nomEntreprise;
    private String alias;
    private String adresse;
    private String cp;
    private String ville;
    private List<Double> tarifs;
    private int totalLigne;
    private double totalHT;

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public List<Double> getTarifs() {
        return tarifs;
    }

    public void setTarifs(List<Double> tarifs) {
        this.tarifs = tarifs;
    }

    public int getTotalLigne() {
        return totalLigne;
    }

    public void setTotalLigne(int totalLigne) {
        this.totalLigne = totalLigne;
    }

    public double getTotalHT() {
        return totalHT;
    }

    public void setTotalHT(double totalHT) {
        this.totalHT = totalHT;
    }
}