package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Models.Transaction;
import com.mindhub.Homebanking.Models.TransactionType;
import com.mindhub.Homebanking.Repositories.AccountRepository;
import com.mindhub.Homebanking.Repositories.ClientRepository;
import com.mindhub.Homebanking.Repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.mindhub.Homebanking.Utils.Utilities.*;

@RestController
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Transactional
    @RequestMapping(path = "/api/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransactions(Authentication authentication,

            @RequestParam (required = false) Double amount, @RequestParam String description,
            @RequestParam String accountNumber, @RequestParam String destinyAccount){


        Client clientAuthenticated = clientRepository.findByEmail(authentication.getName());
        Account originAccount = accountRepository.findByNumber(accountNumber);
        Account destiniAccount = accountRepository.findByNumber(destinyAccount);

        if (amount == null ) {
            return new ResponseEntity<>("Amount field is empty",HttpStatus.FORBIDDEN);
        }
        if(amount < 1){
            return new ResponseEntity<>("try a higher amount", HttpStatus.FORBIDDEN);
        }
        else if (description.isEmpty()) {
            return new ResponseEntity<>("Missing description", HttpStatus.FORBIDDEN);
        }
        else if (accountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing account number", HttpStatus.FORBIDDEN);
        }
        else if (destinyAccount.isEmpty()) {
            return new ResponseEntity<>("Missing receptor account number", HttpStatus.FORBIDDEN);
        }
        else if(accountNumber.equals(destinyAccount)){
            return new ResponseEntity<>("The origin account can't be the same as the reception account", HttpStatus.BAD_REQUEST);
        }
        else if(!accountRepository.existsByNumber(accountNumber)){
            return new ResponseEntity<>("The origin account doesn't exist", HttpStatus.BAD_REQUEST);
        }
        else if(!clientAuthenticated.getAccounts().stream().anyMatch(account -> account.getNumber().equals(accountNumber))){
            return new ResponseEntity<>("The origin account doesn't belongs to any of your accounts", HttpStatus.FORBIDDEN);
        }
        else if(accountRepository.findByNumber(destinyAccount) == null){
            return new ResponseEntity<>("The destiny account doesn't exist", HttpStatus.BAD_REQUEST);
        }
        else if(originAccount.getBalance() < amount){
            return new ResponseEntity<>("You don't have enough funds", HttpStatus.FORBIDDEN);
        }
        Transaction transactionDebit = new Transaction(TransactionType.DEBIT,  amount, description, LocalDateTime.now(), currentBalanceDebit(accountRepository, originAccount, amount ));
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT,  amount, description, LocalDateTime.now(), currentBalanceCredit(accountRepository, destiniAccount, amount) );
        originAccount.addTransaction(transactionDebit);
        destiniAccount.addTransaction(transactionCredit);
        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);
        originAccount.setBalance(originAccount.getBalance() - transactionDebit.getAmount());
        destiniAccount.setBalance(destiniAccount.getBalance() + transactionCredit.getAmount());
        accountRepository.save(originAccount);
        accountRepository.save(destiniAccount);

        return new ResponseEntity<>("Transaction was successfully created", HttpStatus.CREATED);
    }

}
