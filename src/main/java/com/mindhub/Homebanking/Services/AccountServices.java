package com.mindhub.Homebanking.Services;

import com.mindhub.Homebanking.Models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountServices {
    List<Account> findAll ();
    Account findByNumber(String number);
    Account findById(Long id);
    Boolean existByNumber(String number);
    void save (Account account);
}
