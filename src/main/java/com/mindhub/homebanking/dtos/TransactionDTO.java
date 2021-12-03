package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime creationDate;
    private double accountBalance;

    public TransactionDTO() { }

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description =transaction.getDescription();
        this.creationDate = transaction.getCreationDate();
        this.accountBalance = transaction.getAccountBalance();
    }

    public Long getId() {  return id; }
    public TransactionType getType() { return type;  }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDateTime getCreationDate() {  return creationDate; }
    public double getAccountBalance() {return accountBalance;}

    public void setAccountBalance(double accountBalance) {this.accountBalance = accountBalance;}
    public void setId(Long id) { this.id = id; }
    public void setType(TransactionType type) { this.type = type;  }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
