package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private Long id;
    private String number;
    private double balance;
    private LocalDateTime creationDate;
    private Set<TransactionDTO> transactionDTO = new HashSet<>();
    private Boolean deleted;
    private AccountType accountType;

    public AccountDTO() {  }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.accountType = account.getAccountType();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.creationDate = account.getCreationDate();
        this.deleted = account.getDeleted();
        this.transactionDTO = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
    }

    public Long getId() { return id;  }
    public String getNumber() { return number; }
    public double getBalance() {  return balance;  }
    public LocalDateTime getCreationDate() { return creationDate; }
    public Set<TransactionDTO> getTransactionDTO() { return transactionDTO; }
    public Boolean getDeleted() {return deleted;}
    public AccountType getAccountType() {return accountType;}

    public void setAccountType(AccountType accountType) {this.accountType = accountType;}
    public void setDeleted(Boolean deleted) {this.deleted = deleted;}
    public void setId(Long id) {  this.id = id; }
    public void setNumber(String number) {   this.number = number; }
    public void setBalance(double balance) {  this.balance = balance;  }
    public void setCreationDate(LocalDateTime creationDate) {  this.creationDate = creationDate;  }
    public void setTransactionDTO(Set<TransactionDTO> transactionDTO) { this.transactionDTO = transactionDTO;}

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", balance=" + balance +
                ", creationDate=" + creationDate +
                ", transactionDTO=" + transactionDTO +
                '}';
    }
}
