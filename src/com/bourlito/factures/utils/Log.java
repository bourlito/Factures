package com.bourlito.factures.utils;

public class Log {
    /**
     * methode d'affiche d'un message dans la console
     *
     * @param message contenu
     */
    public static void d(Object message) {
        System.out.println(message.toString());
    }

    /**
     * methode d'affiche d'un message dans la console
     *
     * @param libelle du message
     * @param message contenu
     */
    public static void d(String libelle, Object message) {
        System.out.println(libelle + ": " + message.toString());
    }
}
