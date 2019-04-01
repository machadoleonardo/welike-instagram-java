package br.com.welike.instagram.service;

import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    @Autowired
    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction getStatus(String transactionId) {
        return repository.findByTransactionId(transactionId);
    }

    public void save(Transaction transaction) {
        this.repository.save(transaction);
    }

}
