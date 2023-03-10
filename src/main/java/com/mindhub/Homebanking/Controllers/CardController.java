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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        if (authenticatedClient.getCards().stream().anyMatch(card -> type == card.getType() && color == card.getColor() && card.getShowCard() == true) ){
            return new ResponseEntity<>("Already have this card category", HttpStatus.FORBIDDEN);}

         Card addNewCard = new Card(type, color, authenticatedClient, cvv(), randomNumberCard(cardRepository), LocalDate.now(), LocalDate.now().plusYears(5), true);
         cardRepository.save(addNewCard);

        return new  ResponseEntity<>("New card created", HttpStatus.CREATED);

    }
    @RequestMapping("/api/clients/current/cards")
    public List<CardDTO> getCurrentCards(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        List<Card> visibleCards = client.getCards().stream().filter(card -> card.getShowCard() == true).collect(Collectors.toList());
        return visibleCards.stream().map(card -> new CardDTO(card)).collect(toList());
    }

    //EXTRAS: Delete cards
    @RequestMapping(path = "/api/clients/current/cards", method = RequestMethod.PATCH)
    public ResponseEntity<Object> deleteCards (Authentication authentication, @RequestParam String number){

        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());
        Card getCardToDelete = cardRepository.findByNumber(number);

            if(authenticatedClient.getCards().stream().noneMatch(card -> card == getCardToDelete)){
                return new ResponseEntity<>("You do not posses this card", HttpStatus.FORBIDDEN);}
            if( getCardToDelete.getCardholder().isEmpty()){
                return new  ResponseEntity<>("You must select Cardholder option", HttpStatus.BAD_REQUEST);}
            if( getCardToDelete.getType().toString().isEmpty()){
                return new  ResponseEntity<>("You must select a card type option", HttpStatus.BAD_REQUEST);}
            if( getCardToDelete.getColor().toString().isEmpty()){
                return new  ResponseEntity<>("You must select a card color option", HttpStatus.BAD_REQUEST);}
            if( getCardToDelete.getCvv().isEmpty()){
                return new  ResponseEntity<>("You must select CVV option", HttpStatus.BAD_REQUEST);}
            if( getCardToDelete.getFromDate().toString().isEmpty()){
                return new  ResponseEntity<>("You must select Date of Creation option", HttpStatus.BAD_REQUEST);}
            if( getCardToDelete.getThruDate().toString().isEmpty()){
                return new  ResponseEntity<>("You must select Date of Expiration option", HttpStatus.BAD_REQUEST);}
            if(getCardToDelete.getShowCard() == false){
                return new ResponseEntity<>("You can't delete this card", HttpStatus.BAD_REQUEST);}



        getCardToDelete.setShowCard(false);
        cardRepository.save(getCardToDelete);

        return new ResponseEntity<>("Card successfully deleted!", HttpStatus.ACCEPTED);
    }
}
