package com.mindhub.Homebanking.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String cardholder;
    private CardType type;
    private CardColor color;
    private String number;
    private String cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;
    private Boolean showCard;

    public Card(){};

    public Card(CardType type, CardColor color, Client client, String cvv, String number, LocalDate fromDate, LocalDate thruDate, Boolean showCard) {
        this.type = type;
        this.color = color;
        this.cardholder = client.getFirstName() + " " + client.getLastName() ; //nombre concat
        this.number = number ; //metodo de numero16 mas no repeticion
        this.cvv = cvv ; //3 numeros
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.client = client;
        this.showCard = showCard;
    }

    public Long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Boolean getShowCard() {
        return showCard;
    }

    public void setShowCard(Boolean showCard) {
        this.showCard = showCard;
    }
}
