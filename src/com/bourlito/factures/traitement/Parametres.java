package com.bourlito.factures.traitement;

public class Parametres {

    private static String decompte;
    private static String adresses;
    private static String destination;

    public static String getDecompte() {
        return decompte;
    }

    public static void setDecompte(String decompte) {
        Parametres.decompte = decompte;
    }

    public static String getAdresses() {
        return adresses;
    }

    public static void setAdresses(String adresses) {
        Parametres.adresses = adresses;
    }

    public static String getDestination() {
        return destination;
    }

    public static void setDestination(String destination) {
        Parametres.destination = destination;
    }
}
