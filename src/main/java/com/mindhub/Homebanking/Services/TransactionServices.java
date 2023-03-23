package com.mindhub.Homebanking.Services;

import com.mindhub.Homebanking.Models.Transaction;

import java.util.List;

public interface TransactionServices {
    void save(Transaction transaction);

     List<Transaction> findAll();
}
