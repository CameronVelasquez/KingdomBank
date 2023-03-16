package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.DTO.AccountDTO;
import com.mindhub.Homebanking.DTO.CardDTO;
import com.mindhub.Homebanking.DTO.ClientDTO;
import com.mindhub.Homebanking.Models.*;
import com.mindhub.Homebanking.Repositories.AccountRepository;
import com.mindhub.Homebanking.Repositories.CardRepository;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
    public ResponseEntity<Object> createAccounts (Authentication authentication, @RequestParam (required = false) AccountType accountType){

        Client clientAuthenticated = clientRepository.findByEmail(authentication.getName());

        if( clientAuthenticated.getAccounts().size() >= 3 && clientAuthenticated.getAccounts().stream().allMatch(account -> account.getShowAccount() == true)){
            return new ResponseEntity<>("No more accounts allowed", HttpStatus.FORBIDDEN);
        }
        if (accountType == null){
            return new ResponseEntity<>("Account type field is empty", HttpStatus.BAD_REQUEST); }

        Account newAccount = new Account(accountNumber(accountRepository) ,LocalDateTime.now(), 0, true, accountType);
        clientAuthenticated.addAccount(newAccount);
        accountRepository.save(newAccount);
        return new ResponseEntity<>("Account successfully created", HttpStatus.CREATED);
    }

    @RequestMapping("api/clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        List<Account> visibleAccounts = client.getAccounts().stream().filter(card -> card.getShowAccount() == true).collect(Collectors.toList());
        return visibleAccounts.stream().map(account -> new AccountDTO(account)).collect(toList());
    }
    @RequestMapping(path = "/api/clients/current/accounts", method = RequestMethod.PATCH)
    public ResponseEntity<Object> deleteAccounts (Authentication authentication,
                                               @RequestParam String accountNumber,
                                               @RequestParam String accountDestiny){

        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());
        Account accountToDelete = accountRepository.findByNumber(accountNumber);
        Account accountDestini = accountRepository.findByNumber(accountDestiny);


        if (accountNumber.isEmpty()){
            return new ResponseEntity<>("Account number is empty",HttpStatus.BAD_REQUEST);
        }
        if (accountDestiny.isEmpty()){
            return new ResponseEntity<>("Account destiny is empty",HttpStatus.BAD_REQUEST);
        }
        if (accountToDelete == accountDestini){
            return new ResponseEntity<>("Account number and account destiny are the same",HttpStatus.BAD_REQUEST);
        }
        if (authenticatedClient.getAccounts().size() < 2){
            return new ResponseEntity<>("You need another account to delete one account",HttpStatus.FORBIDDEN);
        }
        if (accountToDelete == null){
            return new ResponseEntity<>("The account number doesn't exist",HttpStatus.BAD_REQUEST);
        }
        if (!authenticatedClient.getAccounts().contains(accountToDelete)){
            return new ResponseEntity<>("This account number does not belong to your accounts",HttpStatus.FORBIDDEN);
        }
        if (accountDestini == null){
            return new ResponseEntity<>("The account destiny doesn't exist", HttpStatus.BAD_REQUEST);
        }
        if (!authenticatedClient.getAccounts().contains(accountDestini)){
            return new ResponseEntity<>("This account destiny does not belong to your accounts",HttpStatus.FORBIDDEN);
        }
        if (accountToDelete.getShowAccount() == false || accountDestini.getShowAccount() == false){
            return new ResponseEntity<>("The account is already deleted",HttpStatus.BAD_REQUEST);
        }

        accountDestini.setBalance(accountDestini.getBalance()+accountToDelete.getBalance());
        accountToDelete.setBalance(0);
        accountToDelete.setShowAccount(false);
        accountRepository.save(accountToDelete);
        accountRepository.save(accountDestini);

        return new ResponseEntity<>("Successfully deleted", HttpStatus.ACCEPTED);
    }

}
