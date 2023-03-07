package com.mindhub.Homebanking.DTO;

import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.ClientLoan;
import com.mindhub.Homebanking.Models.Loan;

import java.util.List;

public class LoanApplicationDTO {

    private Long id; //primitivos a wrappers

    private Double amount;

    private Integer payments;

    private String accountNumber;

    public LoanApplicationDTO(){}

    public LoanApplicationDTO(Long id, Double amount, Integer payments, String accountNumber) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.accountNumber = accountNumber;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}