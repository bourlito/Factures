package com.bourlito.factures.utils;

public class Column {

    public static String getLetterFromInt(int index){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return alphabet.substring(index, index + 1);
    }

    public static int getIntFromLetter(String letter){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return alphabet.indexOf(letter);
    }
}
