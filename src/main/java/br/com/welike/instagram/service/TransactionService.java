package br.com.welike.instagram.service;

import br.com.welike.instagram.converter.TransactionResponseConverter;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.repository.TransactionRepository;
import br.com.welike.instagram.response.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final TransactionResponseConverter transactionResponseConverter;

    @Autowired
    public TransactionService(TransactionRepository repository, TransactionResponseConverter transactionResponseConverter) {
        this.repository = repository;
        this.transactionResponseConverter = transactionResponseConverter;
    }

    public TransactionResponse getStatus(String transactionId) {
        return transactionResponseConverter.encode(findByTransactionId(transactionId));
    }

    public Transaction findByTransactionId(String transactionId) {
        return repository.findByTransactionId(transactionId);
    }

    public Transaction save(Transaction transaction) {
        return this.repository.save(transaction);
    }

}
