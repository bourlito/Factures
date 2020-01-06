package com.bourlito.factures.traitement;

import com.bourlito.factures.dto.Client;
import com.bourlito.factures.dto.Ligne;
import com.bourlito.factures.utils.Date;
import com.bourlito.factures.utils.Erreur;
import com.bourlito.factures.utils.MotsCles;
import com.bourlito.factures.utils.NumFormat;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PDF extends Mamasita {

    private PdfPTable table;
    private Font fontTitre = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(46, 110, 175));

    public PDF(HSSFSheet sheet, String filename, int nFacture, Client client) {
        super(sheet, filename, nFacture, client);
    }

    public void createPdf() {
        HeaderTable event = null;
        try {
            event = new HeaderTable();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        assert event != null;
        Document document = new Document(PageSize.A4, 50, 50, 50 + event.getTableHeight(), 50);
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        assert writer != null;
        writer.setPageEvent(event);
        document.open();

        try {
            document.add(createCDNReg());
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        try {
            document.add(new Paragraph("\n\n"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        try {
            document.add(createTable(filename));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Font font = new Font(Font.FontFamily.HELVETICA, 11);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
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

        String phrase = nvDos <= 1 ? nvDos + " Nouveau Dossier" : nvDos + " Nouveaux Dossiers";

        paragraph = new Paragraph(phrase, font);
        cell = new PdfPCell();
        cell.addElement(paragraph);
        cell.setPaddingTop(-3);
        cell.setPaddingBottom(4);
        cell.setBorderColor(new BaseColor(46, 110, 175));
        cell.setBorderWidth(1.1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(new Paragraph("\n"));
        cell.setBorder(0);
        table.addCell(cell);

        paragraph = new Paragraph("Total nombre de Lignes : " + NumFormat.fEntier().format(totalLigne) + "\nTotal € HT : " + NumFormat.fDouble().format(totalHT) + " €\nTVA (20%) : " + NumFormat.fDouble().format(totalHT * 0.2) + " €\nTotal € TTC : " + NumFormat.fDouble().format(totalHT * 1.2) + " €", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        cell = new PdfPCell();
        cell.addElement(paragraph);
        cell.setPaddingTop(-3);
        cell.setPaddingBottom(4);
        cell.setBorderColor(new BaseColor(46, 110, 175));
        cell.setBorderWidth(1.1f);
        table.addCell(cell);

        try {
            document.add(new Paragraph("\n"));
        } catch (DocumentException e) {
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
        try {
            document.add(table);
        } catch (DocumentException e) {
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
        document.close();
    }

    @NotNull
    private PdfPTable createCDNReg() {
        Font font = new Font(Font.FontFamily.HELVETICA, 8);
        Font fontTitre = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

        PdfPTable table = new PdfPTable(11);
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
        cell.setColspan(11);
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(11);
        cell.setFixedHeight(8);
        cell.setBorder(0);
        table.addCell(cell);

        String[][] listReg = {{"Code Banque", "Code Guichet", "N° du compte", "Clé RIB", "Domiciliation"}, {"16006", "21111", "00023064321", "95", "CREDIT AGRICOLE"}};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <= 10; j++) {
                if ((j % 2) == 0) {
                    cell = new PdfPCell(new Phrase(""));
                    cell.setBorder(0);
                    table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase(listReg[i][j / 2], font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }
            cell = new PdfPCell(new Phrase(""));
            cell.setColspan(11);
            cell.setFixedHeight(2);
            cell.setBorder(0);
            table.addCell(cell);
        }

        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(11);
        cell.setFixedHeight(2);
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Clause de réserve de propriété : le vendeur conserve la propriété pleine et entière des produits et services vendus jusqu'au paiement complet du prix, en application de la loi du 12/05/1980. Escompte pour paiement anticipé : néant.", font));
        cell.setColspan(11);
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(11);
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

        return table;
    }

    @NotNull
    private PdfPTable createTable(String result) {
        table = new PdfPTable(5);
        table.setWidthPercentage(100);
        int[] headerwidths = {10, 30, 12, 10, 10};
        try {
            table.setWidths(headerwidths);
        } catch (DocumentException e) {
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(result));
        } catch (DocumentException | FileNotFoundException e) {
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
        document.open();
        PdfPCell cell;
        Paragraph paragraph;

        String[] tableTitleList = {"Date Saisie", "Nom Dossier", "Nombre Lignes", "Tarif Ligne", "Total HT"};
        for (String title : tableTitleList) {
            cell = new PdfPCell();
            paragraph = new Paragraph(title, fontTitre);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(paragraph);
            cell.setPaddingTop(-3);
            cell.setPaddingBottom(4);
            cell.setBorderColor(new BaseColor(46, 110, 175));
            cell.setBorderWidth(1.1f);
            table.addCell(cell);
        }

        if (isNotEmpty(dataLigne)) {
            cell = new PdfPCell(new Paragraph(client.getLibelleTranches(), fontTitre));
            createTableHeader(table, cell);
            remplirLignes(parseLignes());
        }

        remplirAllIfNotEmpty();

        try {
            document.add(table);
        } catch (DocumentException e) {
            Erreur.creerFichierErreur(e.getMessage());
            e.printStackTrace();
        }
        document.close();
        return table;
    }

    private void createTableHeader(@NotNull PdfPTable table, @NotNull PdfPCell cell) {
        BaseColor baseColor = new BaseColor(46, 110, 175);
        cell.setColspan(5);
        cell.setPaddingBottom(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(baseColor);
        table.addCell(cell);
    }

    private void remplirLignes(@NotNull List<Ligne> data) {

        Font font = new Font(Font.FontFamily.HELVETICA, 10);

        for (Ligne ligne : data) {
            creerCell(new Paragraph(ligne.getDate(), font));
            creerCell(new Paragraph(ligne.getEntreprise(), font));
            creerCell(new Paragraph(NumFormat.fEntier().format(ligne.getNbLigne()), font));
            creerCell(new Paragraph(NumFormat.fTriple().format(ligne.getTarif()), font));
            creerCell(new Paragraph(NumFormat.fDouble().format(ligne.getTotal()) + " €", font));
        }
    }

    public void remplirIfNotEmpty(List<List<String>> data, String entete, int numCol) {
        if (isNotEmpty(data)) {
            PdfPCell cell = new PdfPCell(new Paragraph(entete, fontTitre));
            createTableHeader(table, cell);
            remplirLignes(createLigne(data, numCol));
        }
    }

    private void creerCell(@NotNull Paragraph paragraph) {
        PdfPCell cell = new PdfPCell();
        paragraph.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(paragraph);
        cell.setPaddingTop(-3);
        cell.setPaddingBottom(4);
        cell.setBorderColor(new BaseColor(46, 110, 175));
        table.addCell(cell);
    }

    private class HeaderTable extends PdfPageEventHelper {
        private final PdfPTable table;
        private float tableHeight;

        private HeaderTable() throws DocumentException, IOException {

            table = new PdfPTable(4);
            table.setTotalWidth(495);
            table.setLockedWidth(true);
            int[] headerwidths = {20, 100, 2, 50};
            table.setWidths(headerwidths);

            BaseColor baseColor = new BaseColor(46, 110, 175);

            PdfPCell cell;
            Paragraph paragraph;

            cell = new PdfPCell();
            Image image = Image.getInstance(MotsCles.IMG);
            cell.addElement(image);
            cell.setBorder(0);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell();
            paragraph = new Paragraph(MotsCles.adresseCPE, new Font(Font.FontFamily.HELVETICA, 10));
            cell.addElement(paragraph);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell();
            paragraph = new Paragraph("FACTURE N° " + Date.getYear() + "-" + NumFormat.fNbFact().format(nFacture), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
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
            cell.setBackgroundColor(baseColor);
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
            cell.setBorderColor(baseColor);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell();
            paragraph = new Paragraph("Date : " + Date.getDate() + "\nA régler avant le : " + Date.getARegler(), new Font(Font.FontFamily.HELVETICA, 10));
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            cell.addElement(paragraph);
            cell.setBorderColor(baseColor);
            table.addCell(cell);
            tableHeight = table.getTotalHeight();
        }

        private float getTableHeight() {
            return tableHeight;
        }

        public void onEndPage(@NotNull PdfWriter writer, @NotNull Document document) {
            table.writeSelectedRows(0, -1,
                    document.left(),
                    document.top() + ((document.topMargin() + tableHeight) / 2),
                    writer.getDirectContent());
        }
    }

    private static class BorderEvent implements PdfPTableEvent {
        public void tableLayout(PdfPTable table, @NotNull float[][] widths, @NotNull float[] heights, int headerRows, int rowStart, @NotNull PdfContentByte[] canvases) {
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
