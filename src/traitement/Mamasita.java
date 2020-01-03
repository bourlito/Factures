package traitement;

import dto.Entreprise;
import dto.Ligne;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Mamasita {
    final List<List<String>> dataLigne = new ArrayList<>();
    final List<List<String>> dataIB = new ArrayList<>();
    final List<List<String>> data471 = new ArrayList<>();
    final List<List<String>> dataLe = new ArrayList<>();
    final List<List<String>> dataSF = new ArrayList<>();
    final List<List<String>> dataAI = new ArrayList<>();
    final List<List<String>> dataDP = new ArrayList<>();
    final List<List<String>> dataTVA = new ArrayList<>();
    final List<List<String>> dataREV = new ArrayList<>();
    final List<List<String>> dataEI = new ArrayList<>();
    final List<List<String>> dataLinkup = new ArrayList<>();
    final List<List<String>> dataND = new ArrayList<>();
    final List<List<String>> dataAF = new ArrayList<>();

    private final List<List<String>> address = new ArrayList<>();

    /**
     * methode de lecture d'un fichier excel
     * @param sheet page correspondant aux données d'une entreprise
     * @return les donnees dans une liste de listes
     */
    List<List<String>> readXls(HSSFSheet sheet) {
        //remise a zero de la liste data
        List<List<String>> data = new ArrayList<>();

        for (Row row : sheet) {
            //saut de la premiere ligne
            if (row.getRowNum() == 0)
                continue;
            List<String> dataLine = new ArrayList<>();
            for (Cell cell : row) {
                try {
                    switch (cell.getColumnIndex()) {
                        //recuperation de la date
                        case 0:
                            dataLine.add(new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue()));
                            break;
                        //recuperation des donnees textes
                        case 1:
                            dataLine.add(cell.getStringCellValue());
                            break;
                        //recuparation des donnees numeriques
                        default:
                            dataLine.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    creerFichierErreur(e.getMessage()+"\n"
                            +sheet.getSheetName() +" "+ getColumnLetter(cell.getColumnIndex()) + (cell.getRowIndex() + 1));
                }
            }
            data.add(dataLine);
        }
        return data;
    }

    /**
     * methode de parsing de la liste data
     * @param data liste de listes des donnees
     */
    void parseXls(List<List<String>> data) {
        for (List<String> aData : data) {
            //initialisation des listes de donnees
            List<String> dataLineLigne = new ArrayList<>();
            List<String> dataLineIB = new ArrayList<>();
            List<String> dataLine471 = new ArrayList<>();
            List<String> dataLineLe = new ArrayList<>();
            List<String> dataLineSF = new ArrayList<>();
            List<String> dataLineAI = new ArrayList<>();
            List<String> dataLineDP = new ArrayList<>();
            List<String> dataLineTVA = new ArrayList<>();
            List<String> dataLineREV = new ArrayList<>();
            List<String> dataLineEI = new ArrayList<>();
            List<String> dataLineLinkup = new ArrayList<>();
            List<String> dataLineND = new ArrayList<>();
            List<String> dataLineAF = new ArrayList<>();

            //tri des donnees existantes dans les differentes listes
            for (int index = 2; index < aData.size(); index++) {
                if (!Objects.equals(aData.get(index), "0.0")) {
                    switch (index) {
                        case MotsCles.NUM_COL_LI:
                            remplirData(aData, dataLineLigne, MotsCles.NUM_COL_LI);
                            break;
                        case MotsCles.NUM_COL_IB:
                            remplirData(aData, dataLineIB, MotsCles.NUM_COL_IB);
                            break;
                        case MotsCles.NUM_COL_471:
                            remplirData(aData, dataLine471, MotsCles.NUM_COL_471);
                            break;
                        case MotsCles.NUM_COL_LE:
                            remplirData(aData, dataLineLe, MotsCles.NUM_COL_LE);
                            break;
                        case MotsCles.NUM_COL_SF:
                            remplirData(aData, dataLineSF, MotsCles.NUM_COL_SF);
                            break;
                        case MotsCles.NUM_COL_AI:
                            remplirData(aData, dataLineAI, MotsCles.NUM_COL_AI);
                            break;
                        case MotsCles.NUM_COL_DP:
                            remplirData(aData, dataLineDP, MotsCles.NUM_COL_DP);
                            break;
                        case MotsCles.NUM_COL_TVA:
                            remplirData(aData, dataLineTVA, MotsCles.NUM_COL_TVA);
                            break;
                        case MotsCles.NUM_COL_REV:
                            remplirData(aData, dataLineREV, MotsCles.NUM_COL_REV);
                            break;
                        case MotsCles.NUM_COL_EI:
                            remplirData(aData, dataLineEI, MotsCles.NUM_COL_EI);
                            break;
                        case MotsCles.NUM_COL_LK:
                            remplirData(aData, dataLineLinkup, MotsCles.NUM_COL_LK);
                            break;
                        case MotsCles.NUM_COL_ND:
                            remplirData(aData, dataLineND, MotsCles.NUM_COL_ND);
                            break;
                        case MotsCles.NUM_COL_AF:
                            remplirData(aData, dataLineAF, MotsCles.NUM_COL_AF);
                            break;
                    }
                }
            }

            //ajout des donnees a la liste correspondante
            dataLigne.add(dataLineLigne);
            dataIB.add(dataLineIB);
            data471.add(dataLine471);
            dataLe.add(dataLineLe);
            dataSF.add(dataLineSF);
            dataAI.add(dataLineAI);
            dataDP.add(dataLineDP);
            dataTVA.add(dataLineTVA);
            dataREV.add(dataLineREV);
            dataEI.add(dataLineEI);
            dataLinkup.add(dataLineLinkup);
            dataND.add(dataLineND);
            dataAF.add(dataLineAF);
        }
    }

    /**
     * methode de remplissage des listes de donnees
     * @param aData liste initiale
     * @param data liste triee
     * @param colonne numero de colonne de la liste triee
     */
    private void remplirData(List<String> aData, List<String> data, int colonne) {
        data.add(aData.get(0));
        data.add(aData.get(1));
        data.add(aData.get(colonne));
    }

    /**
     * methode de creation des lignes
     * @param data liste de listes d'entree
     * @param tarif correspondant a la donnee
     * @param entreprise traitee
     * @return une liste de ligne
     */
    List<Ligne> createLigne(List<List<String>> data, Integer tarif, Entreprise entreprise) {
        Ligne ligne;
        List<Ligne> lignes = new ArrayList<>();

        //cas particulier des tarifs speciaux
        if (tarif != MotsCles.TARIF_AF) {
            for (List<String> list : data) {
                if (!list.isEmpty()) {
                    ligne = new Ligne();
                    ligne.setDate(list.get(0));
                    ligne.setEntreprise(list.get(1));
                    ligne.setNbLigne((int) Double.parseDouble(list.get(2)));
                    ligne.setTarif(entreprise.getTarifs().get(tarif));
                    ligne.setTotal(ligne.getNbLigne() * ligne.getTarif());
                    lignes.add(ligne);
                }
            }
        //cas classiques
        } else {
            for (List<String> list : data) {
                if (!list.isEmpty()) {
                    ligne = new Ligne();
                    ligne.setDate(list.get(0));
                    ligne.setEntreprise(list.get(1));
                    ligne.setNbLigne(1);
                    ligne.setTarif(Double.parseDouble(list.get(2)));
                    ligne.setTotal(ligne.getNbLigne() * ligne.getTarif());
                    lignes.add(ligne);
                }
            }
        }

        return lignes;
    }

    List<Ligne> parseLignes(Entreprise entreprise) {
        List<Ligne> lignes = createLigne(dataLigne, 0, entreprise);
        int i = 0;
        int lastnbligne1 = 0, lastnbligne2 = 0, lastnbligne3 = 0, lastnbligne4 = 0, lastnbligne5 = 0;
        int lastpos1 = 0, lastpos2 = 0, lastpos3 = 0, lastpos4 = 0, lastpos5 = 0;
        List<Double> tarif = entreprise.getTarifs();
        List<Integer> tranche = new ArrayList<>();

        for (Ligne ligne : lignes) {

            if (0 <= i && i <= 5000) {
                ligne.setTarif(tarif.get(0));
                ligne.setTotal(ligne.getNbLigne() * ligne.getTarif());
                lastpos1 = lignes.indexOf(ligne);
                lastnbligne1 = i;
            } else if (5000 < i && i <= 10000) {
                ligne.setTarif(tarif.get(1));
                ligne.setTotal(ligne.getTarif() * ligne.getNbLigne());
                lastpos2 = lignes.indexOf(ligne);
                lastnbligne2 = i;
            } else if (10000 < i && i <= 15000) {
                ligne.setTarif(tarif.get(2));
                ligne.setTotal(ligne.getTarif() * ligne.getNbLigne());
                lastpos3 = lignes.indexOf(ligne);
                lastnbligne3 = i;
            } else if (15000 < i && i <= 20000) {
                ligne.setTarif(tarif.get(3));
                ligne.setTotal(ligne.getTarif() * ligne.getNbLigne());
                lastpos4 = lignes.indexOf(ligne);
                lastnbligne4 = i;
            } else if (20000 < i && i <= 50000) {
                ligne.setTarif(tarif.get(4));
                ligne.setTotal(ligne.getTarif() * ligne.getNbLigne());
                lastpos5 = lignes.indexOf(ligne);
                lastnbligne5 = i;
            } else if (50000 < i) {
                ligne.setTarif(tarif.get(5));
                ligne.setTotal(ligne.getTarif() * ligne.getNbLigne());
            }
            i += ligne.getNbLigne();
        }

        tranche.add(5000);
        tranche.add(10000);
        tranche.add(15000);
        tranche.add(20000);
        tranche.add(50000);

        if (lastnbligne1 != 0 && lastpos1 != 0 && i > 5000 && !tarif.get(0).equals(tarif.get(1))) {
            Ligne test = new Ligne();
            test.setDate(lignes.get(lastpos1).getDate());
            test.setEntreprise(lignes.get(lastpos1).getEntreprise());
            test.setNbLigne(tranche.get(0) - lastnbligne1);
            test.setTarif(tarif.get(0));
            test.setTotal(test.getNbLigne() * test.getTarif());
            lignes.get(lastpos1).setNbLigne(lignes.get(lastpos1).getNbLigne() - test.getNbLigne());
            lignes.get(lastpos1).setTarif(tarif.get(1));
            lignes.get(lastpos1).setTotal(lignes.get(lastpos1).getNbLigne() * lignes.get(lastpos1).getTarif());
            lignes.add(lastpos1, test);
        }

        if (lastnbligne2 != 0 && lastpos2 != 0 && i > 10000 && !tarif.get(1).equals(tarif.get(2))) {
            Ligne test = new Ligne();
            test.setDate(lignes.get(lastpos2 + 1).getDate());
            test.setEntreprise(lignes.get(lastpos2 + 1).getEntreprise());
            test.setNbLigne(tranche.get(1) - lastnbligne2);
            test.setTarif(tarif.get(1));
            test.setTotal(test.getNbLigne() * test.getTarif());
            lignes.get(lastpos2 + 1).setNbLigne(lignes.get(lastpos2 + 1).getNbLigne() - test.getNbLigne());
            lignes.get(lastpos2 + 1).setTarif(tarif.get(2));
            lignes.get(lastpos2 + 1).setTotal(lignes.get(lastpos2 + 1).getNbLigne() * lignes.get(lastpos2 + 1).getTarif());
            lignes.add(lastpos2 + 1, test);
        }

        if (lastnbligne3 != 0 && lastpos3 != 0 && i > 15000 && !tarif.get(2).equals(tarif.get(3))) {
            Ligne test = new Ligne();
            test.setDate(lignes.get(lastpos3 + 2).getDate());
            test.setEntreprise(lignes.get(lastpos3 + 2).getEntreprise());
            test.setNbLigne(tranche.get(2) - lastnbligne3);
            test.setTarif(tarif.get(2));
            test.setTotal(test.getNbLigne() * test.getTarif());
            lignes.get(lastpos3 + 2).setNbLigne(lignes.get(lastpos3 + 2).getNbLigne() - test.getNbLigne());
            lignes.get(lastpos3 + 2).setTarif(tarif.get(3));
            lignes.get(lastpos3 + 2).setTotal(lignes.get(lastpos3 + 2).getNbLigne() * lignes.get(lastpos3 + 2).getTarif());
            lignes.add(lastpos3 + 3, test);
        }

        if (lastnbligne4 != 0 && lastpos4 != 0 && i > 20000 && !tarif.get(3).equals(tarif.get(4))) {
            Ligne test = new Ligne();
            test.setDate(lignes.get(lastpos4 + 3).getDate());
            test.setEntreprise(lignes.get(lastpos4 + 3).getEntreprise());
            test.setNbLigne(tranche.get(3) - lastnbligne4);
            test.setTarif(tarif.get(3));
            test.setTotal(test.getNbLigne() * test.getTarif());
            lignes.get(lastpos4 + 3).setNbLigne(lignes.get(lastpos4 + 3).getNbLigne() - test.getNbLigne());
            lignes.get(lastpos4 + 3).setTarif(tarif.get(4));
            lignes.get(lastpos4 + 3).setTotal(lignes.get(lastpos4 + 3).getNbLigne() * lignes.get(lastpos4 + 3).getTarif());
            lignes.add(lastpos4 + 4, test);
        }

        if (lastnbligne5 != 0 && lastpos5 != 0 && i > 50000 && !tarif.get(4).equals(tarif.get(5))) {
            Ligne test = new Ligne();
            test.setDate(lignes.get(lastpos5 + 4).getDate());
            test.setEntreprise(lignes.get(lastpos5 + 4).getEntreprise());
            test.setNbLigne(tranche.get(4) - lastnbligne5);
            test.setTarif(tarif.get(4));
            test.setTotal(test.getNbLigne() * test.getTarif());
            lignes.get(lastpos5 + 4).setNbLigne(lignes.get(lastpos5 + 4).getNbLigne() - test.getNbLigne());
            lignes.get(lastpos5 + 4).setTarif(tarif.get(5));
            lignes.get(lastpos5 + 4).setTotal(lignes.get(lastpos5 + 4).getNbLigne() * lignes.get(lastpos5 + 4).getTarif());
            lignes.add(lastpos5 + 5, test);
        }
        return lignes;
    }

    Boolean isNotEmpty(List<List<String>> data) {
        for (List<String> aData : data) {
            for (String field : aData) {
                if (field != null) {
                    return true;
                }
            }
        }
        return false;
    }

    NumberFormat formatDouble() {
        NumberFormat formatDouble = NumberFormat.getInstance(Locale.FRANCE);
        formatDouble.setMaximumFractionDigits(2);
        formatDouble.setMinimumFractionDigits(2);
        formatDouble.setRoundingMode(RoundingMode.HALF_UP);

        return formatDouble;
    }

    NumberFormat formatTriple() {
        NumberFormat formatTriple = NumberFormat.getInstance(Locale.FRANCE);
        formatTriple.setMinimumFractionDigits(2);

        return formatTriple;
    }

    NumberFormat formatEntier() {
        NumberFormat formatEntier = NumberFormat.getInstance(Locale.FRANCE);
        formatEntier.setMaximumFractionDigits(0);

        return formatEntier;
    }

    public List<Entreprise> getAdresseTarif(String entadd) {
        List<Entreprise> entreprises;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(entadd));
        } catch (FileNotFoundException e) {
            creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
        HSSFWorkbook wb = null;
        try {
            assert fis != null;
            wb = new HSSFWorkbook(fis);
        } catch (IOException e) {
            creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
        assert wb != null;
        HSSFSheet sheet = wb.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0)
                continue;
            List<String> dataset = new ArrayList<>();
            for (Cell cell : row) {
                switch (cell.getCellTypeEnum()) {
                    case NUMERIC:
                        dataset.add(String.valueOf(cell.getNumericCellValue()));
                        break;
                    case STRING:
                        dataset.add(String.valueOf(cell.getStringCellValue()));
                        break;
                }
            }
            address.add(dataset);
        }

        entreprises = new ArrayList<>();
        List<Double> tarifs;
        Entreprise entreprise;

        for (List<String> list : address) {
            int i = address.indexOf(list);
            tarifs = new ArrayList<>();
            for (int j = 5; j < address.get(i).size(); j++) {
                tarifs.add(Double.parseDouble(address.get(i).get(j)));
            }
            entreprise = new Entreprise();
            entreprise.setAlias(address.get(i).get(0));
            entreprise.setNomEntreprise(address.get(i).get(1));
            entreprise.setAdresse(address.get(i).get(2));
            entreprise.setCp(address.get(i).get(3));
            entreprise.setVille(address.get(i).get(4));
            entreprise.setTarifs(tarifs);
            entreprises.add(entreprise);
        }

        return entreprises;
    }

    public static void creerFichierErreur(String erreur) {
        try {
            FileWriter writer = new FileWriter(Parametres.getDestination() + "\\erreur" + ".txt");
            writer.write(erreur);
            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String getColumnLetter(int index){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return alphabet.substring(index, index + 1);
    }
}
