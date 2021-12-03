package com.mindhub.homebanking.models;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import static java.util.stream.Collectors.toList;

@Entity public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Account> accounts = new HashSet<Account>();

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Card> cards = new HashSet<>();

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<ClientFrequentContact> clientFrequentContacts = new HashSet<>();


    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;

    public Client() { }

    public Client(String firstName, String lastName, String email, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Role getRole() { return role; }
    public Long getId() { return id; }
    public String getPassword() { return password;  }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public Set<Account> getAccounts() { return accounts; }
    public Set<Card> getCards() { return cards; }
    public Set<ClientLoan> getClientLoans() { return clientLoans; }
    public Set<ClientFrequentContact> getClientFrequentContacts() { return clientFrequentContacts;}
    @JsonIgnore public List<Loan> getLoans() { return clientLoans.stream().map(sub -> sub.getLoan()).collect(toList());}
    @JsonIgnore public List<FrequentContact> getFrequentContacts() { return clientFrequentContacts.stream().map(sub -> sub.getFrequentContact()).collect(toList());}


    public void setRole(Role role) { this.role = role; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setClientFrequentContacts(Set<ClientFrequentContact> clientFrequentContacts) {  this.clientFrequentContacts = clientFrequentContacts; }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public void addClientFrequentContact(ClientFrequentContact clientFrequentContact) {
        clientFrequentContact.setClient(this);
        clientFrequentContacts.add(clientFrequentContact);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", accounts=" + accounts +
                ", cards=" + cards +
                ", clientLoans=" + clientLoans +
                ", clientFrequentContacts=" + clientFrequentContacts +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
