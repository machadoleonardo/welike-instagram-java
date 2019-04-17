package br.com.welike.instagram.service;

import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.request.ScrapingRequest;
import br.com.welike.instagram.scraping.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

@Service
public class ScrapingControlService {

    private final TransactionService transactionService;
    private final StatusControlService statusControlService;
    private final Scraper scraper;

    @Autowired
    public ScrapingControlService(TransactionService transactionService, StatusControlService statusControlService, Scraper scraper) {
        this.transactionService = transactionService;
        this.statusControlService = statusControlService;
        this.scraper = scraper;
    }

    public String startScraping(ScrapingRequest request) throws InterruptedException, IOException, AWTException {
        String transactionId = String.valueOf(new Date().getTime());

        saveTransaction(transactionId);
        saveStatusControl(transactionId, request.getUserName().size());

        for (String username : request.getUserName()) {
            scraper.execute(username, transactionId);
        }

        return transactionId;
    }

    private void saveTransaction(String transactionId) {
        Transaction transaction = new Transaction();

        transaction.setTransactionId(transactionId);
        transaction.setStatus("Sucesso");

        transactionService.save(transaction);
    }

    private void saveStatusControl(String transactionId, int totalScrapings) {
        StatusControl statusControl = new StatusControl();

        statusControl.setTotalScrapings(totalScrapings);
        statusControl.setTransactionId(transactionId);
        statusControl.setScrapings(0);

        statusControlService.save(statusControl);
    }

}
