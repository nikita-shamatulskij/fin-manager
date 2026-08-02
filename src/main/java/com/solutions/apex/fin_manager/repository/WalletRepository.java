package com.solutions.apex.fin_manager.repository;

import com.solutions.apex.fin_manager.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByUserId(Long id);
}
