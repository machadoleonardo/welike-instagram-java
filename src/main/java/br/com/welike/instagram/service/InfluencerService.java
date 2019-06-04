package br.com.welike.instagram.service;

import br.com.welike.instagram.enums.TransactionEnum;
import br.com.welike.instagram.model.Influencer;
import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.repository.InfluencerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Influencer> influencersToSave = influencers.stream()
                                                        .filter((influencer) -> !repository.existsByUserName(influencer.getUserName()))
                                                        .collect(Collectors.toList());
        repository.saveAll(influencersToSave);

        Transaction transaction = transactionService.findByTransactionId(transactionId);
        StatusControl statusControl = statusControlService.findByTransactionId(transaction.getTransactionId());

        Integer newScrapings = statusControl.getScrapings() + 1;
        statusControl.setScrapings(newScrapings);

        if (newScrapings.equals(statusControl.getTotalScrapings())) {
            transaction.setStatus(TransactionEnum.SUCESSO.getDescription());
        }

        influencers.addAll(transaction.getInfluencers());
        transaction.setInfluencers(new HashSet<>(influencers));

        transactionService.save(transaction);
        statusControlService.save(statusControl);
    }
}
