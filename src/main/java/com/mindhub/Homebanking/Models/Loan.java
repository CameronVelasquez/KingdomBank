package com.mindhub.Homebanking.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String name;
    private double maxAmount;
    private Double fee;
    @ElementCollection
    @Column(name = "payment_fees")
    private List<Double> paymentFees = new ArrayList<>();
    @ElementCollection
    @Column(name = "payment")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan(){};

    public Loan(String name, double maxAmount, List<Integer> payments, Double fee, List<Double> paymentFees) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.fee = fee;
        this.paymentFees = paymentFees;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public List<Double> getPaymentFees() {
        return paymentFees;
    }

    public void setPaymentFees(List<Double> paymentFees) {
        this.paymentFees = paymentFees;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }
    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }
    public void addLoans(ClientLoan clientLoan) {
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }
    public Set<Client> getClients() {
        return clientLoans.stream().map(ClientLoan::getClient).collect(Collectors.toSet());
    }
}
