package com.bourlito.factures.utils;

public class Column {

    public static String getLetterFromInt(int index){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return alphabet.substring(index, index + 1);
    }
}
