package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.homebanking.models.Client;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class FrequentContact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @OneToMany(mappedBy="frequentContact", fetch=FetchType.EAGER)
    Set<ClientFrequentContact> clientFrequentContacts = new HashSet<>();

    private String firstName;
    private String lastName;
    private String accountNumber;

    public FrequentContact() { }

    public FrequentContact(String firstName, String lastName, String accountNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountNumber = accountNumber;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getNumber() {  return accountNumber; }
    @JsonIgnore
    public List<Client> getClients() { return clientFrequentContacts.stream().map(sub -> sub.getClient()).collect(toList());}

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName;}
    public void setNumber(String accountNumber) {  this.accountNumber = accountNumber; }

    public void addClientFrequentContact(ClientFrequentContact clientFrequentContact) {
        clientFrequentContact.setFrequentContact(this);
        clientFrequentContacts.add(clientFrequentContact);
    }


    @Override
    public String toString() {
        return "FrequentContact{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}
