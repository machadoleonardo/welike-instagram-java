package br.com.welike.instagram.repository;

import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusControlRepository extends JpaRepository<StatusControl, Long> {

    StatusControl findByTransactionId(String transactionId);
    void deleteByTransactionId(String transactionId);

}
