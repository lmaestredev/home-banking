package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientFrequentContactRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.FrequentContactRepository;
import com.mindhub.homebanking.util.Util;
import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientFrequentContactRepository clientFrequentContactRepository;

    @Autowired
    private FrequentContactRepository frequentContactRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/api/clients")
    public ResponseEntity<?> getClients() {
        return new ResponseEntity<>(Util.makeDTO("clients",this.clientRepository.findAll().stream().map(ClientDTO::new).collect(toList())), HttpStatus.OK);
    }

    @GetMapping("/api/clients/{id}")
    public ResponseEntity<?> getClient(@PathVariable Long id){
        return new ResponseEntity<>(Util.makeDTO("client", this.clientRepository.findById(id).map(ClientDTO::new).orElse(null)), HttpStatus.OK);
    }

    @GetMapping("/api/clients/current")
    public ResponseEntity<?> getClient(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        ClientDTO clientDTO = new ClientDTO(client);
        return new ResponseEntity<>(Util.makeDTO("current", clientDTO), HttpStatus.OK);
    }

    @PostMapping("/api/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName,@RequestParam String lastName,@RequestParam String email,@RequestParam String password){

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Random random = new Random();
        int cantidad = random.nextInt(100000000) + 10000000;

        Client newClient = new Client(firstName,lastName,email,passwordEncoder.encode(password), Role.CLIENT);
        Account newAccount = new Account("VIN-"+cantidad, 0.0, LocalDateTime.now(), false, AccountType.SAVINGS, newClient);

        clientRepository.save(newClient);
        accountRepository.save(newAccount);

        return new ResponseEntity<>("Created user successfully",HttpStatus.CREATED);

    }

    @DeleteMapping("/api/deleteContact/{id}")
    public ResponseEntity<?> deleteContact(Authentication authenticated, @PathVariable String id){

        Client authenticatedClient = clientRepository.findByEmail(authenticated.getName());

        if (id.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (authenticatedClient.getClientFrequentContacts().stream().filter(contact -> contact.getId() == (Long.valueOf(id))).collect(Collectors.toSet()).size() == 0){
            return new ResponseEntity<>("Contact to delete is not yours", HttpStatus.FORBIDDEN);
        }

        clientFrequentContactRepository.deleteById(Long.valueOf(id));

        return new ResponseEntity<>("Contact deleted successfully", HttpStatus.OK);
    }
}
