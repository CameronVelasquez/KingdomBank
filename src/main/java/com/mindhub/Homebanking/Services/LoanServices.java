package com.mindhub.Homebanking.Services;

import com.mindhub.Homebanking.Models.Loan;

import java.util.List;

public interface LoanServices {

    List<Loan> findAll ();
    Loan getReferencedById(Long id);
    void save(Loan loan);
}
