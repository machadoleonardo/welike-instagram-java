package br.com.welike.instagram.service;

import br.com.welike.instagram.repository.InfluencerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfluencerService {

    private final InfluencerRepository repository;

    @Autowired
    public InfluencerService(InfluencerRepository repository) {
        this.repository = repository;
    }
}
