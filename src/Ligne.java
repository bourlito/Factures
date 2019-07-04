class Ligne {
    private String date;
    private String entreprise;
    private double nbLigne;
    private double tarif;
    private double total;

    String getDate() {
        return date;
    }

    void setDate(String date) {
        this.date = date;
    }

    String getEntreprise() {
        return entreprise;
    }

    void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    double getNbLigne() {
        return nbLigne;
    }

    void setNbLigne(double nbLigne) {
        this.nbLigne = nbLigne;
    }

    double getTarif() {
        return tarif;
    }

    void setTarif(double tarif) {
        this.tarif = tarif;
    }

    double getTotal() {
        return total;
    }

    void setTotal(double total) {
        this.total = total;
    }
}
