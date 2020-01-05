package com.bourlito.factures.dto;

import org.jetbrains.annotations.NotNull;

public class Tranche implements Comparable<Tranche>{

    private int min;
    private double prix;

    public Tranche() {
    }

    public Tranche(int min, double prix) {
        this.min = min;
        this.prix = prix;
    }

    @Override
    public int compareTo(@NotNull Tranche tranche) {
        return this.getMin() - tranche.getMin();
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
