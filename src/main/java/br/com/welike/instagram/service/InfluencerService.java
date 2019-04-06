package br.com.welike.instagram.service;

import br.com.welike.instagram.model.Influencer;
import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.repository.InfluencerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class InfluencerService {

    private final String URL_GET_USER_INSTAGRAM = "https://www.instagram.com/{username}/?__a=1";

//    String url = "http://www.sample.com?foo={fooValue}";
//
//    Map<String, String> uriVariables = new HashMap();
//    uriVariables.put("fooValue", 2);
//
//    // "http://www.sample.com?foo=2"
//    restTemplate.getForObject(url, Object.class, uriVariables);

    private final InfluencerRepository repository;
    private final TransactionService transactionService;
    private final StatusControlService statusControlService;

    @Autowired
    public InfluencerService(InfluencerRepository repository, TransactionService transactionService, StatusControlService statusControlService) {
        this.repository = repository;
        this.transactionService = transactionService;
        this.statusControlService = statusControlService;
    }

    public void saveInfluencers(List<String> usernames, String transactionId) {
        List<Influencer> influencers = getInfluencers(usernames);

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

    private List<Influencer> getInfluencers(List<String> usernames) {
        List<Influencer> influencersToSave = new ArrayList<>(0);
        for (String username : usernames) {
            influencersToSave.add(getInfluencerInApiInstagram(username));
        }
        return influencersToSave;
    }

    private Influencer getInfluencerInApiInstagram(String username) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://localhost:5000/start/" + username, Influencer.class);
    }
}
