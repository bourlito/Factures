package com.bourlito.factures.db;

import com.bourlito.factures.utils.Constants;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class Database {

    private static final String DB_NAME = "clients.json";
    private static Database INSTANCE = null;

    /**
     * constructeur
     */
    private Database(){}

    /**
     * cree l'instance si elle n'exite pas
     * @return l'instance de Database
     */
    public static Database getInstance(){
        if (INSTANCE == null)
            INSTANCE = new Database();

        return INSTANCE;
    }

    /**
     * methode d'ecriture d'un json array dans le fichier db
     * @param array json array contenant les infos de la db
     */
    public void write( JSONArray array){
        try {

            File file = new File(Constants.DOSSIER + DB_NAME);
            FileWriter fr = new FileWriter(file);
            fr.write(array.toString());
            fr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * methode de lecture de la db
     * @return un json array contenant les infos de la db
     */
    public JSONArray read(){
        JSONParser parser = new JSONParser();

        try {
            return (JSONArray) parser.parse(new FileReader(Constants.DOSSIER + DB_NAME));

        } catch (FileNotFoundException e){
            this.write(new JSONArray());
            return this.read();

        } catch (ParseException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
