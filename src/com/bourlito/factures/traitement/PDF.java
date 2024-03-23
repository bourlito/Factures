package com.bourlito.factures.traitement;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Ligne;
import com.bourlito.factures.utils.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;


import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

public class PDF {

    private final String name;
    private final int numero;
    private final Client client;
    private final Traitement traitement;

    private final Font fontHeader = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(46, 110, 175));
    private final BaseColor color = new BaseColor(46, 110, 175);

    /**
     * constructeur
     *
     * @param name       le nom de la facture
     * @param numero     le numero de facture
     * @param client     le client associe
     * @param traitement le traitement effectue sur la page du decompte
     */
    protected PDF(String name, int numero, Client client, Traitement traitement) {
        this.name = name;
        this.numero = numero;
        this.client = client;
        this.traitement = traitement;
    }

    /**
     * methode de creation de la facture pdf
     *
     * @throws DocumentException
     * @throws IOException
     */
    public void createPdf() throws DocumentException, IOException {

        HeaderTable headerTable = new HeaderTable();
        Document document = new Document(PageSize.A4, Constants.MARGIN, Constants.MARGIN, Constants.MARGIN + headerTable.getTableHeight(), Constants.MARGIN);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(name));
        writer.setPageEvent(headerTable);


        document.open();

        document.add(this.getConditionsReglement());
        document.add(new Paragraph("\n\n"));
        document.add(this.getDetails());
        document.add(new Paragraph("\n\n"));
        document.add(this.getRecap());

        document.close();
    }

    /**
     * @return les conditions de reglement
     */
    
    private PdfPTable getConditionsReglement() {
        Font font = new Font(Font.FontFamily.HELVETICA, 8);
        Font fontTitre = new Font(Font.FontFamily.HELVETICA, Constants.FONT_SIZE, Font.BOLD);

        PdfPTable table = new PdfPTable(Constants.TOTAL_COL);
        PdfPCell cell = new PdfPCell();
        int[] headerwidths = {10, 25, 10, 25, 10, 40, 10, 25, 10, 40, 10};
        table.setWidthPercentage(100);
        try {
            table.setWidths(headerwidths);
        } catch (DocumentException e) {
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
        table.setTableEvent(new BorderEvent());

        Paragraph paragraph = new Paragraph("Conditions de règlement : Virement", fontTitre);
        cell.addElement(paragraph);
        cell.setColspan(Constants.TOTAL_COL);
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(Constants.TOTAL_COL);
        cell.setFixedHeight(8);
        cell.setBorder(0);
        table.addCell(cell);

        String[][] listReg = {{"Code Banque", "Code Guichet", "N° du compte", "Clé RIB", "Domiciliation"}, {"16006", "21111", "00023064321", "95", "CREDIT AGRICOLE"}};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <= 10; j++) {
                if ((j % 2) == 0) {
                    cell = new PdfPCell(new Phrase(""));
                    cell.setBorder(0);
                } else {
                    cell = new PdfPCell(new Phrase(listReg[i][j / 2], font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                }

                table.addCell(cell);
            }
            cell = new PdfPCell(new Phrase(""));
            cell.setColspan(Constants.TOTAL_COL);
            cell.setFixedHeight(2);
            cell.setBorder(0);
            table.addCell(cell);
        }

        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(Constants.TOTAL_COL);
        cell.setFixedHeight(2);
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Clause de réserve de propriété : le vendeur conserve la propriété pleine et entière des produits et services vendus jusqu'au paiement complet du prix, en application de la loi du 12/05/1980. Escompte pour paiement anticipé : néant.", font));
        cell.setColspan(Constants.TOTAL_COL);
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(Constants.TOTAL_COL);
        cell.setFixedHeight(2);
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Pénalités de retard : taux légal en vigueur. ", font));
        cell.setColspan(9);
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TVA sur les encaissements", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)));
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Pour tout règlement par chèque, nous vous remercions de nous les transmettre à l'adresse de notre bureau commercial :\nPlace Jean Monnet, bâtiment E, 56270 Ploemeur", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, new BaseColor(46, 110, 175))));
        cell.setColspan(Constants.TOTAL_COL);
        cell.setBorder(0);
        table.addCell(cell);

        return table;
    }

    /**
     * @return les details de la facture
     */
    private PdfPTable getDetails() {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        int[] headerwidths = {10, 30, 12, 10, 10};
        try {
            table.setWidths(headerwidths);
        } catch (DocumentException e) {
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
        PdfPCell cell;
        Paragraph paragraph;

        String[] tableTitleList = {"Date Saisie", "Nom Dossier", "Nombre Lignes", "Tarif Ligne", "Total HT"};
        for (String title : tableTitleList) {
            cell = new PdfPCell();
            paragraph = new Paragraph(title, fontHeader);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(paragraph);
            cell.setPaddingTop(-3);
            cell.setPaddingBottom(4);
            cell.setBorderColor(color);
            cell.setBorderWidth(1.1f);
            table.addCell(cell);
        }

        List<Ligne> liste = traitement.getListeLignes(Constants.NUM_COL_LI);
        if (liste != null && !liste.isEmpty()) {
            cell = new PdfPCell(new Paragraph(client.getLibelleTranches(), fontHeader));
            table.addCell(this.createHeaderLine(cell));
            this.remplirLignes(table, liste);
        }

        // +3 car les tarifs commencent à la colonne D dans le decompte
        for (int i = 0; i < client.getTarifs().size(); i++)
            this.remplirIfNotEmpty(table, client.getTarifs().get(i).getNom(), i+3);

        this.remplirIfNotEmpty(table, Constants.LIBELLE_AF, Constants.NUM_COL_AF);

        return table;
    }

    /**
     * @return le recapitulatif de la facture
     */
    private PdfPTable getRecap() {
        Font font = new Font(Font.FontFamily.HELVETICA, Constants.FONT_SIZE);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setKeepTogether(true);
        int[] headerwidths = {25, 5, 60, 5, 60};
        try {
            table.setWidths(headerwidths);
        } catch (DocumentException e) {
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
        PdfPCell cell = new PdfPCell();
        Paragraph paragraph = new Paragraph("Remarques", font);

        cell.addElement(paragraph);
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("\n"));
        cell.setBorder(0);
        table.addCell(cell);

        paragraph = new Paragraph(traitement.getNvDosAsString(), font);
        cell = new PdfPCell();
        cell.addElement(paragraph);
        cell.setPaddingTop(-3);
        cell.setPaddingBottom(4);
        cell.setBorderColor(color);
        cell.setBorderWidth(1.1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("\n"));
        cell.setBorder(0);
        table.addCell(cell);

        paragraph = new Paragraph("Total nombre de Lignes : " + traitement.getTotalLigneAsString() + "\nTotal € HT : " + traitement.getTotalHtAsString() + " €\nTVA (20%) : " + traitement.getTotalTvaAsString() + " €\nTotal € TTC : " + traitement.getTotalTtcAsString() + " €", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        cell = new PdfPCell();
        cell.addElement(paragraph);
        cell.setPaddingTop(-3);
        cell.setPaddingBottom(4);
        cell.setBorderColor(color);
        cell.setBorderWidth(1.1f);
        table.addCell(cell);

        return table;
    }

    /**
     * methode de creation d'une ligne d'entete (lignes, lettrage, etc...)
     *
     * @param cell la cellule contenant le libelle d'entete
     * @return la cellule au format d'une ligne d'entete
     */
    private PdfPCell createHeaderLine( PdfPCell cell) {
        cell.setColspan(5);
        cell.setPaddingBottom(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(color);

        return cell;
    }

    /**
     * methode de remplissage d'une ligne du pdf
     *
     * @param table que l'on doit remplir
     * @param data  la liste (ligne, lettrage, etc...) contenant les lignes du decompte
     */
    private void remplirLignes(PdfPTable table,  List<Ligne> data) {

        Font font = new Font(Font.FontFamily.HELVETICA, 10);

        for (Ligne ligne : data) {
            table.addCell(creerCell(new Paragraph(ligne.getDate(), font)));
            table.addCell(creerCell(new Paragraph(ligne.getEntreprise(), font)));
            NumberFormat nf = Format.fEntier();
            if (ligne.getNbLigne() % 1 != 0) nf = Format.fTriple(); 
            table.addCell(creerCell(new Paragraph(nf.format(ligne.getNbLigne()), font)));
            table.addCell(creerCell(new Paragraph(Format.fTriple().format(ligne.getTarif()), font)));
            table.addCell(creerCell(new Paragraph(Format.fTriple().format(ligne.getTotal()) + " €", font)));
        }
    }

    /**
     * methode de creation d'une cellule du tableau des details
     *
     * @param paragraph contenant la valeur de la cellule
     * @return la cellule stylisee
     */
    private PdfPCell creerCell( Paragraph paragraph) {
        PdfPCell cell = new PdfPCell();
        paragraph.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(paragraph);
        cell.setPaddingTop(-3);
        cell.setPaddingBottom(4);
        cell.setBorderColor(color);

        return cell;
    }

    /**
     * methode de remplissage d'une categorie si elle n'est pas vide
     *
     * @param table  a remplir
     * @param entete libelle de la categorie
     * @param numCol correspondante a la categorie dans le decompte
     */
    private void remplirIfNotEmpty(PdfPTable table, String entete, int numCol) {
        List<Ligne> liste = traitement.getListeLignes(numCol);
        if (liste != null && !liste.isEmpty()) {
            PdfPCell cell = new PdfPCell(new Paragraph(entete, fontHeader));
            table.addCell(this.createHeaderLine(cell));
            this.remplirLignes(table, liste);
        }

    }

    /**
     * classe du header de la page contenant les infos de la facture
     */
    private class HeaderTable extends PdfPageEventHelper {
        private final PdfPTable table;
        private float tableHeight;

        /**
         * constructeur
         *
         * @throws DocumentException
         * @throws IOException
         */
        private HeaderTable() throws DocumentException, IOException {

            table = new PdfPTable(4);
            table.setTotalWidth(495);
            table.setLockedWidth(true);
            int[] headerwidths = {20, 100, 2, 50};
            table.setWidths(headerwidths);

            PdfPCell cell;
            Paragraph paragraph;

            cell = new PdfPCell();
            Image image = Image.getInstance(Constants.IMG);
            cell.addElement(image);
            cell.setBorder(0);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell();
            paragraph = new Paragraph(Constants.adresseCPE, new Font(Font.FontFamily.HELVETICA, 10));
            cell.addElement(paragraph);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell();
            paragraph = new Paragraph("FACTURE N° " + Date.getYear() + "-" + Format.fNbFact().format(numero), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            cell.addElement(paragraph);
            cell.setBorder(0);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setBackgroundColor(color);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setFixedHeight(10);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell();
            paragraph = new Paragraph(client.getNom() + "\nAdresse : " + client.getAdresse() + "\nCP : " + client.getCp() + "   Ville : " + client.getVille(), new Font(Font.FontFamily.HELVETICA, 10));
            cell.addElement(paragraph);
            cell.setBorderColor(color);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell();
            paragraph = new Paragraph("Date : " + Date.getDate() + "\nA régler avant le : " + Date.getARegler(), new Font(Font.FontFamily.HELVETICA, 10));
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            cell.addElement(paragraph);
            cell.setBorderColor(color);
            table.addCell(cell);
            tableHeight = table.getTotalHeight();
        }

        /**
         * @return tableHeight
         */
        private float getTableHeight() {
            return tableHeight;
        }

        public void onEndPage( PdfWriter writer,  Document document) {
            table.writeSelectedRows(0, -1,
                    document.left(),
                    document.top() + ((document.topMargin() + tableHeight) / 2),
                    writer.getDirectContent());
        }
    }

    /**
     * classe de bordure des conditions de reglement
     */
    private static class BorderEvent implements PdfPTableEvent {
        public void tableLayout(PdfPTable table,  float[][] widths,  float[] heights, int headerRows, int rowStart,  PdfContentByte[] canvases) {
            float[] width = widths[0];
            float x1 = width[0];
            float x2 = width[width.length - 1];
            float y1 = heights[0];
            float y2 = heights[heights.length - 1];
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.rectangle(x1, y1, x2 - x1, y2 - y1);
            cb.setRGBColorStroke(46, 110, 175);
            cb.stroke();
            cb.resetRGBColorStroke();
        }
    }

}
