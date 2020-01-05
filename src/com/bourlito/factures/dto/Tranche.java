package com.bourlito.factures.dto;

public class Tranche {

    private int min;
    private double prix;

    public Tranche() {
    }

    public Tranche(int min, double prix) {
        this.min = min;
        this.prix = prix;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
