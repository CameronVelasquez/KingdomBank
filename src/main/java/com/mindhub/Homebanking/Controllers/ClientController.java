package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.DTO.ClientDTO;
import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.AccountType;
import com.mindhub.Homebanking.Models.Card;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Services.AccountServices;
import com.mindhub.Homebanking.Services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.Homebanking.Utils.Utilities.accountNumber;
import static java.util.stream.Collectors.toList;

@RestController
public class ClientController {
    @Autowired
    ClientServices clientServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AccountServices accountServices;

    @GetMapping("/api/clients")
    public List<ClientDTO> getClients() {
        return clientServices.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toList());
    }
    @GetMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(clientServices.findById(id));
    }
    @PostMapping("/api/clients")
    public ResponseEntity<Object> register(

            @RequestParam String first, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (first.isEmpty()) {
            return new ResponseEntity<>("Missing FirstName", HttpStatus.BAD_REQUEST);
        }
        else if (lastName.isEmpty()) {
            return new ResponseEntity<>("Missing LastName", HttpStatus.BAD_REQUEST);
        }
        else if (email.isEmpty()) {
            return new ResponseEntity<>("Missing Email", HttpStatus.BAD_REQUEST);
        }
        else if (password.isEmpty()) {
            return new ResponseEntity<>("Missing Password", HttpStatus.BAD_REQUEST);
        }
        if (clientServices.findByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client newClient = new Client(first, lastName, email, passwordEncoder.encode(password));
        Account newAccount = new Account( accountNumber(accountServices), LocalDateTime.now(), 0, true, AccountType.CHECKING);
        newClient.addAccount(newAccount);
        clientServices.save(newClient);
        accountServices.save(newAccount);

        return new ResponseEntity<>("Welcome ,"+ first+" "+lastName, HttpStatus.CREATED);
    }
    @GetMapping("/api/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        String email = authentication.getName();
        Client client = clientServices.findByEmail(email);
        Set<Card> visibleCards = client.getCards().stream().filter(card -> card.getShowCard() == true).collect(Collectors.toSet());
        Set<Account> visibleAccounts = client.getAccounts().stream().filter(account -> account.getShowAccount() == true).collect(Collectors.toSet());

        client.setCards(visibleCards);
        client.setAccounts(visibleAccounts);

        return new ClientDTO(client);
    }

}