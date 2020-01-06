package com.bourlito.factures.db;

import com.bourlito.factures.utils.MotsCles;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Database {

    private static final String DB_PATH = "src\\com\\bourlito\\factures\\db\\";
    private static final String DB_NAME = "clients.json";
    private static Database INSTANCE = null;

    private Database(){}

    public static Database getInstance(){
        if (INSTANCE == null)
            INSTANCE = new Database();

        return INSTANCE;
    }

    public void write(@NotNull JSONArray array){
        try {

            File file = new File(DB_PATH + DB_NAME);
            FileWriter fr = new FileWriter(file);
            fr.write(array.toString());
            fr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void export(@NotNull JSONArray array){
        try {

            File file = new File(MotsCles.DOSSIER + DB_NAME);
            FileWriter fr = new FileWriter(file);
            fr.write(array.toString());
            fr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONArray read(){
        JSONParser parser = new JSONParser();

        try {
            return  (JSONArray) parser.parse(new FileReader(DB_PATH + DB_NAME));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
