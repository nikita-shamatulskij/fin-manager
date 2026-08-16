package com.solutions.apex.fin_manager.repository;

import com.solutions.apex.fin_manager.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findBySenderWalletIdOrReceiverWalletId(Long senderWalletId, Long receiverWalletId, Pageable pageable);
    Page<Transaction> findBySenderWalletIdAndCreatedAtBetween(Long senderWalletId, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore, Pageable pageable);
    Page<Transaction> findByReceiverWalletIdAndCreatedAtBetween(Long receiverWalletId, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore, Pageable pageable);
    Page<Transaction> findByCreatedAtBetween(LocalDateTime createdAtAfter, LocalDateTime createdAtBefore, Pageable pageable);
}
