package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.DTO.AccountDTO;
import com.mindhub.Homebanking.DTO.CardDTO;
import com.mindhub.Homebanking.DTO.ClientDTO;
import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Repositories.AccountRepository;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.Homebanking.Utils.Utilities.accountNumber;
import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }
    @RequestMapping(path = "/api/clients/current/accounts", method = RequestMethod.POST)
        public ResponseEntity<Object> createAccounts (Authentication authentication){

        Client clientAuthenticated = clientRepository.findByEmail(authentication.getName());

        if( clientAuthenticated.getAccounts().size() >= 3 ){
            return new ResponseEntity<>("No more accounts allowed", HttpStatus.FORBIDDEN);
        }

        Account newAccount = new Account(accountNumber(accountRepository) ,LocalDateTime.now(), 0);
        clientAuthenticated.addAccount(newAccount);
        accountRepository.save(newAccount);
        return new ResponseEntity<>("Account successfully created", HttpStatus.CREATED);
    }

    @RequestMapping("api/clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        return clientRepository.findByEmail(authentication.getName()).getAccounts().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

}
