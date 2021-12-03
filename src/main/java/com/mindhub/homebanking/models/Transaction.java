package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime creationDate;
    private double accountBalance;

    public Transaction() { }

    public Transaction(TransactionType type, double amount, String description,LocalDateTime creationDate, Account account, double accountBalance) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.creationDate = creationDate;
        this.account = account;
        this.accountBalance = accountBalance;
    }

    public TransactionType getType() {  return type;  }
    public Long getId() {  return id; }
    public double getAmount() { return amount;  }
    public String getDescription() {  return description; }
    public LocalDateTime getCreationDate() { return creationDate;  }
    @JsonIgnore public Account getAccount() {  return  account; }
    public double getAccountBalance() {return accountBalance;}

    public void setAccountBalance(double accountBalance) {this.accountBalance = accountBalance;}
    public void setType(TransactionType type) {  this.type = type;  }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description;  }
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}
    public void setAccount(Account account) { this.account = account;  }


    @Override
    public String toString() {
        return "Transaction{" +
                "account=" + account +
                ", id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
