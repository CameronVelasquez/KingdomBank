package com.mindhub.Homebanking;

import com.mindhub.Homebanking.Models.*;
import com.mindhub.Homebanking.Repositories.AccountRepository;
import com.mindhub.Homebanking.Repositories.CardRepository;
import com.mindhub.Homebanking.Services.AccountServices;
import com.mindhub.Homebanking.Utils.Utilities;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.AssertionErrors;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class UtilitiesTest {

//    @Test
//    void cvv() {
//        String cvvNumber = Utilities.cvv();
//        assertThat(cvvNumber.length(), is(3));
//    }
//
//    @Test
//    void randomString() {
//        String number = Utilities.randomString();
//        assertThat(number.length(), is(19));
//    }
//
//    @Test
//    void randomNumberCard() {
//        CardRepository cardRepository = mock(CardRepository.class);
//        String randomNumber = Utilities.randomString();
//        assertFalse(cardRepository.existsCardByNumber(randomNumber));
//        assertThat(randomNumber, is(notNullValue()));
//
//
//    }
//
//    @Test
//    void generateNumber() {
//        String generatedNumber = Utilities.GenerateNumber();
//        assertThat(generatedNumber, is(not(emptyOrNullString())));
//    }
//
//    @Test
//    void accountNumber() {
//        AccountRepository accountRepository = mock(AccountRepository.class);
//        String randomNumber = Utilities.GenerateNumber();
//        boolean verifyNumber = accountRepository.existsByNumber(randomNumber);
//        assertFalse(verifyNumber);
//        assertThat(randomNumber, is(notNullValue()));
//    }
//
//    @Test
//    void loanFees() {
//        Loan loan = new Loan("TestLoan", 500000.00, List.of(12,24,36,48,60), 1.10, List.of(1.0,1.05,1.1,1.15,1.2), true);
//        Double loanfees = Utilities.loanFees(500.0, loan );
//        assertThat(loanfees, is(550.0));
//    }
//
//    @Test
//    void currentBalanceCredit() {
//        AccountServices accountServices = mock(AccountServices.class);
//        List<Account> accounts = accountServices.findAll();
//        Transaction transactionTest = new Transaction(TransactionType.CREDIT, 500.00, "sample", LocalDateTime.now(), 5000.00);
//
//        for (Account account : accounts){
//            Double currentBalance = Utilities.currentBalanceCredit(accountServices, account, transactionTest.getAmount());
//            assertThat( currentBalance, is(5500.0));
//        };
//    }
//
//    @Test
//    void currentBalanceDebit() {
//        AccountServices accountServices = mock(AccountServices.class);
//        List<Account> accounts = accountServices.findAll();
//        Transaction transactionTest = new Transaction(TransactionType.DEBIT, 500.00, "sample", LocalDateTime.now(), 5000.00);
//
//        for (Account account : accounts){
//            Double currentBalance = Utilities.currentBalanceCredit(accountServices, account, transactionTest.getAmount());
//            assertThat( currentBalance, is(4500.0));
//        };
//    }


}
