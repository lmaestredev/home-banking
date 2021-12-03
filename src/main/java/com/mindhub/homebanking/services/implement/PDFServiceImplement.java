package com.mindhub.homebanking.services.implement;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.PDFService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class PDFServiceImplement implements PDFService {

    @Override
    public void export(HttpServletResponse response,
                       Client client1,
                       Client client2,
                       String account1,
                       String account2,
                       double amount,
                       String description) throws IOException{

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        Locale locale = new Locale("es", "US");
        NumberFormat currency = NumberFormat.getCurrencyInstance(locale);
        String amountFormatCurrency = currency.format(amount);
        DateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String currentDatTime= dateFormat.format(new Date());

        document.open();
        Image image = Image.getInstance("./src/main/resources/static/web/assets/bank.png");
        image.scaleAbsolute(50,50);
        image.setAbsolutePosition(120,760);
        document.add(image);
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(20);
        Color bankColor = new Color(30, 30, 47, 0);
        fontTitle.setColor(bankColor);
        Paragraph paragraph = new Paragraph("Mindhub Brothers Bank", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA);
        paragraphFont.setSize(15);
        Paragraph paragraph1 = new Paragraph("Transaction voucher.", paragraphFont);
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph1.setSpacingBefore(15);
        paragraph1.setSpacingAfter(20);
        document.add(paragraph1);

        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA);
        dateFont.setSize(12);
        Paragraph dateParagraph = new Paragraph("Date: " + currentDatTime, dateFont);
        dateParagraph.setSpacingAfter(15);
        document.add(dateParagraph);

        Font paragraphTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        paragraphTitleFont.setSize(12);
        Font paragraphFont2 = FontFactory.getFont(FontFactory.HELVETICA);
        paragraphFont2.setSize(12);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{7.5f, 5.5f});
        PdfPCell cell = new PdfPCell();
        Phrase phrase1 = new Phrase();
        Chunk text1 = new Chunk("Origin account: ", paragraphTitleFont);
        Chunk text2 = new Chunk(account1, paragraphFont2);
        phrase1.add(text1);
        phrase1.add(text2);
        cell.setPhrase(phrase1);
        cell.setBorder(0);
        cell.setExtraParagraphSpace(10);
        table.addCell(cell);

        Phrase phrase2 = new Phrase();
        Chunk text3 = new Chunk("Owner: ", paragraphTitleFont);
        Chunk text4 = new Chunk(client1.getFirstName()+" "+client1.getLastName(), paragraphFont2);
        phrase2.add(text3);
        phrase2.add(text4);
        cell.setPhrase(phrase2);
        table.addCell(cell);

        Phrase phrase3 = new Phrase();
        Chunk text5 = new Chunk("Amount: ", paragraphTitleFont);
        Chunk text6 = new Chunk(amountFormatCurrency,paragraphFont2);
        phrase3.add(text5);
        phrase3.add(text6);
        cell.setPhrase(phrase3);
        table.addCell(cell);

        Phrase phrase4 = new Phrase();
        Chunk text7 = new Chunk("Description: ", paragraphTitleFont);
        Chunk text8 = new Chunk(description,paragraphFont2);
        phrase4.add(text7);
        phrase4.add(text8);
        cell.setPhrase(phrase4);
        table.addCell(cell);

        Phrase phrase5 = new Phrase();
        Chunk text9= new Chunk("Destination account: ", paragraphTitleFont);
        Chunk text10= new Chunk(account2,paragraphFont2);
        phrase5.add(text9);
        phrase5.add(text10);
        cell.setPhrase(phrase5);
        table.addCell(cell);

        Phrase phrase6 = new Phrase();
        Chunk text11 = new Chunk("Owner: ", paragraphTitleFont);
        Chunk text12 = new Chunk(client2.getFirstName()+" "+client2.getLastName(), paragraphFont2);
        phrase6.add(text11);
        phrase6.add(text12);
        cell.setPhrase(phrase6);
        table.addCell(cell);

        document.add(table);

        document.close();
    };

}
