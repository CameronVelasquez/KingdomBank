package com.mindhub.Homebanking.DTO;

import com.mindhub.Homebanking.Models.Transaction;
import com.mindhub.Homebanking.Models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;
    private Double showCurrentBalance;
    public TransactionDTO(){}

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.showCurrentBalance = transaction.getShowCurrentBalance();
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Double getShowCurrentBalance() {
        return showCurrentBalance;
    }
}
