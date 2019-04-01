package br.com.welike.instagram.service;

import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.request.ScrapingRequest;
import br.com.welike.instagram.scraping.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ScrapingService {

    private final TransactionService transactionService;
    private final Scraper scraper;

    @Autowired
    public ScrapingService(TransactionService transactionService, Scraper scraper) {
        this.transactionService = transactionService;
        this.scraper = scraper;
    }

    public String startScraping(ScrapingRequest request) throws InterruptedException {
        String transactionId = String.valueOf(new Date());
        transactionService.save(generateTransaction(transactionId));
        executeScraping(request);
        return transactionId;
    }

    @Async
    protected void executeScraping(ScrapingRequest request) throws InterruptedException {
        scraper.execute(request);
    }



    private Transaction generateTransaction(String transactionId) {
        Transaction transaction = new Transaction();

        transaction.setTransactionId(transactionId);
        transaction.setStatus("Em andamento");

        return transaction;
    }

}
