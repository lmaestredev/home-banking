package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO{

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accountsDTO = new HashSet<>();
    private Set<ClientLoanDTO> clientLoansDTO = new HashSet<>();
    private Set<CardDTO> cardsDTO = new HashSet<>();
    private Set<ClientFrequentContactDTO> clientFrequentContactsDTO = new HashSet<>();
    private Role role;


    public ClientDTO() {  }

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accountsDTO = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
        this.clientLoansDTO = client.getClientLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toSet());
        this.cardsDTO = client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
        this.role = client.getRole();
        this.clientFrequentContactsDTO = client.getClientFrequentContacts().stream().map(ClientFrequentContactDTO::new).collect(Collectors.toSet());
    }

    public Long getId() {  return id; }
    public String getFirstName() {  return firstName; }
    public String getLastName() {   return lastName;  }
    public String getEmail() { return email; }
    public Set<AccountDTO> getAccountsDTO() { return accountsDTO;  }
    public Set<CardDTO> getCardsDTO() { return cardsDTO; }
    public Set<ClientLoanDTO> getClientLoansDTO() { return clientLoansDTO; }
    public Role getRole() {return role;}
    public Set<ClientFrequentContactDTO> getClientFrequentContactsDTO() { return clientFrequentContactsDTO;}

    public void setRole(Role role) { this.role = role;}
    public void setId(Long id) {  this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) {   this.lastName = lastName; }
    public void setEmail(String email) {  this.email = email;  }
    public void setAccountsDTO(Set<AccountDTO> accounts) {  this.accountsDTO = accounts; }
    public void setCardsDTO(Set<CardDTO> cardsDTO) { this.cardsDTO = cardsDTO; }
    public void setClientLoansDTO(Set<ClientLoanDTO> clientLoansDTO) { this.clientLoansDTO = clientLoansDTO; }
    public void setClientFrequentContactsDTO(Set<ClientFrequentContactDTO> clientFrequentContactsDTO) {this.clientFrequentContactsDTO = clientFrequentContactsDTO;}


}
