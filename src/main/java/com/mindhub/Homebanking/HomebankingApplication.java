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

import static com.mindhub.Homebanking.Utils.Utilities.*;


@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;
	public static void main(String[] args) { SpringApplication.run(HomebankingApplication.class, args);	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime nextDay = now.plusDays(1);
			LocalDateTime yesterday = now.minusDays(1);
			LocalDateTime lastMonth = now.minusMonths(1);
			LocalDateTime lastTwoMonths = now.minusMonths(2);


			Client Melba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba"));
			Client Cameron = new Client("Cameron", "Velasquez", "cameronlino@outlook.com", passwordEncoder.encode("564Cam"));
			Client Admin = new Client("Admin", "Admin", "admin@mindhub.com", passwordEncoder.encode("000000"));

			Account account1 = new Account("VIN001", now, 5000.00);
			Account account2 = new Account( "VIN002", nextDay, 7500.00);
			Account account3 = new Account( "VIN003", nextDay, 7500.00);



			//Transacciones account1
			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 2500.00, "transfered from bank account", yesterday.minusHours(1) );
			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 1500.00, "transfered from bank account", yesterday );
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, 20.00, "transfered to another bank account", now.minusHours(10));
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, 10.50, "transfered to another bank account", lastMonth);
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 1000.00, "transfered from bank account", now.minusHours(5) );

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account1.addTransaction(transaction4);
			account1.addTransaction(transaction5);

			//Transacciones account2
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, 85.00, "transfered to another bank account", now );
			Transaction transaction7 = new Transaction(TransactionType.DEBIT, 24.50, "transfered to another bank account", now );
			Transaction transaction8 = new Transaction(TransactionType.CREDIT, 7500.00, "transfered from bank account", lastMonth.minusHours(10) );
			Transaction transaction9 = new Transaction(TransactionType.DEBIT, 14.00, "transfered to another bank account", lastTwoMonths.minusHours(5) );
			Transaction transaction10 = new Transaction(TransactionType.DEBIT, 5.00, "transfered to another bank account", lastTwoMonths.minusHours(2) );

			account2.addTransaction(transaction6);
			account2.addTransaction(transaction7);
			account2.addTransaction(transaction8);
			account2.addTransaction(transaction9);
			account2.addTransaction(transaction10);

			//Transacciones account 1
			Transaction transaction11 = new Transaction(TransactionType.CREDIT, 2000.00,"Sale of jacket",LocalDateTime.now());
			Transaction transaction12 = new Transaction(TransactionType.CREDIT, 1500.00,"Sale of footwear",LocalDateTime.now());
			Transaction transaction13 = new Transaction(TransactionType.CREDIT, 3000.00,"Sale of jeweler's",LocalDateTime.now());
			Transaction transaction14 = new Transaction(TransactionType.DEBIT, 1000.00,"Purchase of papers",LocalDateTime.now());
			Transaction transaction15 = new Transaction(TransactionType.DEBIT, 500.21,"Purchase of snacks",LocalDateTime.now());

			account1.addTransaction(transaction11);
			account1.addTransaction(transaction12);
			account1.addTransaction(transaction13);
			account1.addTransaction(transaction14);
			account1.addTransaction(transaction15);

            //Transacciones account 2
			Transaction transaction16 = new Transaction(TransactionType.CREDIT, 7000.00,"Sale of jacket",LocalDateTime.now().minusHours(10));
			Transaction transaction17 = new Transaction(TransactionType.CREDIT, 8500.00,"Sale of footwear",LocalDateTime.now());
			Transaction transaction18 = new Transaction(TransactionType.DEBIT, 2500.00,"Purchase of keyboard",LocalDateTime.now());
			Transaction transaction19 = new Transaction(TransactionType.DEBIT, 3500.00,"Purchase of microphone",LocalDateTime.now());
			Transaction transaction20 = new Transaction(TransactionType.DEBIT, 2500.00,"Purchase of mouse",LocalDateTime.now());

			account2.addTransaction(transaction16);
			account2.addTransaction(transaction17);
			account2.addTransaction(transaction18);
			account2.addTransaction(transaction19);
			account2.addTransaction(transaction20);

			Melba.addAccount(account1);
			Melba.addAccount(account2);
			Cameron.addAccount(account3);

			//Loans
			Loan mortgage = new Loan( "Mortgage", 500000.00, List.of(12,24,36,48,60));
			Loan personal = new Loan("Personal", 100000.00, List.of(6,12,24));
			Loan automotive = new Loan("Automotive", 300000.00, List.of(6,12,24,36));

			ClientLoan Loan1 = new ClientLoan(400000.00, 60, Melba, mortgage);
			ClientLoan Loan2 = new ClientLoan(50000.00, 12, Melba, personal);
			ClientLoan Loan3 = new ClientLoan(100000.00, 24, Cameron, personal);
			ClientLoan Loan4 = new ClientLoan(200000.00, 32, Cameron, automotive);

			//Cards for Melba
			Card card1 = new Card(CardType.DEBIT, CardColor.GOLD, Melba, cvv(),randomString(), LocalDate.now(), LocalDate.now().plusYears(5), true);
			Card card2 = new Card(CardType.CREDIT, CardColor.TITANIUM, Melba, cvv(), randomNumberCard(cardRepository), LocalDate.now(), LocalDate.now().plusYears(5),true );
			Card cardSample = new Card(CardType.CREDIT, CardColor.SILVER, Melba, cvv(), randomNumberCard(cardRepository), LocalDate.now().minusYears(5), LocalDate.now().minusDays(1), true);

			//Cards Client2
			Card card3 = new Card(CardType.CREDIT, CardColor.SILVER, Cameron, cvv(), randomNumberCard(cardRepository), LocalDate.now(), LocalDate.now().plusYears(5), true);

			//Melba.addCard(card1);
			//Melba.addCard(card2);



			// save a couple of customers, accounts, transactions, loans, cards
			clientRepository.save(Melba);
			clientRepository.save(Cameron);
			clientRepository.save(Admin);
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);
			transactionRepository.save(transaction11);
			transactionRepository.save(transaction12);
			transactionRepository.save(transaction13);
			transactionRepository.save(transaction14);
			transactionRepository.save(transaction15);
			transactionRepository.save(transaction16);
			transactionRepository.save(transaction17);
			transactionRepository.save(transaction18);
			transactionRepository.save(transaction19);
			transactionRepository.save(transaction20);
			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);
			clientLoanRepository.save(Loan1);
			clientLoanRepository.save(Loan2);
			clientLoanRepository.save(Loan3);
			clientLoanRepository.save(Loan4);
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(cardSample);


		};
	}


}
