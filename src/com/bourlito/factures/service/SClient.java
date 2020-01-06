package com.bourlito.factures.service;

import com.bourlito.factures.utils.Keys;
import com.bourlito.factures.db.Database;
import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Tarif;
import com.bourlito.factures.dto.Tranche;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SClient {

    private static SClient INSTANCE = null;
    private List<Client> clients = new ArrayList<>();

    private Database db = Database.getInstance();

    private SClient(){
        this.initClients();
    }

    public static SClient getInstance(){
        if (INSTANCE == null)
            INSTANCE = new SClient();

        return INSTANCE;
    }

    public List<Client> getClients() {
        Collections.sort(clients);
        return clients;
    }

    public void addClient(Client client) {
        clients.add(client);
        this.writeDB();
    }

    public void deleteClient(Client client){
        clients.remove(client);
        this.writeDB();
    }

    public void exportDB(){
        db.export(this.insert());
    }

    private void initClients(){
        JSONArray jClients = db.read();

        for (Object o1: jClients) {
            JSONObject jClient = (JSONObject) o1;

            String alias = (String) jClient.get(Keys.ALIAS);
            String nom = (String) jClient.get(Keys.NOM);
            String adresse = (String) jClient.get(Keys.ADRESSE);
            String cp = (String) jClient.get(Keys.CP);
            String ville = (String) jClient.get(Keys.VILLE);
            String libelleTranches = (String) jClient.get(Keys.LIBELLE_TRANCHE);

            JSONArray jTranches = (JSONArray) jClient.get(Keys.TRANCHES);
            List<Tranche> tranches = new ArrayList<>();

            for (Object o2: jTranches){
                JSONObject jTranche = (JSONObject) o2;

                long tMin = (long) jTranche.get(Keys.MIN);
                double tPrix = (double) jTranche.get(Keys.PRIX);

                Tranche tranche = new Tranche((int) tMin, tPrix);
                tranches.add(tranche);
            }

            JSONArray jTarifs = (JSONArray) jClient.get(Keys.TARIFS);
            List<Tarif> tarifs = new ArrayList<>();

            for (Object o2: jTarifs){
                JSONObject jTranche = (JSONObject) o2;

                String tColonne = (String) jTranche.get(Keys.COLONNE);
                String tNom = (String) jTranche.get(Keys.NOM);
                double tPrix = (double) jTranche.get(Keys.PRIX);

                Tarif tarif = new Tarif(tColonne, tNom, tPrix);
                tarifs.add(tarif);
            }

            Client client = new Client(alias, nom, adresse, cp, ville, libelleTranches, tranches, tarifs);
            clients.add(client);
        }
    }

    private void writeDB(){
        db.write(this.insert());
    }

    private JSONArray insert(){
        JSONArray array = new JSONArray();
        for (Client client: clients)
            array.add(client.toJsonObject());

        return array;
    }
}
