package br.com.welike.instagram.service;

import br.com.welike.instagram.request.ScrapingRequest;
import br.com.welike.instagram.scraping.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    private final Scraper scraper;

    @Autowired
    public StatusService(Scraper scraper) {
        this.scraper = scraper;
    }

    public void getStatus(String transactionId) {
        scraper.execute(request);
    }

}
