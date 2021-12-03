package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

import java.util.HashSet;
import java.util.Set;

public class ClientLoanDTO {
    private Long id;
    private Long loanId;
    private String loanName;
    private double amount;
    private Integer payments;

    public ClientLoanDTO() { }

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.loanName = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }

    public Long getId() { return id; }
    public Long getLoanId() {  return loanId; }
    public String getLoanName() { return loanName;  }
    public double getAmount() { return amount; }
    public Integer getPayments() { return payments; }

    public void setId(Long id) {  this.id = id; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }
    public void setLoanName(String loanName) { this.loanName = loanName; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setPayments(Integer payments) { this.payments = payments;  }


}
