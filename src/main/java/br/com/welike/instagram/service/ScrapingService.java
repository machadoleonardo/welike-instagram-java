package br.com.welike.instagram.service;

import br.com.welike.instagram.request.ScrapingRequest;
import br.com.welike.instagram.scraping.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScrapingService {

    private final Scraper scraper;

    @Autowired
    public ScrapingService(Scraper scraper) {
        this.scraper = scraper;
    }

    public void executeScraping(ScrapingRequest request) throws InterruptedException {
        // TODO: Criar uma transactionId e um, registro no banco com status 'Em andamento'
        scraper.execute(request);
        // TODO: Retornar trancationId
    }

}
