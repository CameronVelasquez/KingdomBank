package com.mindhub.Homebanking.Controllers;

import com.mindhub.Homebanking.DTO.LoanApplicationDTO;
import com.mindhub.Homebanking.DTO.LoanDTO;
import com.mindhub.Homebanking.Models.*;
import com.mindhub.Homebanking.Repositories.*;
import com.mindhub.Homebanking.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    ClientServices clientServices;
    @Autowired
    LoanServices loanServices;
    @Autowired
    ClientLoanServices clientLoanServices;
    @Autowired
    TransactionServices transactionServices;
    @Autowired
    AccountServices accountServices;

    @GetMapping("/api/loans")
    @ResponseBody
    public List<LoanDTO> getLoans(){
        return loanServices.findAll().stream().map( LoanDTO::new ).collect(Collectors.toList());
    }
    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> addLoanApplications (@RequestBody (required = false) LoanApplicationDTO loanApplicationDTO, Authentication authentication) {


        Long id = loanApplicationDTO.getId();
        Double amount= loanApplicationDTO.getAmount();
        Integer payments = loanApplicationDTO.getPayments();
        String accountNumber = loanApplicationDTO.getAccountNumber();

        Loan selectedLoan = loanServices.getReferencedById(id);
        Client authenticatedClient = clientServices.findByEmail(authentication.getName());
        Account selectedAccount = accountServices.findByNumber(accountNumber);

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
        if(authenticatedClient.getClientLoans().stream().anyMatch(clientLoan -> clientLoan.getLoan() == selectedLoan)){
            return new ResponseEntity<>( "You are currently paying this loan", HttpStatus.BAD_REQUEST);}




        ClientLoan newClientLoan = new ClientLoan( loanFees(amount, selectedLoan), payments, authenticatedClient, selectedLoan);
        Transaction newTransaction = new Transaction(TransactionType.CREDIT, amount, selectedLoan.getName().toUpperCase() + ": Loan approved", LocalDateTime.now(), currentBalanceCredit(accountServices, selectedAccount, amount));

        selectedLoan.addLoans(newClientLoan);
        selectedAccount.addTransaction(newTransaction);
        selectedAccount.setBalance(selectedAccount.getBalance() + amount);
        transactionServices.save(newTransaction);
        clientLoanServices.save(newClientLoan);
        return new ResponseEntity<>( "Loan successfully created", HttpStatus.CREATED);
    }
    @PostMapping("/api/admin/loans")
    public ResponseEntity<Object> createLoans (@RequestBody Loan loan){

        String name = loan.getName();
        double maxAmount = loan.getMaxAmount();
        List<Integer> payments = loan.getPayments();
        Double fee = loan.getFee();
        List<Double> feePayments = loan.getPaymentFees();
        Boolean show = true;

        Loan loans = new Loan(name,maxAmount,payments,fee,feePayments,show);

        loanServices.save(loans);

        return new ResponseEntity<>("Created",HttpStatus.CREATED);
    }
}
