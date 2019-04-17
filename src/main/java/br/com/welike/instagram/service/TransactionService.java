package br.com.welike.instagram.service;

import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.model.Transaction;
import br.com.welike.instagram.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final StatusControlService statusControlService;

    @Autowired
    public TransactionService(TransactionRepository repository, StatusControlService statusControlService) {
        this.repository = repository;
        this.statusControlService = statusControlService;
    }

    public Transaction getStatus(String transactionId) {
        StatusControl statusControl = statusControlService.findByTransactionId(transactionId);
        if (!statusControl.getScrapings().equals(statusControl.getTotalScrapings())) {
            return getTransactionEmAnadamento(transactionId);
        }
        return findByTransactionId(transactionId);
    }

    private Transaction getTransactionEmAnadamento(String transactionId) {
        Transaction transaction = new Transaction();

        transaction.setStatus("Em andamento");
        transaction.setTransactionId(transactionId);

        return transaction;
    }

    public Transaction findByTransactionId(String transactionId) {
        return repository.findByTransactionId(transactionId);
    }

    public void save(Transaction transaction) {
        this.repository. save(transaction);
    }

}
