package br.com.welike.instagram.service;

import br.com.welike.instagram.enums.TransactionEnum;
import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.request.ScrapingRequest;
import br.com.welike.instagram.scraping.Scraper;
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

    public String startScraping(ScrapingRequest request) {
        String transactionId = String.valueOf(new Date().getTime());

        saveTransaction(transactionId);
        saveStatusControl(transactionId, request.getSeguindo().size());

        for (String username : request.getSeguindo()) {
            scraper.execute(username, transactionId, request.getMinFollowers());
        }

        return transactionId;
    }

    private void saveTransaction(String transactionId) {
        Transaction transaction = new Transaction();

        transaction.setTransactionId(transactionId);
        transaction.setStatus(TransactionEnum.EM_ANDAMENTO.getDescription());

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
