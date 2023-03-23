package com.mindhub.Homebanking.Services.Implementations;

import com.mindhub.Homebanking.Models.Loan;
import com.mindhub.Homebanking.Repositories.LoanRepository;
import com.mindhub.Homebanking.Services.LoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServicesImplementations implements LoanServices {
    @Autowired
    LoanRepository loanRepository;
    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @Override
    public Loan getReferencedById(Long id) {
        return loanRepository.getReferencedById(id);
    }

    @Override
    public void save(Loan loan) {
        loanRepository.save(loan);
    }
}
