import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PDF extends Mamasita implements MotsCles {
    private static Calendar calendar = Calendar.getInstance();

    private double totalHT = 0;
    private int totalLigne = 0;

    void createPdf(HSSFSheet sheet, String filename, int nFacture, Entreprise entreprise) {
        HeaderTable event = null;
        try {
            event = new HeaderTable(nFacture, entreprise);
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
        readXls(sheet);
        parseXls();
        try {
            document.add(new Paragraph("\n\n"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        try {
            document.add(createTable(filename, entreprise));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        int nvDos = createLigne(dataND, 16, entreprise).size();
        for (Ligne ligne : createLigne(dataLigne, 0, entreprise))
            totalLigne += ligne.getNbLigne();
        for (Ligne ligne : createLigne(dataLe, 8, entreprise))
            totalLigne += ligne.getNbLigne();

        Font font = new Font(Font.HELVETICA, 11);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        int[] headerwidths = {25, 5, 60, 5, 60};
        try {
            table.setWidths(headerwidths);
        } catch (DocumentException e) {
            creerFichierErreur(e);
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

        String phrase;
        if (nvDos == 0 || nvDos == 1)
            phrase = nvDos + " nouveau dossier\n";
        else
            phrase = nvDos + " nouveaux dossiers\n";

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

        paragraph = new Paragraph("Total nombre de Lignes : " + formatEntier().format(entreprise.getTotalLigne()) + "\nTotal € HT : " + formatDouble().format(entreprise.getTotalHT()) + "€\nTVA (20%) : " + formatDouble().format(entreprise.getTotalHT() * 0.2) + "€\nTotal € TTC : " + formatDouble().format(entreprise.getTotalHT() * 1.2) + "€", font);
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
            creerFichierErreur(e);
            e.printStackTrace();
        }
        try {
            document.add(table);
        } catch (DocumentException e) {
            creerFichierErreur(e);
            e.printStackTrace();
        }
        document.close();
    }

    private PdfPTable createCDNReg() {
        Font font = new Font(Font.HELVETICA, 8);
        Font fontTitre = new Font(Font.HELVETICA, 11, Font.BOLD);

        PdfPTable table = new PdfPTable(11);
        PdfPCell cell = new PdfPCell();
        int headerwidths[] = {10, 25, 10, 25, 10, 40, 10, 25, 10, 40, 10};
        table.setWidthPercentage(100);
        try {
            table.setWidths(headerwidths);
        } catch (DocumentException e) {
            creerFichierErreur(e);
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
                switch ((j % 2)) {
                    case 0:
                        cell = new PdfPCell(new Phrase(""));
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    default:
                        cell = new PdfPCell(new Phrase(listReg[i][j / 2], font));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                        break;
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

        cell = new PdfPCell(new Phrase("TVA sur les encaissements", new Font(Font.HELVETICA, 8, Font.BOLD)));
        cell.setColspan(2);
        cell.setBorder(0);
        table.addCell(cell);

        return table;
    }

    private void populateTable(PdfPTable table, List<Ligne> data, Entreprise entreprise) {
        Font font = new Font(Font.HELVETICA, 10);

        for (Ligne ligne : data) {
            creerCell(new Paragraph(ligne.getDate(), font), table);
            creerCell(new Paragraph(ligne.getEntreprise(), font), table);
            creerCell(new Paragraph(formatEntier().format(ligne.getNbLigne()), font), table);
            creerCell(new Paragraph(formatTriple().format(ligne.getTarif()), font), table);
            creerCell(new Paragraph(formatDouble().format(ligne.getTotal()) + "€", font), table);

            totalLigne += ligne.getNbLigne();
            totalHT += ligne.getTotal();

        }
        entreprise.setTotalLigne(totalLigne);
        entreprise.setTotalHT(totalHT);
    }

    private PdfPTable createTable(String RESULT, Entreprise entreprise) {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        int headerwidths[] = {10, 30, 12, 10, 10};
        try {
            table.setWidths(headerwidths);
        } catch (DocumentException e) {
            creerFichierErreur(e);
            e.printStackTrace();
        }
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        } catch (DocumentException | FileNotFoundException e) {
            creerFichierErreur(e);
            e.printStackTrace();
        }
        document.open();
        PdfPCell cell;
        Paragraph paragraph;
        Font fontTitre = new Font(Font.HELVETICA, 10, Font.BOLD, new BaseColor(46, 110, 175));

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
            cell = new PdfPCell(new Paragraph("Lignes", fontTitre));
            createTableHeader(table, cell);
            populateTable(table, parseLignes(entreprise), entreprise);
        }

        remplirIfNotEmpty(dataIB, "Import Banque", fontTitre, table, TARIF_IB, entreprise);
        remplirIfNotEmpty(data471, "471", fontTitre, table, TARIF_471, entreprise);
        remplirIfNotEmpty(dataLe, "Lettrage", fontTitre, table, TARIF_LE, entreprise);
        remplirIfNotEmpty(dataSF, "ScanFact", fontTitre, table, TARIF_SF, entreprise);
        remplirIfNotEmpty(dataAI, "Attache Image", fontTitre, table, TARIF_AI, entreprise);
        remplirIfNotEmpty(dataDP, "Découpe PDF", fontTitre, table, TARIF_DP, entreprise);
        remplirIfNotEmpty(dataTVA, "TVA", fontTitre, table, TARIF_TVA, entreprise);
        remplirIfNotEmpty(dataREV, "Révision", fontTitre, table, TARIF_REV, entreprise);
        remplirIfNotEmpty(dataEI, "Ecritures Importées", fontTitre, table, TARIF_EI, entreprise);
        remplirIfNotEmpty(dataLinkup, "Linkup", fontTitre, table, TARIF_LK, entreprise);
        remplirIfNotEmpty(dataND, "Nouveaux Dossiers", fontTitre, table, TARIF_ND, entreprise);
        remplirIfNotEmpty(dataAF, "Dossiers Spécifiques", fontTitre, table, TARIF_AF, entreprise);

        try {
            document.add(table);
        } catch (DocumentException e) {
            creerFichierErreur(e);
            e.printStackTrace();
        }
        document.close();
        return table;
    }

    private void createTableHeader(PdfPTable table, PdfPCell cell) {
        BaseColor baseColor = new BaseColor(46, 110, 175);
        cell.setColspan(5);
        cell.setPaddingBottom(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(baseColor);
        table.addCell(cell);
    }

    private void remplirIfNotEmpty(List<List<String>> data, String entete, Font fontTitre, PdfPTable table, int tarif, Entreprise entreprise) {
        if (isNotEmpty(data)) {
            PdfPCell cell = new PdfPCell(new Paragraph(entete, fontTitre));
            createTableHeader(table, cell);
            populateTable(table, createLigne(data, tarif, entreprise), entreprise);
        }
    }

    private void creerCell(Paragraph paragraph, PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        paragraph.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(paragraph);
        cell.setPaddingTop(-3);
        cell.setPaddingBottom(4);
        cell.setBorderColor(new BaseColor(46, 110, 175));
        table.addCell(cell);
    }

    public class HeaderTable extends PdfPageEventHelper {
        private PdfPTable table;
        private float tableHeight;

        private HeaderTable(int nFacture, Entreprise entreprise) throws DocumentException, IOException {

            table = new PdfPTable(4);
            table.setTotalWidth(495);
            table.setLockedWidth(true);
            int headerwidths[] = {20, 100, 2, 50};
            table.setWidths(headerwidths);

            BaseColor baseColor = new BaseColor(46, 110, 175);

            PdfPCell cell;
            Paragraph paragraph;

            cell = new PdfPCell();
            Image image = Image.getInstance(IMG);
            cell.addElement(image);
            cell.setBorder(0);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell();
            paragraph = new Paragraph(adresseCPE, new Font(Font.HELVETICA, 10));
            cell.addElement(paragraph);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            table.addCell(cell);

            NumberFormat nbFactFormat = NumberFormat.getInstance(Locale.FRANCE);
            nbFactFormat.setMinimumIntegerDigits(3);

            cell = new PdfPCell();
            paragraph = new Paragraph("FACTURE N° " + calendar.get(Calendar.YEAR) + "-" + nbFactFormat.format(nFacture), new Font(Font.HELVETICA, 12, Font.BOLD));
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
            paragraph = new Paragraph(entreprise.getNomEntreprise() + "\nAdresse : " + entreprise.getAdresse() + "\nCP : " + entreprise.getCp().substring(0, 5) + "   Ville : " + entreprise.getVille(), new Font(Font.HELVETICA, 10));
            cell.addElement(paragraph);
            cell.setBorderColor(baseColor);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(0);
            table.addCell(cell);

            String date = "";
            String dateReg = "";
            Calendar calendar = Calendar.getInstance();

            switch (calendar.get(Calendar.MONTH)) {
                case 1:
                    date = "31/01/" + calendar.get(Calendar.YEAR);
                    dateReg = "20/02/" + calendar.get(Calendar.YEAR);
                    break;
                case 2:
                    date = "28/02/" + calendar.get(Calendar.YEAR);
                    dateReg = "20/03/" + calendar.get(Calendar.YEAR);
                    break;
                case 3:
                    date = "31/03/" + calendar.get(Calendar.YEAR);
                    dateReg = "20/04/" + calendar.get(Calendar.YEAR);
                    break;
                case 4:
                    date = "30/04/" + calendar.get(Calendar.YEAR);
                    dateReg = "20/05/" + calendar.get(Calendar.YEAR);
                    break;
                case 5:
                    date = "31/05/" + calendar.get(Calendar.YEAR);
                    dateReg = "20/06/" + calendar.get(Calendar.YEAR);
                    break;
                case 6:
                    date = "30/06/" + calendar.get(Calendar.YEAR);
                    dateReg = "20/07/" + calendar.get(Calendar.YEAR);
                    break;
                case 7:
                    date = "31/07/" + calendar.get(Calendar.YEAR);
                    dateReg = "20/08/" + calendar.get(Calendar.YEAR);
                    break;
                case 8:
                    date = "31/08/" + calendar.get(Calendar.YEAR);
                    dateReg = "20/09/" + calendar.get(Calendar.YEAR);
                    break;
                case 9:
                    date = "30/09/" + calendar.get(Calendar.YEAR);
                    dateReg = "20/10/" + calendar.get(Calendar.YEAR);
                    break;
                case 10:
                    date = "31/10/" + calendar.get(Calendar.YEAR);
                    dateReg = "20/11/" + calendar.get(Calendar.YEAR);
                    break;
                case 11:
                    date = "30/11/" + calendar.get(Calendar.YEAR);
                    dateReg = "20/12/" + calendar.get(Calendar.YEAR);
                    break;
                case 0:
                    date = "31/12/" + calendar.get(Calendar.YEAR);
                    dateReg = "20/01/" + calendar.get(Calendar.YEAR) + 1;
                    break;
            }

            cell = new PdfPCell();
            paragraph = new Paragraph("Date : " + date + "\nA régler avant le : " + dateReg, new Font(Font.HELVETICA, 10));
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            cell.addElement(paragraph);
            cell.setBorderColor(baseColor);
            table.addCell(cell);
            tableHeight = table.getTotalHeight();
        }

        private float getTableHeight() {
            return tableHeight;
        }

        public void onEndPage(PdfWriter writer, Document document) {
            table.writeSelectedRows(0, -1,
                    document.left(),
                    document.top() + ((document.topMargin() + tableHeight) / 2),
                    writer.getDirectContent());
        }
    }

    public class BorderEvent implements PdfPTableEvent {
        public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
            float width[] = widths[0];
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
