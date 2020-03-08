package com.bourlito.factures.utils;

public class Column {

    /**
     * methode de recuperation d'une lettre de l'alphabet a partir de son index
     * @param index la postion dans l'alphabet
     * @return la lettre associee
     */
    public static String getLetterFromInt(int index){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return alphabet.substring(index, index + 1);
    }
}
