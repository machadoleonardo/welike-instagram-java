package br.com.welike.instagram.service;

import br.com.welike.instagram.converter.AccountInfluencerConverter;
import br.com.welike.instagram.model.Influencer;
import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.repository.InfluencerRepository;
import br.com.welike.instagram.scraping.UserScraping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class InfluencerService {

    @Value("${instagram.auth.login}")
    private String login;

    @Value("${instagram.auth.password}")
    private String password;

    private final InfluencerRepository repository;
    private final TransactionService transactionService;
    private final StatusControlService statusControlService;

    @Autowired
    public InfluencerService(InfluencerRepository repository, TransactionService transactionService,
                             StatusControlService statusControlService) {
        this.repository = repository;
        this.transactionService = transactionService;
        this.statusControlService = statusControlService;
    }

    public void saveInfluencers(List<Influencer> influencers, String transactionId) throws IOException, InterruptedException {
        save(influencers);

        Transaction transaction = transactionService.findByTransactionId(transactionId);
        transaction.setInfluencers(new HashSet<>(influencers));

        transactionService.save(transaction);
        saveStatusControl(transactionId);
    }

    private void saveStatusControl(String transactionId) {
        StatusControl statusControl = statusControlService.findByTransactionId(transactionId);
        statusControl.setScrapings(statusControl.getScrapings() + 1);
        statusControlService.save(statusControl);
    }

    public List<Influencer> save(List<Influencer> influencers) {
        return repository.saveAll(influencers);
    }

    public Influencer save(Influencer influencer) {
        return repository.save(influencer);
    }
}
