package com.mindhub.homebanking.services;

import com.lowagie.text.pdf.PdfPTable;
import com.mindhub.homebanking.models.Client;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PDFService {

    public void export(HttpServletResponse response, Client client1, Client client2, String account1, String account2,
                       double amount, String description) throws IOException;
}
