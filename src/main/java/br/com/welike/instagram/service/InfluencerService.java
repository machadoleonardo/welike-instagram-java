package br.com.welike.instagram.service;

import br.com.welike.instagram.model.Influencer;
import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.repository.InfluencerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class InfluencerService {

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

    public void save(List<Influencer> influencers, String transactionId) {
        repository.saveAll(influencers);

        Transaction transaction = transactionService.findByTransactionId(transactionId);

        influencers.addAll(transaction.getInfluencers());
        transaction.setInfluencers(new HashSet<>(influencers));

        transactionService.save(transaction);

        StatusControl statusControl = statusControlService.findByTransactionId(transactionId);
        statusControl.setScrapings(statusControl.getScrapings() + 1);
        statusControlService.save(statusControl);
    }
}
