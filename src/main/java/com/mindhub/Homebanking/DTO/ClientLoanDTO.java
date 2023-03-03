package com.mindhub.Homebanking.DTO;

import com.mindhub.Homebanking.Models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private long loanId;
    private String name;
    private double amount;
    private int payments;

    public ClientLoanDTO(){};
    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.payments = clientLoan.getPaymentSelected();
        this.amount = clientLoan.getAmountSelected();
    }

    public long getId() {
        return id;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }
}
