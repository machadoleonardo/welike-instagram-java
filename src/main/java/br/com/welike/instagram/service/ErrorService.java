package br.com.welike.instagram.service;

import br.com.welike.instagram.model.Error;
import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.repository.ErrorRepository;
import br.com.welike.instagram.repository.StatusControlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ErrorService {

    private final ErrorRepository repository;

    @Autowired
    public ErrorService(ErrorRepository repository) {
        this.repository = repository;
    }

    public void save(Error error) {
        repository.save(error);
    }

}
