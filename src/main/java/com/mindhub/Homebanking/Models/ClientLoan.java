package com.mindhub.Homebanking.Models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
public class ClientLoan {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
     @GenericGenerator(name = "native", strategy = "native")
     private Long id;
     private double amountSelected;
     private int paymentSelected;
     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "client_id")
     private Client client;
     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "loan_id")
     private Loan loan;

     public ClientLoan(){};
     public ClientLoan(double amountSelected, int paymentSelected, Client client, Loan loan) {
         this.amountSelected = amountSelected;
         this.paymentSelected = paymentSelected;
         this.client = client;
         this.loan= loan;
    }

    public Long getId() {
        return id;
    }
    public double getAmountSelected() {
        return amountSelected;
    }

    public void setAmountSelected(double amountSelected) {
        this.amountSelected = amountSelected;
    }

    public int getPaymentSelected() {
        return paymentSelected;
    }

    public void setPaymentSelected(int paymentSelected) {
        this.paymentSelected = paymentSelected;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

}
