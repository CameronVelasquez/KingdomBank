package com.mindhub.Homebanking;

import com.mindhub.Homebanking.Models.*;
import com.mindhub.Homebanking.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.mindhub.Homebanking.Utils.Utilities.*;


@SpringBootApplication
public class HomebankingApplication {
//	@Autowired
//	private PasswordEncoder passwordEncoder;
	public static void main(String[] args) { SpringApplication.run(HomebankingApplication.class, args);	}


//	@Bean
//	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
//		return (args) -> {
//			LocalDateTime now = LocalDateTime.now();
//			LocalDateTime nextDay = now.plusDays(1);
//			LocalDateTime yesterday = now.minusDays(1);
//			LocalDateTime lastMonth = now.minusMonths(1);
//			LocalDateTime lastTwoMonths = now.minusMonths(2);
//
//
//			Client Melba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba"));
//			Client Cameron = new Client("Cameron", "Velasquez", "cameronlino@outlook.com", passwordEncoder.encode("564Cam"));
//			Client Admin = new Client("Admin", "Admin", "admin@mindhub.com", passwordEncoder.encode("000000"));
//
//			Account account1 = new Account("VIN001", now, 5000.00, true);
//			Account account2 = new Account( "VIN002", nextDay, 7500.00, true);
//			Account account3 = new Account( "VIN003", nextDay, 7500.00, true);
//
//
//
//			//Transacciones account1
//			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 1.00, "transfered from bank account", now.minusDays(10), 1.0 );
//			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 0.00, "transfered from bank account", now.minusDays(9) , 1.0);
//			Transaction transaction3 = new Transaction(TransactionType.DEBIT, 1.00, "transfered to another bank account", now.minusDays(8), 0.0 );
//			Transaction transaction4 = new Transaction(TransactionType.DEBIT, 0.00, "transfered to another bank account", now.minusDays(7), 0.0 );
//			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 1.00, "transfered from bank account", now.minusDays(6), 1.0  );
//
//			account1.addTransaction(transaction1);
//			account1.addTransaction(transaction2);
//			account1.addTransaction(transaction3);
//			account1.addTransaction(transaction4);
//			account1.addTransaction(transaction5);
//
//			//Transacciones account2
//			Transaction transaction6 = new Transaction(TransactionType.DEBIT, 4000.00, "transfered to another bank account", now.minusHours(10), 4000.0  );
//			Transaction transaction7 = new Transaction(TransactionType.DEBIT, 1000.00, "transfered to another bank account", now.minusHours(9) , 3000.0 );
//			Transaction transaction8 = new Transaction(TransactionType.CREDIT, 3000.00, "transfered from bank account", now.minusHours(8) , 6000.0 );
//			Transaction transaction9 = new Transaction(TransactionType.DEBIT, 500.00, "transfered to another bank account", now.minusHours(7), 5500.0 );
//			Transaction transaction10 = new Transaction(TransactionType.DEBIT, 500.00, "transfered to another bank account", now.minusHours(6) , 5000.0 );
//
//			account2.addTransaction(transaction6);
//			account2.addTransaction(transaction7);
//			account2.addTransaction(transaction8);
//			account2.addTransaction(transaction9);
//			account2.addTransaction(transaction10);
//
//			//Transacciones account 1
//			Transaction transaction11 = new Transaction(TransactionType.CREDIT, 0.00,"Sale of jacket",LocalDateTime.now().minusDays(5), 1.0 );
//			Transaction transaction12 = new Transaction(TransactionType.CREDIT, 0.00,"Sale of footwear",LocalDateTime.now().minusDays(4), 1.0 );
//			Transaction transaction13 = new Transaction(TransactionType.CREDIT, 0.00,"Sale of jeweler's",LocalDateTime.now().minusDays(3), 1.0 );
//			Transaction transaction14 = new Transaction(TransactionType.DEBIT, 1.00,"Purchase of papers",LocalDateTime.now().minusDays(2), 0.0 );
//			Transaction transaction15 = new Transaction(TransactionType.CREDIT, 5000.00,"Patreon",LocalDateTime.now(), 5000.0);
//
//			account1.addTransaction(transaction11);
//			account1.addTransaction(transaction12);
//			account1.addTransaction(transaction13);
//			account1.addTransaction(transaction14);
//			account1.addTransaction(transaction15);
//
//            //Transacciones account 2
//			Transaction transaction16 = new Transaction(TransactionType.CREDIT, 500.99,"Sale of jacket",LocalDateTime.now().minusHours(5), 5500.0 );
//			Transaction transaction17 = new Transaction(TransactionType.CREDIT, 4000.99,"Sale of footwear",LocalDateTime.now().minusHours(4), 9500.0 );
//			Transaction transaction18 = new Transaction(TransactionType.DEBIT, 500.00,"Purchase of keyboard",LocalDateTime.now().minusHours(3), 9000.0 );
//			Transaction transaction19 = new Transaction(TransactionType.DEBIT, 1000.00,"Purchase of microphone",LocalDateTime.now().minusHours(2), 8000.0 );
//			Transaction transaction20 = new Transaction(TransactionType.DEBIT, 500.00,"Purchase of mouse",LocalDateTime.now().minusHours(1),7500.0  );
//
//			account2.addTransaction(transaction16);
//			account2.addTransaction(transaction17);
//			account2.addTransaction(transaction18);
//			account2.addTransaction(transaction19);
//			account2.addTransaction(transaction20);
//
//			Melba.addAccount(account1);
//			Melba.addAccount(account2);
//			Cameron.addAccount(account3);
//
//			//Loans
//			Loan mortgage = new Loan( "Mortgage", 500000.00, List.of(12,24,36,48,60), 1.10, List.of(1.0,1.05,1.1,1.15,1.2));
//			Loan personal = new Loan("Personal", 100000.00, List.of(6,12,24), 1.2, List.of(1.0, 1.05, 1.1));
//			Loan automotive = new Loan("Automotive", 300000.00, List.of(6,12,24,36), 1.15, List.of(1.0,1.05,1.1,1.15));
//
//			ClientLoan Loan1 = new ClientLoan(400000.00, 60, Melba, mortgage);
//			ClientLoan Loan2 = new ClientLoan(50000.00, 12, Melba, personal);
//			ClientLoan Loan3 = new ClientLoan(100000.00, 24, Cameron, personal);
//			ClientLoan Loan4 = new ClientLoan(200000.00, 32, Cameron, automotive);
//
//			//Cards for Melba
//			Card card1 = new Card(CardType.DEBIT, CardColor.GOLD, Melba, cvv(),randomString(), LocalDate.now(), LocalDate.now().plusYears(5), true);
//			Card card2 = new Card(CardType.CREDIT, CardColor.TITANIUM, Melba, cvv(), randomNumberCard(cardRepository), LocalDate.now(), LocalDate.now().plusYears(5),true );
//			Card cardSample = new Card(CardType.CREDIT, CardColor.SILVER, Melba, cvv(), randomNumberCard(cardRepository), LocalDate.now().minusYears(5), LocalDate.now().minusDays(1), true);
//
//			//Cards Client2
//			Card card3 = new Card(CardType.CREDIT, CardColor.SILVER, Cameron, cvv(), randomNumberCard(cardRepository), LocalDate.now(), LocalDate.now().plusYears(5), true);
//
//			//Melba.addCard(card1);
//			//Melba.addCard(card2);
//
//
//
//			// save a couple of customers, accounts, transactions, loans, cards
//			clientRepository.save(Melba);
//			clientRepository.save(Cameron);
//			clientRepository.save(Admin);
//			accountRepository.save(account1);
//			accountRepository.save(account2);
//			accountRepository.save(account3);
//			transactionRepository.save(transaction1);
//			transactionRepository.save(transaction2);
//			transactionRepository.save(transaction3);
//			transactionRepository.save(transaction4);
//			transactionRepository.save(transaction5);
//			transactionRepository.save(transaction6);
//			transactionRepository.save(transaction7);
//			transactionRepository.save(transaction8);
//			transactionRepository.save(transaction9);
//			transactionRepository.save(transaction10);
//			transactionRepository.save(transaction11);
//			transactionRepository.save(transaction12);
//			transactionRepository.save(transaction13);
//			transactionRepository.save(transaction14);
//			transactionRepository.save(transaction15);
//			transactionRepository.save(transaction16);
//			transactionRepository.save(transaction17);
//			transactionRepository.save(transaction18);
//			transactionRepository.save(transaction19);
//			transactionRepository.save(transaction20);
//			loanRepository.save(mortgage);
//			loanRepository.save(personal);
//			loanRepository.save(automotive);
//			clientLoanRepository.save(Loan1);
//			clientLoanRepository.save(Loan2);
//			clientLoanRepository.save(Loan3);
//			clientLoanRepository.save(Loan4);
//			cardRepository.save(card1);
//			cardRepository.save(card2);
//			cardRepository.save(card3);
//			cardRepository.save(cardSample);
//
//
//		};
//	}


}
