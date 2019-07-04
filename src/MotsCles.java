public interface MotsCles {
    String IMG = "D:\\projetFrancois\\logo-cpe-solutions.PNG";
    String IMGREG = "D:\\projetFrancois\\cdnReg.PNG";
    String DOSSIER = "D:\\projetFrancois";
    //String IMG = "C:\\Users\\franc\\Desktop\\CPE SOLUTIONS\\FACTURATION\\logo-cpe-solutions.PNG";
    //String DOSSIER = "C:\\Users\\franc\\Desktop\\CPE SOLUTIONS\\FACTURATION";

    String adresseCPE = "SARL CPE SOLUTIONS\n35-37 Rue de Rome, 75008 PARIS\nTél. 02 30 91 97 42\nSARL au Capital de 75 000 € - RCS de Paris 450 692 447\nTva intracommunautaire : FR76450692447";

    //correspond à la colonne dans le xcl_adresse pour les tarifs
    int TARIF_IB = 6;
    int TARIF_471 = 7;
    int TARIF_LE = 8;
    int TARIF_SF = 9;
    int TARIF_AI = 10;
    int TARIF_DP = 11;
    int TARIF_TVA = 12;
    int TARIF_REV = 13;
    int TARIF_EI = 14;
    int TARIF_LK = 15;
    int TARIF_ND = 16;
    int TARIF_AF = 17;

    //correspond à la colonne dans le xcl_décompte pour le type de ligne
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

    //correspond à la colonne de la facture xcl
    int NUM_COL_DAT_SAI = 0;
    int NUM_COL_NOM_DOS_0 = 1;
    int NUM_COL_NOM_DOS_1 = 2;
    int NUM_COL_NOM_DOS_2 = 3;
    int NUM_COL_NB_LI_0 = 4;
    int NUM_COL_NB_LI_1 = 5;
    int NUM_COL_TRF_LI = 6;
    int NUM_COL_THT = 7;
}