package com.solutions.apex.fin_manager.repository;

import com.solutions.apex.fin_manager.model.Wallet;
import com.solutions.apex.fin_manager.model.WalletCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByUserId(Long id);
    List<Wallet> findByUserIdAndCurrency(Long userId, WalletCurrency currency);
    List<Wallet> findByBalanceGreaterThanEqual(BigDecimal amount);
}
