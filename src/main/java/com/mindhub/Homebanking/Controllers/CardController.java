package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.DTO.CardDTO;
import com.mindhub.Homebanking.Models.Card;
import com.mindhub.Homebanking.Models.CardColor;
import com.mindhub.Homebanking.Models.CardType;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Repositories.CardRepository;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.Homebanking.Utils.Utilities.cvv;
import static com.mindhub.Homebanking.Utils.Utilities.randomNumberCard;
import static java.util.stream.Collectors.toList;

@RestController
public class CardController {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CardRepository cardRepository;

    @RequestMapping(path = "/api/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> newCard(Authentication authentication,
                                          @RequestParam CardType type,
                                          @RequestParam CardColor color) {
        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());

        if (authenticatedClient.getCards().size() >= 6){
            return new ResponseEntity<> ("You cannot create more Cards", HttpStatus.FORBIDDEN);}
        if (authenticatedClient.getCards().stream().anyMatch(card -> type == card.getType() && color == card.getColor()) ){
            return new ResponseEntity<>("Already have this card category", HttpStatus.FORBIDDEN);}

         Card addNewCard = new Card(type, color, authenticatedClient, cvv(), randomNumberCard(cardRepository));
         cardRepository.save(addNewCard);

        return new  ResponseEntity<>("New card created", HttpStatus.CREATED);

    }
    @RequestMapping("/api/clients/current/cards")
    public List<CardDTO> getCurrentCards(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards().stream().map(account -> new CardDTO(account)).collect(toList());
    }

}
