package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.DTO.AccountDTO;
import com.mindhub.Homebanking.Models.*;
import com.mindhub.Homebanking.Services.AccountServices;
import com.mindhub.Homebanking.Services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.Homebanking.Utils.Utilities.accountNumber;
import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {

    @Autowired
    AccountServices accountServices;
    @Autowired
    ClientServices clientServices;

    @GetMapping("/api/accounts")
    public List<AccountDTO> getAccounts(){
        return accountServices.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountServices.findById(id));
    }

    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> createAccounts (Authentication authentication, @RequestParam (required = false) AccountType accountType){

        Client clientAuthenticated = clientServices.findByEmail(authentication.getName());

        if( clientAuthenticated.getAccounts().size() >= 3 && clientAuthenticated.getAccounts().stream().allMatch(account -> account.getShowAccount() == true)){
            return new ResponseEntity<>("No more accounts allowed", HttpStatus.FORBIDDEN);
        }
        if (accountType == null){
            return new ResponseEntity<>("Account type field is empty", HttpStatus.BAD_REQUEST); }

        Account newAccount = new Account(accountNumber(accountServices) ,LocalDateTime.now(), 0, true, accountType);
        clientAuthenticated.addAccount(newAccount);
        accountServices.save(newAccount);
        return new ResponseEntity<>("Account successfully created", HttpStatus.CREATED);
    }

    @GetMapping("api/clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        Client client = clientServices.findByEmail(authentication.getName());
        List<Account> visibleAccounts = client.getAccounts().stream().filter(card -> card.getShowAccount() == true).collect(Collectors.toList());
        return visibleAccounts.stream().map(account -> new AccountDTO(account)).collect(toList());
    }
    @PatchMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> deleteAccounts (Authentication authentication,
                                               @RequestParam String accountNumber,
                                               @RequestParam String accountDestiny){

        Client authenticatedClient = clientServices.findByEmail(authentication.getName());
        Account accountToDelete = accountServices.findByNumber(accountNumber);
        Account accountDestini = accountServices.findByNumber(accountDestiny);


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
        accountServices.save(accountToDelete);
        accountServices.save(accountDestini);

        return new ResponseEntity<>("Successfully deleted", HttpStatus.ACCEPTED);
    }

}
