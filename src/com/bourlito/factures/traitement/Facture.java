package com.bourlito.factures.traitement;

import com.bourlito.factures.dto.Client;
import com.itextpdf.text.DocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.io.IOException;

public class Facture {

    private final String name;
    private final int numero;
    private final Client client;
    private final Traitement traitement;

    /**
     * constructeur
     *
     * @param name   le nom de la facture
     * @param numero le numero de facture
     * @param client le client associe
     * @param sheet  la page du decompte
     */
    public Facture(String name, int numero, Client client, HSSFSheet sheet) {
        this.name = name;
        this.numero = numero;
        this.client = client;
        this.traitement = new Traitement(client, sheet);
    }

    /**
     * methode de creation des factures pdf et xls
     */
    public void create() {
        PDF pdf = new PDF(name + ".pdf", numero, client, traitement);
        XLS xls = new XLS(name + ".xls", numero, client, traitement);

        try {
            pdf.createPdf();
            xls.createXls();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return le total hors taxe de la facture
     */
    public double getTotalHT() {
        return traitement.getTotalHT();
    }
}
