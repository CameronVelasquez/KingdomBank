package com.mindhub.Homebanking.Services.Implementations;

import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Repositories.AccountRepository;
import com.mindhub.Homebanking.Services.AccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServicesImplementations implements AccountServices {
    @Autowired
    AccountRepository accountRepository;
    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }
    @Override
    public Boolean existByNumber(String number) {
        return accountRepository.existsByNumber(number);
    }


    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }


    @Override
    public void save(Account account) {
        accountRepository.save(account);

    }
}
