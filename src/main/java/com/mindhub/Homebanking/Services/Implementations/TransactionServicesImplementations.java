package com.mindhub.Homebanking.Services.Implementations;

import com.mindhub.Homebanking.Models.Transaction;
import com.mindhub.Homebanking.Repositories.TransactionRepository;
import com.mindhub.Homebanking.Services.TransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServicesImplementations implements TransactionServices {
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }
}
