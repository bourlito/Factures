package com.bourlito.factures;

import java.io.FileWriter;
import java.io.IOException;

public class Erreur {

    public static void creerFichierErreur(String erreur) {
        try {
            FileWriter writer = new FileWriter(MotsCles.DOSSIER + "erreur.txt");
            writer.write(erreur);
            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
