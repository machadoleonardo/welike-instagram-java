package br.com.welike.instagram.controller;

import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.request.ScrapingRequest;
import br.com.welike.instagram.response.ScrapingResponse;
import br.com.welike.instagram.response.TransactionResponse;
import br.com.welike.instagram.service.ScrapingControlService;
import br.com.welike.instagram.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/")
public class ScrapingController {

    private final ScrapingControlService scrapingControlService;
    private final TransactionService transactionService;

    @Autowired
    public ScrapingController(ScrapingControlService scrapingControlService, TransactionService transactionService) {
        this.scrapingControlService = scrapingControlService;
        this.transactionService = transactionService;
    }

    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ScrapingResponse scrapping(@RequestBody @Valid ScrapingRequest request) {
        ScrapingResponse scrapingResponse = new ScrapingResponse();
        scrapingResponse.setTransactionId(scrapingControlService.startScraping(request));
        return scrapingResponse;
    }

    @GetMapping(value = "/{transactionId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse status(@PathVariable String transactionId) {
        return transactionService.getStatus(transactionId);
    }
}