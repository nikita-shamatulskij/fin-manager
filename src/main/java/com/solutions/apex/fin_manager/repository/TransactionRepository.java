package com.solutions.apex.fin_manager.repository;

import com.solutions.apex.fin_manager.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findBySenderWalletIdOrReceiverWalletId(Long senderWalletId, Long receiverWalletId, Pageable pageable);
}
