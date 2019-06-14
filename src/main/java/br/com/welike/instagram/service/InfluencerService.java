package br.com.welike.instagram.service;

import br.com.welike.instagram.model.Influencer;
import br.com.welike.instagram.repository.InfluencerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfluencerService {

    private final InfluencerRepository repository;

    @Autowired
    public InfluencerService(InfluencerRepository repository) {
        this.repository = repository;
    }

    public List<Influencer> save(List<Influencer> influencers) {
        return repository.saveAll(influencers);
    }

    public Influencer save(Influencer influencer) {
        return repository.save(influencer);
    }

    public boolean existsByUserName(String username) {
        return repository.existsByUserName(username);
    }

    public List<Influencer> findAllByUserNameIn(List<String> usernames) {
        return repository.findAllByUserNameIn(usernames);
    }
}
