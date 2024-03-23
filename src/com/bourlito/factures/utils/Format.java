package com.bourlito.factures.utils;



import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class Format {

    /**
     * @return un numberformat de type x xxx,xx a l'arrondi superieur
     */
    
    public static NumberFormat fDouble() {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        format.setRoundingMode(RoundingMode.HALF_UP);

        return format;
    }

    /**
     * @param locale le pays associe au format
     * @return un numberformat de type x xxx,xx a l'arrondi superieur
     */
    
    public static NumberFormat fDouble(Locale locale) {
        NumberFormat format = NumberFormat.getInstance(locale);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        format.setRoundingMode(RoundingMode.HALF_UP);

        return format;
    }

    /**
     * @return un numberformat de type x,xxx
     */
    
    public static NumberFormat fTriple() {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        format.setMinimumFractionDigits(2);

        return format;
    }

    /**
     * @return un numberformat entier
     */
    
    public static NumberFormat fEntier() {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        format.setMaximumFractionDigits(0);

        return format;
    }

    /**
     * @return un numberformat de type xxx
     */
    
    public static NumberFormat fNbFact() {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        format.setMinimumIntegerDigits(3);

        return format;
    }

    /**
     * methode de verification du totalTTC par rapport au totalHT et totalTVA
     * @param totalHT initial
     * @return le total ttc correspondant au ht + tva
     */
    public static double getTotalTTC(double totalHT){
        double ht = totalHT;
        double tva = totalHT * Constants.TAUX_TVA;
        double ttc = totalHT * Constants.TAUX_TTC;

        String sht = Format.fDouble(Locale.US).format(ht);
        sht = sht.replaceAll(",","");
        ht = Double.parseDouble(sht);

        String stva = Format.fDouble(Locale.US).format(tva);
        stva = stva.replaceAll(",","");
        tva = Double.parseDouble(stva);

        String sttc = Format.fDouble(Locale.US).format(ttc);
        sttc = sttc.replaceAll(",","");
        ttc = Double.parseDouble(sttc);

        if (ttc != ht + tva)
            ttc = ht + tva;

        return ttc;
    }
}
