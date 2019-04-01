package br.com.welike.instagram.controller;

import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.request.ScrapingRequest;
import br.com.welike.instagram.service.ScrapingService;
import br.com.welike.instagram.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/")
public class ScrapingController {

    private final ScrapingService scrapingService;
    private final TransactionService transactionService;

    @Autowired
    public ScrapingController(ScrapingService scrapingService, TransactionService transactionService) {
        this.scrapingService = scrapingService;
        this.transactionService = transactionService;
    }

    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public String scrapping(@RequestBody @Valid ScrapingRequest request) throws InterruptedException {
        return scrapingService.startScraping(request);
    }

    @GetMapping(value = "/{transactionId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Transaction status(@PathVariable String transactionId) {
        return transactionService.getStatus(transactionId);
    }
}