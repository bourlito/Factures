package com.bourlito.factures.db;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Database {

    private static final String SRC_DB = "src\\com\\bourlito\\factures\\db\\clients.json";
    private static Database INSTANCE = null;

    private Database(){}

    public static Database getInstance(){
        if (INSTANCE == null)
            INSTANCE = new Database();

        return INSTANCE;
    }

    public void write(@NotNull JSONArray array){
        try {

            File file = new File(SRC_DB);
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
            return  (JSONArray) parser.parse(new FileReader(SRC_DB));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
