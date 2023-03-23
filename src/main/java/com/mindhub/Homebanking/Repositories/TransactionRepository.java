package com.mindhub.Homebanking.Repositories;

import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    //List<Transaction> findByAccount(Account account);
}
