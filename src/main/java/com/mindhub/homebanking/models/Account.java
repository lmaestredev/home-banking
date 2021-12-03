package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    Set<Transaction> transactions = new HashSet<Transaction>();

    private String number;
    private double balance;
    private LocalDateTime creationDate;
    private Boolean deleted;
    private AccountType accountType;

    public Account() { }

    public Account(String number, double balance, LocalDateTime creationDate, Boolean deleted,AccountType accountType, Client client) {
        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;
        this.deleted = deleted;
        this.accountType = accountType;
        this.client = client;
    }

    public void setNumber(String number) { this.number = number; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public void setBalance(double balance) {  this.balance = balance; }
    public void setClient(Client client) { this.client = client;  }
    public void setDeleted(Boolean deleted) {this.deleted = deleted;}
    public AccountType getAccountType() {return accountType;}

    public void setAccountType(AccountType accountType) {this.accountType = accountType;}
    public Boolean getDeleted() { return deleted;}
    public Long getId() {  return id; }
    public String getNumber() { return number;  }
    public LocalDateTime getCreationDate() {  return creationDate; }
    public double getBalance() { return balance;  }
    @JsonIgnore public Client getClient() {  return  client; }
    public Set<Transaction> getTransactions() {  return transactions;  }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", client=" + client +
                ", transactions=" + transactions +
                ", number='" + number + '\'' +
                ", balance=" + balance +
                ", creationDate=" + creationDate +
                '}';
    }
}
