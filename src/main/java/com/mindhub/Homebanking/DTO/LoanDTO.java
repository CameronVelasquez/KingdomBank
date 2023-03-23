package com.mindhub.Homebanking.DTO;

import com.mindhub.Homebanking.Models.Loan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoanDTO {
    private Long id;
    private String name;
    private double maxAmount;
    private List<Integer> payments;
    private Double fee;
    private List<Double> paymentFees;

    public LoanDTO(){};

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.fee = loan.getFee();
        this.paymentFees = loan.getPaymentFees();

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public Double getFee() {return fee; }

    public List<Double> getPaymentsFees() {
        return paymentFees;
    }
}
