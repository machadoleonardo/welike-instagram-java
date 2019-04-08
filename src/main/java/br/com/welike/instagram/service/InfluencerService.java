package br.com.welike.instagram.service;

import br.com.welike.instagram.converter.AccountInfluencerConverter;
import br.com.welike.instagram.model.Influencer;
import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.repository.InfluencerRepository;
import me.postaddict.instagram.scraper.Instagram;
import me.postaddict.instagram.scraper.cookie.CookieHashSet;
import me.postaddict.instagram.scraper.cookie.DefaultCookieJar;
import me.postaddict.instagram.scraper.interceptor.ErrorInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgentInterceptor;
import me.postaddict.instagram.scraper.interceptor.UserAgents;
import me.postaddict.instagram.scraper.model.Account;
import okhttp3.OkHttpClient;
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
    private final AccountInfluencerConverter converter;

    @Autowired
    public InfluencerService(InfluencerRepository repository, TransactionService transactionService, StatusControlService statusControlService, AccountInfluencerConverter converter) {
        this.repository = repository;
        this.transactionService = transactionService;
        this.statusControlService = statusControlService;
        this.converter = converter;
    }

    public void saveInfluencers(List<String> usernames, String transactionId) throws IOException {
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

    private List<Influencer> getInfluencers(List<String> usernames) throws IOException {
        List<Influencer> influencersToSave = new ArrayList<>(0);
        for (String username : usernames) {
            Account influencerAccount = getInfluencerByUsername(username);
            influencersToSave.add(converter.encode(influencerAccount));
        }
        return influencersToSave;
    }

    private Account getInfluencerByUsername(String username) throws IOException {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new UserAgentInterceptor(UserAgents.WIN10_CHROME))
                .addInterceptor(new ErrorInterceptor())
                .cookieJar(new DefaultCookieJar(new CookieHashSet()))
                .build();

        Instagram instagram = new Instagram(httpClient);

        instagram.basePage();
        instagram.login(login, password);
        instagram.basePage();

        return instagram.getAccountByUsername(username);
    }
}
