package com.bourlito.factures.utils;

public interface MotsCles {
    //String DOSSIER = "D:\\Perso\\projetFrancois\\";
    String DOSSIER = "C:\\Users\\franc\\Desktop\\CPE SOLUTIONS\\FACTURATION\\";
    String IMG = DOSSIER + "logo-cpe-solutions.PNG";
    String IMGREG = DOSSIER + "cdnReg.PNG";

    String adresseCPE = "SARL CPE SOLUTIONS\n" +
            "35-37 Rue de Rome, 75008 PARIS\n" +
            "Tél. 02 30 91 97 42\n" +
            "SARL au Capital de 75 000 € - RCS de Paris 450 692 447\n" +
            "Tva intracommunautaire : FR76450692447";

    // correspond à la colonne dans le xcl_décompte pour le type de ligne
    int NUM_COL_LI = 2;
    int NUM_COL_IB = 3;
    int NUM_COL_471 = 4;
    int NUM_COL_LE = 5;
    int NUM_COL_SF = 6;
    int NUM_COL_AI = 7;
    int NUM_COL_DP = 8;
    int NUM_COL_TVA = 9;
    int NUM_COL_REV = 10;
    int NUM_COL_EI = 11;
    int NUM_COL_LK = 12;
    int NUM_COL_ND = 13;
    int NUM_COL_AF = 14;

    // correspond à la colonne de la facture xcl
    int NUM_COL_DAT_SAI = 0;
    int NUM_COL_NOM_DOS_0 = 1;
    int NUM_COL_NOM_DOS_1 = 2;
    int NUM_COL_NOM_DOS_2 = 3;
    int NUM_COL_NB_LI_0 = 4;
    int NUM_COL_NB_LI_1 = 5;
    int NUM_COL_TRF_LI = 6;
    int NUM_COL_THT = 7;
    
    // libelle tarifs
    String LIBELLE_LI = "Lignes";
    String LIBELLE_IB = "Import Banque";
    String LIBELLE_471 = "471";
    String LIBELLE_LE = "Lettrage";
    String LIBELLE_SF = "ScanFact";
    String LIBELLE_AI = "Attache Image";
    String LIBELLE_DP = "Découpe PDF";
    String LIBELLE_TVA = "TVA";
    String LIBELLE_REV = "Révision";
    String LIBELLE_EI = "Ecritures Importées";
    String LIBELLE_LK = "Linkup";
    String LIBELLE_ND = "Nouveaux Dossiers";
    String LIBELLE_AF = "Dossiers Spécifiques";

    double TARIF_DEFAULT = 0.0;
}