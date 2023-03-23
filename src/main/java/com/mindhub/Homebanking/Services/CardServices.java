package com.mindhub.Homebanking.Services;

import com.mindhub.Homebanking.Models.Card;

import java.util.List;

public interface CardServices {
    void save(Card card);
    Boolean existCardByNumber(String number);
    Card findByNumber(String number);
    List<Card> findAll();
}
