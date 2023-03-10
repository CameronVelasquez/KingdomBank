package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.DTO.LoanApplicationDTO;
import com.mindhub.Homebanking.DTO.LoanDTO;
import com.mindhub.Homebanking.Models.*;
import com.mindhub.Homebanking.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.Homebanking.Utils.Utilities.currentBalanceCredit;
import static com.mindhub.Homebanking.Utils.Utilities.loanFees;

@RestController
public class LoanController {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;

    @RequestMapping("/api/loans")
    @ResponseBody
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll().stream().map( LoanDTO::new ).collect(Collectors.toList());
    }
    @Transactional
    @RequestMapping(path="/api/loans" , method= RequestMethod.POST)
    public ResponseEntity<Object> addLoanApplications (@RequestBody (required = false) LoanApplicationDTO loanApplicationDTO, Authentication authentication) {


        Long id = loanApplicationDTO.getId();
        Double amount= loanApplicationDTO.getAmount();
        Integer payments = loanApplicationDTO.getPayments();
        String accountNumber = loanApplicationDTO.getAccountNumber();

        Loan selectedLoan = loanRepository.getReferencedById(id);
        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());
        Account selectedAccount = accountRepository.findByNumber(accountNumber);

        if (id == null || id <= 0){
            return new ResponseEntity<>("Loan id field is empty", HttpStatus.FORBIDDEN);}
        if (amount == null || amount <= 0){
            return new ResponseEntity<>("You must select an amount to request", HttpStatus.FORBIDDEN);}
        if (payments == null || payments <= 0){
            return new ResponseEntity<>("You must select a number of payments", HttpStatus.FORBIDDEN);}
        if (accountNumber.isEmpty()){
            return new ResponseEntity<>("You must select a destiny account", HttpStatus.FORBIDDEN);}
        if (selectedLoan == null){
            return new ResponseEntity<>( "the loan selected doesn't exist", HttpStatus.FORBIDDEN);}
        if (selectedLoan.getMaxAmount() < amount){
            return new ResponseEntity<>( "You exceeded the max amount allowed for this loan option", HttpStatus.FORBIDDEN);}
        if (!selectedLoan.getPayments().contains(payments)){
            return new ResponseEntity<>("The loan can't be paid in the selected number of payments", HttpStatus.FORBIDDEN);}
        if (selectedAccount == null){
            return new ResponseEntity<>( "the account selected doesn't exist", HttpStatus.FORBIDDEN);}
        if (!authenticatedClient.getAccounts().contains(selectedAccount)){
            return new ResponseEntity<>( "You do not posses this account number", HttpStatus.FORBIDDEN);}




        ClientLoan newClientLoan = new ClientLoan( loanFees(amount, selectedLoan), payments, authenticatedClient, selectedLoan);
        Transaction newTransaction = new Transaction(TransactionType.CREDIT, amount, selectedLoan.getName().toUpperCase() + ": Loan approved", LocalDateTime.now(), currentBalanceCredit(accountRepository, selectedAccount, amount));

        selectedLoan.addLoans(newClientLoan);
        selectedAccount.addTransaction(newTransaction);
        selectedAccount.setBalance(selectedAccount.getBalance() + amount);
        transactionRepository.save(newTransaction);
        clientLoanRepository.save(newClientLoan);
        return new ResponseEntity<>( "Loan successfully created", HttpStatus.CREATED);
    }
}
