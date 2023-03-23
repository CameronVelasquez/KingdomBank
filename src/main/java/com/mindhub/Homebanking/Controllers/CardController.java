package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.DTO.CardDTO;
import com.mindhub.Homebanking.DTO.CardTransactionDTO;
import com.mindhub.Homebanking.Models.*;
import com.mindhub.Homebanking.Repositories.TransactionRepository;
import com.mindhub.Homebanking.Services.AccountServices;
import com.mindhub.Homebanking.Services.CardServices;
import com.mindhub.Homebanking.Services.ClientServices;
import com.mindhub.Homebanking.Services.TransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.naming.ldap.Control;
import javax.persistence.Access;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.mindhub.Homebanking.Utils.Utilities.*;
import static java.util.stream.Collectors.toList;

@RestController
public class CardController {
    @Autowired
    ClientServices clientServices;
    @Autowired
    CardServices cardServices;
    @Autowired
    AccountServices accountServices;
    @Autowired
    TransactionServices transactionServices;

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> newCard(Authentication authentication,
                                          @RequestParam CardType type,
                                          @RequestParam CardColor color) {
        Client authenticatedClient = clientServices.findByEmail(authentication.getName());

        if (authenticatedClient.getCards().size() >= 6){
            return new ResponseEntity<> ("You cannot create more Cards", HttpStatus.FORBIDDEN);}
        if (authenticatedClient.getCards().stream().anyMatch(card -> type == card.getType() && color == card.getColor() && card.getShowCard() == true) ){
            return new ResponseEntity<>("Already have this card category", HttpStatus.FORBIDDEN);}

         Card addNewCard = new Card(type, color, authenticatedClient, cvv(), randomNumberCard(cardServices), LocalDate.now(), LocalDate.now().plusYears(5), true);
         cardServices.save(addNewCard);

        return new  ResponseEntity<>("New card created", HttpStatus.CREATED);

    }
    @GetMapping("/api/clients/current/cards")
    public List<CardDTO> getCurrentCards(Authentication authentication){
        Client client = clientServices.findByEmail(authentication.getName());
        List<Card> visibleCards = client.getCards().stream().filter(card -> card.getShowCard() == true).collect(Collectors.toList());
        return visibleCards.stream().map(card -> new CardDTO(card)).collect(toList());
    }


    //EXTRAS: Delete cards
    @PatchMapping("/api/clients/current/cards")
    public ResponseEntity<Object> deleteCards (Authentication authentication, @RequestParam String number){

        Client authenticatedClient = clientServices.findByEmail(authentication.getName());
        Card getCardToDelete = cardServices.findByNumber(number);

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
        cardServices.save(getCardToDelete);

        return new ResponseEntity<>("Card successfully deleted!", HttpStatus.ACCEPTED);
    }
    @CrossOrigin
    @Transactional
    @PostMapping("/api/cards/transactions")
    public ResponseEntity<Object> createCardTransaction(@RequestBody(required = false) CardTransactionDTO cardTransactionDTO){

        String number = cardTransactionDTO.getNumber();
        String cvv = cardTransactionDTO.getCvv();
        Double amount = cardTransactionDTO.getAmount();
        String description = cardTransactionDTO.getDescription();
        Short thruDateYear = cardTransactionDTO.getThruDateYear();
        String thruDateMonth = cardTransactionDTO.getThruDateMonth();

        Card clientCard = cardServices.findByNumber(number);
        Client client = clientCard.getClient();
        Account accountSelected = client.getAccounts().stream().findFirst().orElse(null);
        String fusion = thruDateYear+"-"+thruDateMonth;
        LocalDate thruDate = clientCard.getThruDate();
        String[] thruDate2 = clientCard.getThruDate().toString().split("-");
        String thruDateFinal = thruDate2[0]+"-"+thruDate2[1];
        System.out.println(thruDateFinal);
        if (number.isEmpty()){
            return new ResponseEntity<>("Card number is empty", HttpStatus.BAD_REQUEST);
        }
        if (cvv.isEmpty()){
            return new ResponseEntity<>("Cvv number is empty",HttpStatus.BAD_REQUEST);
        }
        if (amount == null){
            return new ResponseEntity<>("Amount is empty",HttpStatus.BAD_REQUEST);
        }
        if (description.isEmpty()){
            return new ResponseEntity<>("Description is empty",HttpStatus.BAD_REQUEST);
        }
        if (thruDateYear == null || thruDateMonth == null){
            return new ResponseEntity<>("Expiration number is empty",HttpStatus.BAD_REQUEST);
        }
        if (clientCard == null){
            return new ResponseEntity<>("The card number doesn't exist",HttpStatus.BAD_REQUEST);
        }
        if (!client.getCards().contains(clientCard)){
            return new ResponseEntity<>("You don't have this card",HttpStatus.FORBIDDEN);
        }
        if (!thruDateFinal.equals(fusion)){
            return new ResponseEntity<>("The expiration day is not the same",HttpStatus.BAD_REQUEST);
        }
        if (thruDate.isBefore(LocalDate.now())){
            return new ResponseEntity<>("The card is expired",HttpStatus.FORBIDDEN);
        }
        if (!cvv.equals(clientCard.getCvv())){
            return new ResponseEntity<>("The cvv is not the same",HttpStatus.BAD_REQUEST);
        }
        if (amount > accountSelected.getBalance()){
            return new ResponseEntity<>("the amount is insufficient",HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = new Transaction(TransactionType.DEBIT,amount,description, LocalDateTime.now(),currentBalanceDebit(accountServices,accountSelected,amount));
        accountSelected.addTransaction(transaction);
        accountSelected.setBalance(accountSelected.getBalance() - amount);
        transactionServices.save(transaction);

        return new ResponseEntity<>("Created",HttpStatus.CREATED);
    }
}
