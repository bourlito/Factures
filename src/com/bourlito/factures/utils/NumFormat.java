package com.bourlito.factures.utils;

import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class NumFormat {

    @NotNull
    public static NumberFormat fDouble() {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        format.setRoundingMode(RoundingMode.HALF_UP);

        return format;
    }

    @NotNull
    public static NumberFormat fTriple() {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        format.setMinimumFractionDigits(2);

        return format;
    }

    @NotNull
    public static NumberFormat fEntier() {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        format.setMaximumFractionDigits(0);

        return format;
    }

    @NotNull
    public static NumberFormat fNbFact() {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        format.setMinimumIntegerDigits(3);

        return format;
    }
}
