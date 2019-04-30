package br.com.welike.instagram.repository;

import br.com.welike.instagram.model.StatusControl;
import br.com.welike.instagram.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StatusControlRepository extends JpaRepository<StatusControl, Long> {

    @Query("")
    StatusControl findByTransactionId(String transactionId);
    void deleteByTransactionId(String transactionId);

}
