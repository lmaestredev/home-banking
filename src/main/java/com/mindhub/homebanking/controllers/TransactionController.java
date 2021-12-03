package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.FrequentContact;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@RestController
@RequestMapping
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FrequentContactRepository frequentContactRepository;

    @Autowired
    private ClientFrequentContactRepository clientFrequentContactRepository;

    @Autowired
    private PDFService pdfService;

    @PostMapping("/api/transactions")
    public ResponseEntity<Object> transaction(Authentication authentication,
                                              @RequestParam String originAccountNumber, @RequestParam String destinationAccountNumber,
                                              @RequestParam String amount, @RequestParam String description,@RequestParam Boolean confirmToAdd, @RequestParam String contactType){

        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());
        Account originAccount = accountRepository.findByNumber(originAccountNumber);
        Account destinationAccount = accountRepository.findByNumber(destinationAccountNumber);
        Set<Account> setToVerifyOriginAccount = authenticatedClient.getAccounts().stream().filter(account -> account.getNumber().contains(originAccountNumber)).collect(Collectors.toSet());

        if (originAccount == null){
            return new ResponseEntity<>("Non-exist origin account", HttpStatus.FORBIDDEN);
        }

        if(destinationAccount == null){
            return new ResponseEntity<>("Non-exist destination account", HttpStatus.FORBIDDEN);
        }

        if( amount.isEmpty() || description.isEmpty() || originAccountNumber.isEmpty() || destinationAccountNumber.isEmpty() || contactType.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        Double transformedAmount = Double.valueOf(amount);

        if( transformedAmount <= 0){
            return new ResponseEntity<>("Please sign in a valid amount", HttpStatus.FORBIDDEN);
        }

        if(originAccountNumber.equals(destinationAccountNumber)){
            return new ResponseEntity<>("Accounts must be different", HttpStatus.FORBIDDEN);
        }

        if (setToVerifyOriginAccount.size() == 0){
            return new ResponseEntity<>("Invalid origin account number", HttpStatus.FORBIDDEN);
        }

        if (setToVerifyOriginAccount.iterator().next().getBalance() < transformedAmount ){
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }

        if (confirmToAdd && contactType.equals("no") ){
            return new ResponseEntity<>("Choose a kind of contact, please", HttpStatus.FORBIDDEN);
        }

        if (confirmToAdd && !contactType.equals("no")){

            //FrequentContact frequentContact  = frequentContactRepository.findByAccountNumber(destinationAccountNumber);

            if (frequentContactRepository.findByAccountNumber(destinationAccountNumber) == null){

                FrequentContact newFrequentContact = new FrequentContact(destinationAccount.getClient().getFirstName(), destinationAccount.getClient().getLastName(), destinationAccountNumber);
                ClientFrequentContact clientFrequentContact = new ClientFrequentContact(ContactType.valueOf(contactType));

                newFrequentContact.addClientFrequentContact(clientFrequentContact);
                authenticatedClient.addClientFrequentContact(clientFrequentContact);

                frequentContactRepository.save(newFrequentContact);
                clientFrequentContactRepository.save(clientFrequentContact);

                double newBalanceOriginAccount = originAccount.getBalance() - transformedAmount ;
                double newBalanceDestinationAccount = destinationAccount.getBalance() + transformedAmount;

                originAccount.setBalance(newBalanceOriginAccount);
                destinationAccount.setBalance(newBalanceDestinationAccount);

                accountRepository.save(originAccount);
                accountRepository.save(destinationAccount);

                Transaction creditTransaction =  new Transaction(TransactionType.DEBIT, transformedAmount , description, LocalDateTime.now(),originAccount, newBalanceOriginAccount);
                Transaction debitTransaction =  new Transaction(TransactionType.CREDIT, transformedAmount , description, LocalDateTime.now(),destinationAccount, newBalanceDestinationAccount);

                transactionRepository.save(creditTransaction);
                transactionRepository.save(debitTransaction);

                return new ResponseEntity<>("Added to frequent contacts and transaction sent succesfully",HttpStatus.CREATED);

            }else{

                if (authenticatedClient.getClientFrequentContacts().stream().filter(clientFrequentContact -> clientFrequentContact.getFrequentContact().getId().equals(frequentContactRepository.findByAccountNumber(destinationAccountNumber).getId())).collect(Collectors.toSet()).size() > 0){
                    return new ResponseEntity<>("This contact already exists in your directory", HttpStatus.FORBIDDEN);
                }

                double newBalanceOriginAccount = originAccount.getBalance() - transformedAmount ;
                double newBalanceDestinationAccount = destinationAccount.getBalance() + transformedAmount;

                originAccount.setBalance(newBalanceOriginAccount);
                destinationAccount.setBalance(newBalanceDestinationAccount);

                accountRepository.save(originAccount);
                accountRepository.save(destinationAccount);

                Transaction creditTransaction =  new Transaction(TransactionType.DEBIT, transformedAmount , description, LocalDateTime.now(),originAccount, newBalanceOriginAccount);
                Transaction debitTransaction =  new Transaction(TransactionType.CREDIT, transformedAmount , description, LocalDateTime.now(),destinationAccount, newBalanceDestinationAccount);

                transactionRepository.save(creditTransaction);
                transactionRepository.save(debitTransaction);

                FrequentContact frequentContact  = frequentContactRepository.findByAccountNumber(destinationAccountNumber);
                ClientFrequentContact clientFrequentContact = new ClientFrequentContact(ContactType.valueOf(contactType));

                frequentContact.addClientFrequentContact(clientFrequentContact);
                authenticatedClient.addClientFrequentContact(clientFrequentContact);

                frequentContactRepository.save(frequentContact);
                clientFrequentContactRepository.save(clientFrequentContact);

                return new ResponseEntity<>("Added to frequent contacts and transaction sent succesfully",HttpStatus.CREATED);
            }
        }else{

            double newBalanceOriginAccount = originAccount.getBalance() - transformedAmount ;
            double newBalanceDestinationAccount = destinationAccount.getBalance() + transformedAmount;

            originAccount.setBalance(newBalanceOriginAccount);
            destinationAccount.setBalance(newBalanceDestinationAccount);

            accountRepository.save(originAccount);
            accountRepository.save(destinationAccount);

            Transaction creditTransaction =  new Transaction(TransactionType.DEBIT, transformedAmount , description, LocalDateTime.now(),originAccount, newBalanceOriginAccount);
            Transaction debitTransaction =  new Transaction(TransactionType.CREDIT, transformedAmount , description, LocalDateTime.now(),destinationAccount, newBalanceDestinationAccount);

            transactionRepository.save(creditTransaction);
            transactionRepository.save(debitTransaction);

            return new ResponseEntity<>("Transaction sent succesfully",HttpStatus.CREATED);
        }
    }

    @PostMapping("/api/transactions/pdf")
    public ResponseEntity<?> generatedPDF(HttpServletResponse httpServletResponse, Authentication authentication,
                                          @RequestParam String account1, @RequestParam String account2,
                                          @RequestParam double amount, @RequestParam String description) throws IOException{

        httpServletResponse.setContentType("application/pdf");
        Client client1 = clientRepository.findByEmail(authentication.getName());
        Client client2 = accountRepository.findByNumber(account2).getClient();
        Account destinationAccount = accountRepository.findByNumber(account2);
        DateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss");
        String currentDatTime= dateFormat.format(new Date());


        String headerKey = "Content-Disposition";
        String headerValue = "attacment; filename=Transferencia_a_"+destinationAccount.getNumber()+"_"+currentDatTime+".pdf";
        httpServletResponse.setHeader(headerKey,headerValue);

        this.pdfService.export(httpServletResponse, client1, client2, account1, account2, amount, description);
        return new ResponseEntity<>("Pdf created successfully", HttpStatus.OK);
    }
}
