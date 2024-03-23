package com.bourlito.factures.utils;



import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class Date {

    private static final Calendar calendar = Calendar.getInstance();
    public static final int MILL = 2000;

    
    private static String formatMonth(int month){
        NumberFormat nbFactFormat = NumberFormat.getInstance(Locale.FRANCE);
        nbFactFormat.setMinimumIntegerDigits(2);

        return nbFactFormat.format(month);
    }

    
    public static String getLibelle() {

        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR) - MILL;

        if (month == 0){
            month = Calendar.UNDECIMBER;
            year--;
        }

        return formatMonth(month) + year;
    }

    
    public static String getDate() {

        switch (calendar.get(Calendar.MONTH)) {
            default:
                return "31/" + formatMonth(calendar.get(Calendar.MONTH)) + "/" + calendar.get(Calendar.YEAR);

            case Calendar.JANUARY:
                return  "31/12/" + (calendar.get(Calendar.YEAR) - 1);

            case Calendar.MARCH:
                return "28/" + formatMonth(calendar.get(Calendar.MONTH)) + "/" + calendar.get(Calendar.YEAR);

            case Calendar.MAY: case Calendar.JULY: case Calendar.OCTOBER: case Calendar.DECEMBER:
                return  "30/" + formatMonth(calendar.get(Calendar.MONTH)) + "/" + calendar.get(Calendar.YEAR);
        }
    }

    
    public static String getARegler() {
        return "20/" + formatMonth(calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }

    public static int getYear() {
        return calendar.get(Calendar.MONTH) == Calendar.JANUARY ? calendar.get(Calendar.YEAR) - 1 : calendar.get(Calendar.YEAR);
    }
}
