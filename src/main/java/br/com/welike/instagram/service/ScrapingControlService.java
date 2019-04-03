package br.com.welike.instagram.service;

import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.request.ScrapingRequest;
import br.com.welike.instagram.scraping.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    public String startScraping(ScrapingRequest request) throws InterruptedException {
        String transactionId = String.valueOf(new Date());

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
