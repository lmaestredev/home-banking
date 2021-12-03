package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
    @Autowired
    private PasswordEncoder passwordEnconder;

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){

        return (args) -> {

            Client client1 = new Client("Melba","Moral", "melba@mindhub.com", passwordEnconder.encode("melba123"), Role.CLIENT);//instancia de objeto
            Client client2 = new Client("Luis","Maestre", "luis@mindhub.com", passwordEnconder.encode("luis123"),  Role.CLIENT);//instancia de objeto
            Client client3 = new Client("Jose", "ADMIN", "admin@mindhub.com", passwordEnconder.encode("admin123"),  Role.ADMIN );



            Account account1 = new Account("VIN001", 5000, LocalDateTime.now(), false, AccountType.CHECKING, client1);
            Account account2 = new Account("VIN002", 7500, LocalDateTime.now().plusDays(1), false, AccountType.SAVINGS, client1 );
            Account account3 = new Account("VIN003", 8500, LocalDateTime.now(), false, AccountType.CHECKING, client2);
            Account account4 = new Account("VIN004", 6400, LocalDateTime.now().plusDays(1), false, AccountType.SAVINGS, client2 );

            clientRepository.save(client1);
            clientRepository.save(client2);
            clientRepository.save(client3);

            accountRepository.save(account1);
            accountRepository.save(account2);
            accountRepository.save(account3);
            accountRepository.save(account4);


            Transaction transaction1 = new Transaction(TransactionType.CREDIT,1500, "salary", LocalDateTime.now(), account1, account1.getBalance() + 1500);
            Transaction transaction2 = new Transaction(TransactionType.DEBIT,2500, "shoppings", LocalDateTime.now().plusDays(1), account1, account1.getBalance() - 2500);
            Transaction transaction3 = new Transaction(TransactionType.DEBIT,4500, "phone accessory", LocalDateTime.now(), account1, account1.getBalance() - 4500);
            Transaction transaction4 = new Transaction(TransactionType.CREDIT,5500, "salary", LocalDateTime.now().plusDays(1), account1, account1.getBalance());
            Transaction transaction5 = new Transaction(TransactionType.DEBIT,1500, "shoes", LocalDateTime.now().now(), account2, account2.getBalance() - 1500);
            Transaction transaction6 = new Transaction(TransactionType.DEBIT,500, "food", LocalDateTime.now().plusDays(2), account2, account2.getBalance() - 500);
            Transaction transaction7 = new Transaction(TransactionType.DEBIT,700, "accessory", LocalDateTime.now().plusDays(3), account2, account2.getBalance() - 700);
            Transaction transaction8 = new Transaction(TransactionType.CREDIT,7000, "salary", LocalDateTime.now().plusDays(4), account2, account2.getBalance());



            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);
            transactionRepository.save(transaction3);
            transactionRepository.save(transaction4);
            transactionRepository.save(transaction5);
            transactionRepository.save(transaction6);
            transactionRepository.save(transaction7);
            transactionRepository.save(transaction8);

            Loan loan1 = new Loan ("Mortgage", 500000, List.of(12, 24, 36, 48, 60), 5);
            Loan loan2 = new Loan ("Personal", 100000, List.of(6,12,24), 25);
            Loan loan3 = new Loan ("Car", 300000, List.of(6,12,24,36), 15);

            ClientLoan clientloan1 = new ClientLoan( 400000, 60);
            ClientLoan clientloan2 = new ClientLoan( 50000, 12);
            ClientLoan clientloan3 = new ClientLoan( 100000, 24);
            ClientLoan clientloan4 = new ClientLoan( 200000, 36);

            loan1.addClientLoan(clientloan1);
            loan2.addClientLoan(clientloan2);
            loan2.addClientLoan(clientloan3);
            loan3.addClientLoan(clientloan4);

            client1.addClientLoan(clientloan1);
            client1.addClientLoan(clientloan2);
            client2.addClientLoan(clientloan3);
            client2.addClientLoan(clientloan4);

            loanRepository.save(loan1);
            loanRepository.save(loan2);
            loanRepository.save(loan3);

            clientLoanRepository.save(clientloan1);
            clientLoanRepository.save(clientloan2);
            clientLoanRepository.save(clientloan3);
            clientLoanRepository.save(clientloan4);

            int maxNumberCard = 9999;
            int minNumberCard = 1000;

            Card card1 = new Card(CardType.DEBIT,CardColor.GOLD,((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard)+"-"+((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard)+"-"+((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard)+"-"+((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard), 111,LocalDateTime.now(), LocalDateTime.now(),"no",client1 );
            Card card2 = new Card(CardType.CREDIT,CardColor.TITANIUM, ((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard)+"-"+((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard)+"-"+((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard)+"-"+((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard), 222,LocalDateTime.now(), LocalDateTime.now(),"no", client1 );
            Card card3 = new Card(CardType.CREDIT,CardColor.SILVER, ((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard)+"-"+((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard)+"-"+((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard)+"-"+((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard), 333,LocalDateTime.now(), LocalDateTime.now().plusYears(5),"no", client1 );
            Card card4 = new Card(CardType.CREDIT,CardColor.TITANIUM, ((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard)+"-"+((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard)+"-"+((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard)+"-"+((int) (Math.random() * (maxNumberCard - minNumberCard + 1) ) + minNumberCard), 444,LocalDateTime.now(), LocalDateTime.now().plusYears(5),"no",client2 );

            cardRepository.save(card1);
            cardRepository.save(card2);
            cardRepository.save(card3);
            cardRepository.save(card4);
        };
    }
}
