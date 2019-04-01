package br.com.welike.instagram.controller;

import br.com.welike.instagram.request.ScrapingRequest;
import br.com.welike.instagram.service.ScrapingService;
import br.com.welike.instagram.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/")
public class ScrapingController {

    private final ScrapingService scrapingService;
    private final StatusService statusService;

    @Autowired
    public ScrapingController(ScrapingService scrapingService, StatusService statusService) {
        this.scrapingService = scrapingService;
        this.statusService = statusService;
    }

    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void scrapping(@RequestBody @Valid ScrapingRequest request) {
        try {
            scrapingService.executeScraping(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/{transactionId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void status(@PathVariable String transactionId) {
        try {
            statusService.getStatus(transactionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}