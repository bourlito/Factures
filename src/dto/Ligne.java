package dto;

public class Ligne {
    private String date;
    private String entreprise;
    private double nbLigne;
    private double tarif;
    private double total;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    public double getNbLigne() {
        return nbLigne;
    }

    public void setNbLigne(double nbLigne) {
        this.nbLigne = nbLigne;
    }

    public double getTarif() {
        return tarif;
    }

    public void setTarif(double tarif) {
        this.tarif = tarif;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}