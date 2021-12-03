package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/api/accounts")
    public ResponseEntity<?> getClients() {
        return new ResponseEntity<>(Util.makeDTO("accounts",this.accountRepository.findAll().stream().map(AccountDTO::new).collect(toList())), HttpStatus.OK);
    }

    @GetMapping("/api/accounts/{id}")
    public ResponseEntity<?> getClient(@PathVariable Long id){
        return new ResponseEntity<>(Util.makeDTO("account", this.accountRepository.findById(id).map(AccountDTO::new).orElse(null)), HttpStatus.OK);
    }

    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> createANewAccount(Authentication authentication, @RequestParam String accountType){

            Client authenticatedClient = clientRepository.findByEmail(authentication.getName());
            Set<Account> clientAccounts = authenticatedClient.getAccounts().stream().filter(account -> !account.getDeleted()).collect(Collectors.toSet());

            if (accountType.isEmpty()){
                return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
            }
            if (clientAccounts.size() < 3){

                int maxNumberAccount = 99999999;
                int minNumberAccount = 10000000;

                int accountNumber;

                do{
                    accountNumber = (int) (Math.random() * (maxNumberAccount - minNumberAccount + 1) ) + minNumberAccount;;
                }
                while ( accountRepository.findByNumber("VIN-" + accountNumber) != null );

                accountRepository.save(new Account("VIN-" + accountNumber, 0.0, LocalDateTime.now(), false, AccountType.valueOf(accountType), authenticatedClient));
                return new ResponseEntity<>("Account created seccessfully",HttpStatus.CREATED);

            }else {
                return new ResponseEntity<>("you already have the number of accounts allowed", HttpStatus.FORBIDDEN);
            }

    }

    @DeleteMapping("/api/deleteAccount/{accountNumber}")
        public ResponseEntity<?> deleteAccount(Authentication authentication, @PathVariable String accountNumber ){

        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());
        Account accountToDelete = accountRepository.findByNumber(accountNumber);

        if (accountNumber.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(authenticatedClient.getAccounts().stream().filter(account -> account.getNumber().contains(accountNumber)).collect(Collectors.toSet()).size() == 0){
            return new ResponseEntity<>("Account to delete is not yours", HttpStatus.FORBIDDEN);
        }
        if (accountToDelete.getBalance() > 0){
            return new ResponseEntity<>("Withdraw all your money, please ", HttpStatus.FORBIDDEN);
        }
        accountToDelete.setDeleted(true);
        accountRepository.save(accountToDelete);
        return new ResponseEntity<>("Account deleted successfully",HttpStatus.CREATED);

    }


}