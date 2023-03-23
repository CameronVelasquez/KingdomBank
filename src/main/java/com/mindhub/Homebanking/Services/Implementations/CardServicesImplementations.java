package com.mindhub.Homebanking.Services.Implementations;

import com.mindhub.Homebanking.Models.Card;
import com.mindhub.Homebanking.Repositories.CardRepository;
import com.mindhub.Homebanking.Services.CardServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServicesImplementations implements CardServices {
    @Autowired
    CardRepository cardRepository;
    @Override
    public void save(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Boolean existCardByNumber(String number) {
        return cardRepository.existsCardByNumber(number);
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public List<Card> findAll() {
        return cardRepository.findAll();
    }
}
