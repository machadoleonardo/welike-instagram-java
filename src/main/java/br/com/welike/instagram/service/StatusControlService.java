package br.com.welike.instagram.service;

import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.repository.StatusControlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class StatusControlService {

    private final StatusControlRepository repository;

    @Autowired
    public StatusControlService(StatusControlRepository repository) {
        this.repository = repository;
    }

    public void save(StatusControl statusControl) {
        repository.save(statusControl);
    }

    public StatusControl findByTransactionId(String transactionId) {
        return repository.findByTransactionId(transactionId);
    }

    @Transactional
    public void deleteByTransactionId(String transactionId) {
        repository.deleteByTransactionId(transactionId);
    }

}
