package com.bourlito.factures.utils;

public class Parametres {

    private static String adresses;
    private static String destination;

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
