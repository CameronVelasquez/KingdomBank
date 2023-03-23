package com.mindhub.Homebanking.DTO;

import com.mindhub.Homebanking.Models.Account;
import com.mindhub.Homebanking.Models.AccountType;
import com.mindhub.Homebanking.Models.Transaction;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private Long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private AccountType accountType;
    private Set<TransactionDTO> transactions;

    public AccountDTO(){}
    public AccountDTO(Account account){
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.id = account.getId();
        this.accountType = account.getAccountType();
        this.transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
    }

    public String getNumber() {
        return number;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public double getBalance() { return balance; }
    public Long getId() {return id; }
    public AccountType getAccountType() { return accountType; }
    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}
