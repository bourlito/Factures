package com.bourlito.factures.dto;

import com.bourlito.factures.utils.Keys;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.List;

public class Client implements Comparable<Client>{
    private String alias, nom, adresse, cp, ville, libelleTranches;
    private List<Tranche> tranches;
    private List<Tarif> tarifs;

    public Client(String alias, String nom, String adresse, String cp, String ville, String libelleTranches, List<Tranche> tranches, List<Tarif> tarifs) {
        this.alias = alias;
        this.nom = nom;
        this.adresse = adresse;
        this.cp = cp;
        this.ville = ville;
        this.libelleTranches = libelleTranches;
        this.tranches = tranches;
        this.tarifs = tarifs;
    }

    @Override
    public int compareTo(@NotNull Client client){
        return this.getAlias().compareTo(client.getAlias());
    }

    public JSONObject toJsonObject(){
        JSONObject client = new JSONObject();
        client.put(Keys.ALIAS, alias);
        client.put(Keys.NOM, nom);
        client.put(Keys.ADRESSE, adresse);
        client.put(Keys.CP, cp);
        client.put(Keys.VILLE, ville);
        client.put(Keys.LIBELLE_TRANCHE, libelleTranches);

        JSONArray jTranches = new JSONArray();
        for (Tranche entry: tranches){
            JSONObject tranche = new JSONObject();
            tranche.put(Keys.MIN, entry.getMin());
            tranche.put(Keys.PRIX, entry.getPrix());

            jTranches.add(tranche);
        }

        client.put(Keys.TRANCHES, jTranches);

        JSONArray jTarifs = new JSONArray();
        for (Tarif entry: tarifs){
            JSONObject tarif = new JSONObject();
            tarif.put(Keys.COLONNE, entry.getColonne());
            tarif.put(Keys.NOM, entry.getNom());
            tarif.put(Keys.PRIX, entry.getPrix());

            jTarifs.add(tarif);
        }
        client.put(Keys.TARIFS, jTarifs);

        return client;
    }

    public String getAlias() {
        return alias;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getCp() {
        return cp;
    }

    public String getVille() {
        return ville;
    }

    public String getLibelleTranches() {
        return libelleTranches;
    }

    public List<Tranche> getTranches() {
        Collections.sort(tranches);
        return tranches;
    }

    public List<Tarif> getTarifs() {
        Collections.sort(tarifs);
        return tarifs;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setLibelleTranches(String libelleTranches) {
        this.libelleTranches = libelleTranches;
    }

    public void setTranches(List<Tranche> tranches) {
        this.tranches = tranches;
    }

    public void setTarifs(List<Tarif> tarifs) {
        this.tarifs = tarifs;
    }
}
