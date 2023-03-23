package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.DTO.TransactionDTO;
import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.Client;
import com.mindhub.Homebanking.Models.Transaction;
import com.mindhub.Homebanking.Models.TransactionType;
import com.mindhub.Homebanking.Repositories.TransactionRepository;
import com.mindhub.Homebanking.Services.AccountServices;
import com.mindhub.Homebanking.Services.ClientServices;
import com.mindhub.Homebanking.Services.TransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.Homebanking.Utils.Utilities.currentBalanceCredit;
import static com.mindhub.Homebanking.Utils.Utilities.currentBalanceDebit;

@RestController
public class TransactionController {
    @Autowired
    TransactionServices transactionServices;
    @Autowired
    AccountServices accountServices;
    @Autowired
    ClientServices clientServices;

    @Transactional
    @PostMapping("/api/transactions")
    public ResponseEntity<Object> createTransactions(Authentication authentication,
                                                     @RequestParam(required = false) Double amount,
                                                     @RequestParam String description,
                                                     @RequestParam String accountNumber,
                                                     @RequestParam String destinyAccount){


        Client clientAuthenticated = clientServices.findByEmail(authentication.getName());
        Account originAccount = accountServices.findByNumber(accountNumber);
        Account destiniAccount = accountServices.findByNumber(destinyAccount);

        if (amount == null ) {
            return new ResponseEntity<>("Amount field is empty", HttpStatus.FORBIDDEN);
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
        else if(!accountServices.existByNumber(accountNumber)){
            return new ResponseEntity<>("The origin account doesn't exist", HttpStatus.BAD_REQUEST);
        }
        else if(!clientAuthenticated.getAccounts().stream().anyMatch(account -> account.getNumber().equals(accountNumber))){
            return new ResponseEntity<>("The origin account doesn't belongs to any of your accounts", HttpStatus.FORBIDDEN);
        }
        else if(accountServices.findByNumber(destinyAccount) == null){
            return new ResponseEntity<>("The destiny account doesn't exist", HttpStatus.BAD_REQUEST);
        }
        else if(originAccount.getBalance() < amount){
            return new ResponseEntity<>("You don't have enough funds", HttpStatus.FORBIDDEN);
        }
        Transaction transactionDebit = new Transaction(TransactionType.DEBIT,  amount, description+" "+originAccount.getNumber(), LocalDateTime.now(), currentBalanceDebit(accountServices, originAccount, amount ));
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT,  amount, description+" "+destiniAccount.getNumber(), LocalDateTime.now(), currentBalanceCredit(accountServices, destiniAccount, amount) );
        originAccount.addTransaction(transactionDebit);
        destiniAccount.addTransaction(transactionCredit);
        transactionServices.save(transactionDebit);
        transactionServices.save(transactionCredit);
        originAccount.setBalance(originAccount.getBalance() - transactionDebit.getAmount());
        destiniAccount.setBalance(destiniAccount.getBalance() + transactionCredit.getAmount());
        accountServices.save(originAccount);
        accountServices.save(destiniAccount);

        return new ResponseEntity<>("Transaction was successfully created", HttpStatus.CREATED);
    }

    //PDF Y FILTRO JSON
    @GetMapping("/api/clients/current/transactions")
    public ResponseEntity<?> listOfTransactionsPerDate(@RequestParam String fromAccount ,
                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                         @RequestParam (required = false) String description,
                                                         @RequestParam (required = false) Double maxAmount,
                                                         @RequestParam (required = false) Double minAmount,
                                                         @RequestParam (required = false) TransactionType type ,
                                                         Authentication authentication){
        Client client = clientServices.findByEmail(authentication.getName());
        Account currentAccount = accountServices.findByNumber(fromAccount);
        Set<Transaction> newFilteredTransactions = currentAccount.getTransactions();

        if (client.getAccounts().stream().noneMatch(account -> account.getId() == currentAccount.getId()))
            return new ResponseEntity<>("You do not posses this account",HttpStatus.BAD_REQUEST);
        if (currentAccount.getShowAccount().equals(false))
            return new ResponseEntity<>("this account is not active",HttpStatus.BAD_REQUEST);
        if (startDate == null && endDate != null){
            newFilteredTransactions = newFilteredTransactions.stream().filter(transaction -> transaction.getDate().isBefore(endDate) || transaction.getDate().equals(endDate) ).collect(Collectors.toSet()); }
        if (startDate != null && endDate == null){
            newFilteredTransactions = newFilteredTransactions.stream().filter(transaction -> transaction.getDate().isAfter(startDate) || transaction.getDate().equals(startDate)).collect(Collectors.toSet()); }
        if (startDate != null && endDate != null && startDate.isBefore(endDate)){
            newFilteredTransactions = newFilteredTransactions.stream().filter(transaction -> transaction.getDate().isAfter(startDate)  && transaction.getDate().isBefore(endDate) || transaction.getDate().equals(startDate) || transaction.getDate().equals(endDate)).collect(Collectors.toSet()); }
        if (description != null && !description.isEmpty()){
            newFilteredTransactions = newFilteredTransactions.stream().filter(transaction -> transaction.getDescription().contains(description)).collect(Collectors.toSet()); }
        if(maxAmount != null && !maxAmount.isNaN() && minAmount == null){
            newFilteredTransactions = newFilteredTransactions.stream().filter(transaction -> transaction.getAmount() <= maxAmount).collect(Collectors.toSet()); }
        if(minAmount != null && !minAmount.isNaN() && maxAmount == null){
            newFilteredTransactions = newFilteredTransactions.stream().filter(transaction -> transaction.getAmount() >= minAmount).collect(Collectors.toSet()); }
        if(maxAmount != null && !maxAmount.isNaN() && minAmount != null && !minAmount.isNaN()){
            newFilteredTransactions = newFilteredTransactions.stream().filter(transaction -> transaction.getAmount() >= minAmount && transaction.getAmount() <= maxAmount).collect(Collectors.toSet()); }
        if(type != null && type == TransactionType.CREDIT){
            newFilteredTransactions = newFilteredTransactions.stream().filter(transaction -> transaction.getType() == TransactionType.CREDIT).collect(Collectors.toSet()); }
        if (type !=null && type == TransactionType.DEBIT){
            newFilteredTransactions =newFilteredTransactions.stream().filter(transaction -> transaction.getType() == TransactionType.DEBIT).collect(Collectors.toSet()); }





        return ResponseEntity.ok(newFilteredTransactions.stream().map(TransactionDTO::new).sorted((b, a)-> b.getDate().compareTo(a.getDate())).collect(Collectors.toSet()));
    }




   @GetMapping("/api/clients/current/accounts/account/accountTransactions")
    public List<TransactionDTO> allTransactions(Authentication authentication){
        Client authenticated = clientServices.findByEmail(authentication.getName());
        Account currentAccount = authenticated.getAccounts().stream().findAny().get();
        List<TransactionDTO> allTransactions= currentAccount.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
         return allTransactions;
    }

}
