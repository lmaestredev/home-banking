package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Transactional
@RestController
@RequestMapping
public class LoanController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/api/loan")
    public ResponseEntity<Object> getLoans(){
        return new ResponseEntity<>(Util.makeDTO("loans",this.loanRepository.findAll().stream().map(LoanDTO::new).collect(toList())), HttpStatus.OK);
    }

    @PostMapping("/api/loan")
    public ResponseEntity<Object> newLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){

        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());
        Loan loanRequest = loanRepository.findByName(loanApplicationDTO.getLoanName());
        Account destinantionAccount = accountRepository.findByNumber(loanApplicationDTO.getDestinationAccountNumber());

        if (loanApplicationDTO.getLoanName().isEmpty() || String.valueOf(loanApplicationDTO.getAmount()).isEmpty() || loanApplicationDTO.getPayments().toString().isEmpty() || loanApplicationDTO.getDestinationAccountNumber().isEmpty() || loanApplicationDTO.getPayments().equals(0) || loanApplicationDTO.getPayments().equals(0)){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (destinantionAccount == null){
            return new ResponseEntity<>("Non-exist this account", HttpStatus.FORBIDDEN);
        }

        if (authenticatedClient.getAccounts().stream().filter(account -> account.getNumber().contains(loanApplicationDTO.getDestinationAccountNumber())).collect(Collectors.toSet()) == null){
            return new ResponseEntity<>("Destination account is not yours", HttpStatus.FORBIDDEN);
        }

        if (loanRequest == null){
            return new ResponseEntity<>("Non-exist this kind of loan", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() > loanRequest.getMaxAmount() || loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("Amount is not correct", HttpStatus.FORBIDDEN);
        }

        if (loanRepository.findByName(loanApplicationDTO.getLoanName()).getPayments().contains(loanApplicationDTO.getPayments()) == false){
            return new ResponseEntity<>("The payments are not available to this loan", HttpStatus.FORBIDDEN);
        }

        Set<ClientLoan> setToVerifyLoanRequest = authenticatedClient.getClientLoans().stream().filter(clientLoan -> clientLoan.getLoan().getName().contains(loanApplicationDTO.getLoanName())).collect(Collectors.toSet());
        if(setToVerifyLoanRequest.size() > 0){
            return new ResponseEntity<>("You already have a loan like this", HttpStatus.FORBIDDEN);
        }

        double multiplicationByPercentage = loanApplicationDTO.getAmount() * loanRequest.getPercentage();
        double percent = multiplicationByPercentage / 100;
        double newAmountToPay = loanApplicationDTO.getAmount() + percent;

        ClientLoan newClientLoan = new ClientLoan(newAmountToPay, loanApplicationDTO.getPayments());

        loanRequest.addClientLoan(newClientLoan);
        authenticatedClient.addClientLoan(newClientLoan);
        loanRepository.save(loanRequest);
        clientRepository.save(authenticatedClient);
        clientLoanRepository.save(newClientLoan);

        Transaction newTransaction = new Transaction(TransactionType.CREDIT,loanApplicationDTO.getAmount(), "loan approved / " + loanApplicationDTO.getLoanName(), LocalDateTime.now(), destinantionAccount, destinantionAccount.getBalance() + loanApplicationDTO.getAmount());
        transactionRepository.save(newTransaction);

        destinantionAccount.setBalance(destinantionAccount.getBalance() + loanApplicationDTO.getAmount());

        return new ResponseEntity<>("Transaction send succesfully",HttpStatus.CREATED);
    }

    @PostMapping("/api/admin/newLoan")
    public ResponseEntity<?> newLoan(Authentication authentication, String loanName, double maxAmount,Integer payments, double percentage){

        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());

        if (authenticatedClient.getRole().toString() != "ADMIN"){
            return new ResponseEntity<>("You don´t have access to this service", HttpStatus.FORBIDDEN);
        }

        if (loanName.isEmpty() || maxAmount == 0 || payments == 0 || percentage == 0){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(maxAmount > 200000){
             return new ResponseEntity<>("Max amount is not available", HttpStatus.FORBIDDEN);
        }

        if (payments != 6 && payments != 12){
            return new ResponseEntity<>("This payments are not available", HttpStatus.FORBIDDEN);
        }

        Loan newLoan = new Loan(loanName, maxAmount, List.of(payments), percentage);
        loanRepository.save(newLoan);
        return new ResponseEntity<>("New loan created successfully",HttpStatus.CREATED);
    }

    @DeleteMapping("/api/delete/loan/{loanId}")
    public ResponseEntity<?> deleteLoan(Authentication authentication, @PathVariable Long loanId){

        Client admin = clientRepository.findByEmail(authentication.getName());

        if (!admin.getRole().toString().equals("ADMIN")){
            return new ResponseEntity<>("You don't have access to this service", HttpStatus.FORBIDDEN);
        }

        if(!loanRepository.existsById(loanId)){
            return new ResponseEntity<>("Loan selected doesn't exists", HttpStatus.FORBIDDEN);
        }
        loanRepository.deleteById(loanId);
        return new ResponseEntity<>("Loan deleted successfully",HttpStatus.CREATED);

    }
}
