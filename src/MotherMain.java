import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MotherMain implements MotsCles {
    private static List<List<String>> data = new ArrayList<>();
    List<List<String>> dataLigne = new ArrayList<>();
    List<List<String>> dataIB = new ArrayList<>();
    List<List<String>> data471 = new ArrayList<>();
    List<List<String>> dataLe = new ArrayList<>();
    List<List<String>> dataSF = new ArrayList<>();
    List<List<String>> dataAI = new ArrayList<>();
    List<List<String>> dataDP = new ArrayList<>();
    List<List<String>> dataTVA = new ArrayList<>();
    List<List<String>> dataREV = new ArrayList<>();
    List<List<String>> dataEI = new ArrayList<>();
    List<List<String>> dataLinkup = new ArrayList<>();
    List<List<String>> dataND = new ArrayList<>();
    List<List<String>> dataAF = new ArrayList<>();

    List<List<String>> address = new ArrayList<>();


    void readXls(HSSFSheet sheet) {
        data = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0)
                continue;
            List<String> dataLine = new ArrayList<>();
            for (Cell cell : row) {
                try {
                    switch (cell.getColumnIndex()) {
                        case 0:
                            dataLine.add(new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue()));
                            break;
                        case 1:
                            dataLine.add(cell.getStringCellValue());
                            break;
                        default:
                            dataLine.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    creerFichierErreur(e);
                }
            }
            data.add(dataLine);
        }
    }

    void parseXls() {
        for (List<String> aData : data) {
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
            for (int i = 2; i < aData.size(); i++) {
                if (!Objects.equals(aData.get(i), "0.0")) {
                    switch (i) {
                        case NUM_COL_LI:
                            remplirData(aData, dataLineLigne, NUM_COL_LI);
                            break;
                        case NUM_COL_IB:
                            remplirData(aData, dataLineIB, NUM_COL_IB);
                            break;
                        case NUM_COL_471:
                            remplirData(aData, dataLine471, NUM_COL_471);
                            break;
                        case NUM_COL_LE:
                            remplirData(aData, dataLineLe, NUM_COL_LE);
                            break;
                        case NUM_COL_SF:
                            remplirData(aData, dataLineSF, NUM_COL_SF);
                            break;
                        case NUM_COL_AI:
                            remplirData(aData, dataLineAI, NUM_COL_AI);
                            break;
                        case NUM_COL_DP:
                            remplirData(aData, dataLineDP, NUM_COL_DP);
                            break;
                        case NUM_COL_TVA:
                            remplirData(aData, dataLineTVA, NUM_COL_TVA);
                            break;
                        case NUM_COL_REV:
                            remplirData(aData, dataLineREV, NUM_COL_REV);
                            break;
                        case NUM_COL_EI:
                            remplirData(aData, dataLineEI, NUM_COL_EI);
                            break;
                        case NUM_COL_LK:
                            remplirData(aData, dataLineLinkup, NUM_COL_LK);
                            break;
                        case NUM_COL_ND:
                            remplirData(aData, dataLineND, NUM_COL_ND);
                            break;
                        case NUM_COL_AF:
                            remplirData(aData, dataLineAF, NUM_COL_AF);
                            break;
                    }
                }
            }
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

    private void remplirData(List<String> aData, List<String> data, int colonne) {
        data.add(aData.get(0));
        data.add(aData.get(1));
        data.add(aData.get(colonne));
    }

    List<Ligne> createLigne(List<List<String>> data, Integer tarif, Entreprise entreprise) {
        Ligne ligne;
        List<Ligne> lignes = new ArrayList<>();

        if (tarif != TARIF_AF) {
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
        Boolean isNotEmpty = false;
        for (List<String> aData : data) {
            for (String field : aData) {
                if (field != null)
                    isNotEmpty = true;
            }
        }
        return isNotEmpty;
    }

    void creerFichierErreur(Exception e) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(DOSSIER + "\\erreur.txt");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            writer.write(e.getMessage());
            if (e.getCause() != null)
                writer.write(e.getCause().toString());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(writer);
        try {
            bw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    void creerFichierErreur() {
        FileWriter writer = null;
        try {
            writer = new FileWriter(DOSSIER + "\\erreur.txt");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            writer.write("verifier le nom de la feuille");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(writer);
        try {
            bw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
