package br.com.welike.instagram.service;

import br.com.welike.instagram.model.Reference;
import br.com.welike.instagram.repository.ReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReferenceService {

    private final ReferenceRepository repository;

    @Autowired
    public ReferenceService(ReferenceRepository repository) {
        this.repository = repository;
    }

    boolean existsByUserName(String username) {
        return repository.existsByUserName(username);
    }

    List<Reference> findAllByUserNameIn(List<String> usernames) {
        return repository.findAllByUserNameIn(usernames);
    }

    public void save(Reference reference) {
        repository.save(reference);
    }

    public void save(List<Reference> references) {
        repository.saveAll(references);
    }
}
