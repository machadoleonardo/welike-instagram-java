package br.com.welike.instagram.repository;

import br.com.welike.instagram.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findByTransactionId(String transactionId);

}
