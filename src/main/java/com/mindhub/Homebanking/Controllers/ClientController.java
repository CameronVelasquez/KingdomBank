package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.DTO.ClientDTO;
import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.Card;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Repositories.AccountRepository;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.Homebanking.Utils.Utilities.accountNumber;
import static com.mindhub.Homebanking.Utils.Utilities.randomNumberCard;
import static java.util.stream.Collectors.toList;

@RestController
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientRepository repoClient;
    @Autowired
    private AccountRepository repoAccount;

    @RequestMapping("/api/clients")
    public List<ClientDTO> getClients() {
        return repoClient.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toList());
    }
    @RequestMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return new ClientDTO(repoClient.findById(id).orElse(null));
    }
    @RequestMapping(path = "api/clients", method = RequestMethod.POST)
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
        if (repoClient.findByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client newClient = new Client(first, lastName, email, passwordEncoder.encode(password));
        Account newAccount = new Account( accountNumber(repoAccount), LocalDateTime.now(), 0);
        newClient.addAccount(newAccount);
        repoClient.save(newClient);
        repoAccount.save(newAccount);

        return new ResponseEntity<>("Welcome ,"+ first+" "+lastName, HttpStatus.CREATED);
    }
    @RequestMapping("/api/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        String email = authentication.getName();
        Client client = repoClient.findByEmail(email);
        Set<Card> visibleCards = client.getCards().stream().filter(card -> card.getShowCard() == true).collect(Collectors.toSet());
        client.setCards(visibleCards);
        return new ClientDTO(client);
    }

}