package com.mindhub.Homebanking.DTO;

import java.time.LocalDate;

public class CardTransactionDTO {
    private Long id;
    private String number;
    private String cvv;
    private Double amount;
    private String description;
    private LocalDate thruDate;

    public CardTransactionDTO(){};
    public CardTransactionDTO(Long id, String number, String cvv, Double amount, String description, LocalDate thruDate) {
        this.id = id;
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
        this.thruDate = thruDate;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }
}
