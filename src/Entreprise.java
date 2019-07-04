import java.util.List;

class Entreprise {
    private String nomEntreprise;
    private String alias;
    private String adresse;
    private String cp;
    private String ville;
    private List<Double> tarifs;
    private int totalLigne;
    private double totalHT;

    String getNomEntreprise() {
        return nomEntreprise;
    }

    void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    String getAlias() {
        return alias;
    }

    void setAlias(String alias) {
        this.alias = alias;
    }

    String getAdresse() {
        return adresse;
    }

    void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    String getCp() {
        return cp;
    }

    void setCp(String cp) {
        this.cp = cp;
    }

    String getVille() {
        return ville;
    }

    void setVille(String ville) {
        this.ville = ville;
    }

    List<Double> getTarifs() {
        return tarifs;
    }

    void setTarifs(List<Double> tarifs) {
        this.tarifs = tarifs;
    }

    int getTotalLigne() {
        return totalLigne;
    }

    void setTotalLigne(int totalLigne) {
        this.totalLigne = totalLigne;
    }

    double getTotalHT() {
        return totalHT;
    }

    void setTotalHT(double totalHT) {
        this.totalHT = totalHT;
    }
}
